package evidence.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import evidence.clientserver.ClientPipe;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JButton;

/**
 * The client window is the main window that houses the Scene, the Interaction and Chat Panels.
 * It is what the user interacts with to play the game and chat with other uses.  It is
 * the visual component of the program.
 *
 * It uses a ClientPipe to 'middle-man' any interaction between itself and the Server
 *
 * In a Model-View-Controller sense, ClientWindow is the view.
 *
 * @author Tyler Jones
 */
public class ClientWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;

	// Swing components
	private JPanel contentPane;
	private JTextField messageField;
	private JTextArea chatLog;
	private JTextArea timeLeftArea;
	private DefaultCaret caret;

	// The ClientPipe that gives us a "pipe" to the server
	private ClientPipe pipe;

	// A thread to run on
	private Thread run;
	private RenderCanvas canvas;

	/**
	 * Create the frame and attempt to open the connection
	 *
	 * @param name - name of client
	 * @param address - address we are connecting to
	 * @param port - port we are connecting to
	 */
	public ClientWindow(String name, String address, int port) {
		pipe = new ClientPipe(name, address, port, this);

		// Setup the window
		createWindow();
		writeToChatLog("Attempting a connection to address: " + address + ", port: " + port + ", user: " + name);

		// Open the connection with the server, through the client
		if(!pipe.openConnection(address) ){
			writeToChatLog("Connection failed!");
		}

		// Send a connection packet to the server via our client
		String connection = "/c/" + name + "/e/";
		pipe.send(connection.getBytes() );

		// Put the program in running state
		run = new Thread(this, "Running");
		run.start();
	}

	/**
	 * Append a message to our chatLog textArea
	 *
	 * @param message - the message to append
	 */
	public void writeToChatLog(String message){
		chatLog.append(message.trim() + "\n");
	}

	/**
	 * Append the given message to the history, and send it to
	 * the server via client, so that it can propagate to all
	 * the clients connected to the server.
	 *
	 * We append "/m/" to the message so the server knows what type
	 * of packet it is receiving.
	 *
	 * @param message - the message to send to the client
	 */
	private void sendMessage(String message){
		// Do not send empty messages to the server
		if(message.equals("") ){return;}

		// Append the user's name to their message, writeToHistory and clear the txtMessage Area
		message = pipe.getName() + ": " + message;
		message = "/m/" + message;

		// Send message to server
		pipe.send(message.getBytes() );
		messageField.setText("");
	}

	/**
	 * Called when disconnecting intentionally. Will close the socket
	 * in client.
	 */
	public void close(){
		// Close the connection
		pipe.close();

		// Close the window
		dispose();
	}

	/**
	 * Called after a successful connection has been made,
	 * starts another thread which will sit and listen for
	 * packets from the server through the socket
	 */
	@Override
	public void run() {
		pipe.receive();
	}


	// =================================================================================================================================================================
	//                                                             GUI VISUAL STUFF AND LISTENERS
	// =================================================================================================================================================================

	/**
	 * Create the frame.
	 */
	public void createWindow() {
		setResizable(false);
		// Set the lookAndFeel to be native to the system
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width-200,screenSize.height-200);
	    // setBounds(0,0,screenSize.width, screenSize.height);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//1400 by 700
		setBounds(100, 100, 1400, 700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(new TitledBorder(null, "Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		infoPanel.setBounds(768, 11, 306, 639);
		//infoPanel.setBounds(frameSize.width/2, 11, (frameSize.width/4)-10, frameSize.height-11);
		//infoPanel.setBounds(screenSize.width/2, 11, (3/14)*screenSize.width, screenSize.height-211);
		contentPane.add(infoPanel);
		infoPanel.setLayout(null);
		
		timeLeftArea = new JTextArea();
		timeLeftArea.setBackground(UIManager.getColor("Button.background"));
		timeLeftArea.setEditable(false);
		timeLeftArea.setBounds(10, 21, 286, 22);
		timeLeftArea.setText(" ");
		infoPanel.add(timeLeftArea);

		JScrollPane chatPane = new JScrollPane();
		chatPane.setBounds(1084, 11, 300, 615);
		//chatPane.setBounds(frameSize.width-(frameSize.width/4), 11, (frameSize.width/4)-11, frameSize.height-11);
		chatPane.setBorder(new TitledBorder(null, "Chat", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(chatPane);

		chatLog = new JTextArea();
		chatLog.setBackground(SystemColor.info);
		chatLog.setLineWrap(true);
		chatLog.setEditable(false);
		caret = (DefaultCaret) chatLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatPane.setViewportView(chatLog);

		messageField = new JTextField();
		messageField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String message = messageField.getText();
					sendMessage(message);
				}
			}
		});
		messageField.setBounds(1084, 629, 300, 20);
		contentPane.add(messageField);
		messageField.setColumns(10);

		canvas = new RenderCanvas();
		canvas.setBounds(20, 18, 720, 630);
		contentPane.add(canvas);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				String disconnect = "/dc/" + pipe.getId() + "/e/";
				pipe.send(disconnect.getBytes() );
				close();
			}
		});

		setVisible(true);
		canvas.setImage("img/testwall.png", "img/bigfridge.png");
		render();
	}

	private void render() {
		for(Component c : contentPane.getComponents() ){
			c.repaint();
		}
	}
	
	public void updateTime(String time){
		timeLeftArea.setText(time);
		String[] splitted = time.split(" ");
		int minutes = Integer.parseInt(splitted[2]); // The minutes left 
		System.out.println(minutes);
		if(minutes > 3){timeLeftArea.setBackground(Color.green);}
		else if(minutes > 1){timeLeftArea.setBackground(Color.yellow);}
		else if(minutes < 1){timeLeftArea.setBackground(Color.red);}
	}
}
