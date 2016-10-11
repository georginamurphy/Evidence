package evidence.gameworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import evidence.gameworld.Wall.Direction;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.MovableItem;

/**
 * Player Class, Responsible for having a direction, Unique Id for server, feed back string to print to the screen.
 * 
 * @author Georgina Murphy
 *
 */
@XmlRootElement
public class Player {

	private ArrayList<Item> inventory = new ArrayList<Item>(8);
	private Room currentRoom;
	private Direction currentDirection;
	private Integer ID; // Used to identify clients with a player object
	private String feedback = "";
	private boolean bloody = false;
	private boolean inGame = true;

	public void makeBloody() {
		for (Item item : inventory) {
			if (item.toString().equals("Gloves")) {
				this.bloody = false;
			}
		}
			
		this.bloody = true;
	}

	public boolean getBloody() {
		return bloody;
	}

	public void setCurrentRoom(Room room) {
		this.currentRoom = room;
	}

	public void setCurrentDirection(Direction direction) {
		this.currentDirection = direction;
	}

	public void setInventory(ArrayList<Item> items) {
		inventory = items;
	}

	public void setID(Integer ID) {
		this.ID = ID;
	}

	@XmlElement
	public Integer getID() {
		return this.ID;
	}

	@XmlElement
	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	/**
	 * Getter for this players current wall
	 * 
	 * @return this players current wall
	 */
	public Wall getWall() {
		Wall wall = null;
		if (inGame) {
			for (Wall w : currentRoom.getWalls()) {
				if (w.getDirection().equals(currentDirection)) {
					wall = w;
				}
			}
		}else{
			wall = new Wall();
			wall.setImageName("obj/outside.png");
			wall.setX(0);
			wall.setY(0);
		}
		
		return wall;
	}

	@XmlElement
	public Room getCurrentRoom() {
		return this.currentRoom;
	}

	/**
	 * Add the provided item to this players inventory
	 * 
	 * @param item
	 *            - the item to add to the inventory
	 */
	public void addItem(Item item) {
		inventory.add(item);
	}

	/**
	 * Remove the provided item from this players inventory
	 * 
	 * @param item
	 *            - the item to remove from the inventory
	 */
	public void removeItem(Item item) {
		inventory.remove(item);
	}

	/**
	 * A method to rotate the view of this current player by changing the wall
	 * they are looking at
	 * 
	 * @param direction
	 *            - L for left or R for right
	 */
	public String rotateView(String direction) {
		switch (currentDirection) {
		case NORTH:
			if (direction.equals("L")) {
				currentDirection = Direction.WEST;
				break;
			} else {
				currentDirection = Direction.EAST;
				break;
			}
		case EAST:
			if (direction.equals("L")) {
				currentDirection = Direction.NORTH;
				break;
			} else {
				currentDirection = Direction.SOUTH;
				break;
			}
		case SOUTH:
			if (direction.equals("L")) {
				currentDirection = Direction.EAST;
				break;
			} else {
				currentDirection = Direction.WEST;
				break;
			}
		case WEST:
			if (direction.equals("L")) {
				currentDirection = Direction.SOUTH;
				break;
			} else {
				currentDirection = Direction.NORTH;
				break;
			}
		}

		return "You are now facing the " + currentDirection.toString() + " wall in the " + currentRoom.toString();
	}

	@XmlElement
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	@XmlElement
	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void setBloodie(boolean b) {
		this.bloody = b;

	}
	
	public void removeFromGame(){
		inGame = false;
	}
}
