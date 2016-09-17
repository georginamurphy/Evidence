package evidence.clientserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import evidence.gui.ServerGUI;

/**
 * The server is responsible for keeping track of any users connected to the server
 * and sending / receiving packets to and from them.  For example, if a player moves 
 * in their client, we must send a packet to the server containing the player's new 
 * location, and propagate that packet to the other user's so they can update that
 * player's location on their own client.
 * 
 * The server uses 4 different threads to handle different functionality that
 * the server may provide.  This is mainly because receive() will sit and wait
 * until it receives something through the socket.  If we did not have a separate 
 * thread for this, the entire server would sit and wait and this is not desirable.
 * 
 * * In a Model-View-Controller sense, Server acts as the controller.
 * 
 * @author Tyler Jones
 */
public class Server implements Runnable{
	
	// Port number this server is running on
	private int port;
	
	// A socket for the server to send and receive packets through
	private DatagramSocket socket;
	
	// A boolean to keep track of whether the server is running
	private boolean running;
	
	// Threads to handle different aspects of the server
	private Thread run, send, receive, manage;
	
	// An ArrayList containing all the clients connected to the server
	private ArrayList<ServerClient> clients = new ArrayList<ServerClient>();
	
	// A simple Window that servers as a formal log for the server
	private ServerGUI gui;
	
	/**
	 * Constructor for a server instance
	 * 
	 * @param port - The port the server will be running on
	 * @param gui - The GUI object for the server
	 */
	public Server(int port, ServerGUI gui){
		this.port = port;
		this.gui = gui;
		
		// Try to create a socket for the port given in the command line arguments
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		// If successful, create a new Thread for the server and start the thread
		// Make a record of the server starting successfully
		run = new Thread(this, "Server");
		run.start();
	}

	/**
	 * Called when the run thread is started
	 * Puts the program in the running state and starts
	 * the manage / receive threads.
	 */
	@Override
	public void run() {
		running = true;
		gui.writeToLog("Server successfully started on port: " + port);
		manageClients();
		receive();
	}

	/**
	 * Starts the manage thread.  Will loop repeatedly while the program
	 * is running and manage the client list.  Managing clients is basically
	 * handling disconnections, whether expected or not, from the server.
	 */
	private void manageClients() {
		manage = new Thread("Manager") {
			public void run(){
				while(running){
					// Managing...
				}
			}
		};
		manage.start();
	}

	/**
	 * Starts the receive thread.  Will loop repeatedly while the program is running
	 * and wait to receive messages from the socket.
	 * 
	 * When it receives a packet, it will call the process method to handle
	 * the packet
	 */
	private void receive() {
		receive = new Thread("Receiver") {
			public void run(){
				while(running){
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					//
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}
	
	/**
	 * Upon receiving a packet, this method is called.
	 * Responsible for reading the header of the packet and
	 * performing the appropriate action based on it.
	 * 
	 * HEADERS:
	 * "/c/" - A connection packet, used upon initial connection
	 * "/m/" - A message packet, used when users send messages to the server
	 * 
	 * @param packet - The packet to process
	 */
	private void process(DatagramPacket packet) {
		// Record the server processing a packet
		gui.writeToLog("Processing packet from: " + packet.getAddress() + ":" + packet.getPort() );
		String string = new String(packet.getData() );
		
		// Is this packet a connection packet?
		if(string.startsWith("/c/") ){
			//Assign a unique identifier for this client
			int id = UniqueIdentifier.getIdentifier();
			clients.add(new ServerClient(string.split("/c/|/e/")[1], packet.getAddress(),
					packet.getPort(), id) );
			System.out.println("Added to clients: " + string.split("/c/|/e/")[1] + " with ID " + id);
			
			// Record who we connected to the server
			gui.writeToLog("Added to clients: " + string.split("/c/|/e/")[1] + " with ID " + id);
			
			// Form a connection confirmation packet, and send it,
			// to the client to confirm a connection was successful
			String confirm = "/c/" + id;
			send(confirm, packet.getAddress(), packet.getPort() );
			
			// Record we sent a connect packet and to who
			gui.writeToLog("Sent confirm packet to: " + packet.getAddress() + ":" + packet.getPort() );
		}
		
		// Is this packet a disconnection packet?
		else if(string.startsWith("/dc/") ){
			String ID = string.split("/dc/|/e/")[1];
			disconnect(Integer.parseInt(ID), true);
		}
		
		// Is this packet a message packet?
		else if(string.startsWith("/m/") ){
			sendToAll(string);
		}
		
		// If we could not categorize the packet, print to the server log
		else{
			gui.writeToLog("Could not categorize packet from " + packet.getAddress() + ":" + packet.getPort());
		}
	}
	
	/**
	 * A method to send an array of bytes to a specific address and port,
	 * through our socket.
	 * 
	 * @param data - the bytes to send
	 * @param address - the address to send to
	 * @param port - the port to send to
	 */
	private void send(final byte[] data, InetAddress address, int port){
		// Create a new thread to send the data on and then start the thread
		send = new Thread("ServerSender"){
			public void run(){
				// Create a packet from data, that is the length of data
				// to the IP Address established by our connection, through the specified port
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	/**
	 * Appends an end character to our message
	 * 
	 * @param message - The message to send
	 * @param address - The address to send the packet to
	 * @param port - The port to send the packet to
	 */
	private void send(String message, InetAddress address, int port){
		message += "/e/";
		send(message.getBytes(), address, port);
	}
	
	/**
	 * A simple method that iterates over all the connected clients
	 * in our list, and sends the message to them.
	 * 
	 * @param message - the message to send
	 */
	private void sendToAll(String message){
		// Record we sent a packet and to everyone on the server
		gui.writeToLog("Sent packet to all clients");
		for(int i = 0; i < clients.size(); i++){
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}
	
	/**
	 * Removes a client from our list and reports
	 * the appropriate message to the server log.
	 * 
	 * @param id - ID of the client to disconnect
	 * @param intentional - was the disconnection intentional?
	 */
	private void disconnect(int id, boolean intentional){
		// Find the client with the given id and remove them from our list
		ServerClient sc = null;
		for(int i = 0; i < clients.size(); i++){
			if(clients.get(i).ID == id){
				sc = clients.get(i);
				clients.remove(i);
				break;
			}
		}
		
		// Build an appropriate message for the server log
		String message = "";
		if(intentional){
			message = "Client " + sc.name + "(" + sc.ID + 
					") @" + sc.address + ":" + sc.port + " disconnected";
			
		}
		else{
			message = "Client " + sc.name + "(" + sc.ID + 
					") @" + sc.address + ":" + sc.port + " timed out";
		}
		
		// Record the user being disconnected
		gui.writeToLog(message);
	}
}
