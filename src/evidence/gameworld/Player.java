package evidence.gameworld;

import java.util.ArrayList;
import java.util.List;

import evidence.gameworld.Wall.Direction;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.MovableItem;

/**
 * Player Class
 * 
 * @author Georgina Murphy
 *
 */
public class Player {

	private ArrayList<Item> inventory = new ArrayList<Item>();
	private Room currentRoom;
	private Direction currentDirection;

	public void setRoom(Room room) {
		currentRoom = room;
	}

	public void setDirection(Direction direction) {
		currentDirection = direction;
	}

	public void setInventory(ArrayList<Item> items) {
		inventory = items;
	}

	/**
	 * Getter for this players current wall
	 * 
	 * @return this players current wall
	 */
	public Wall getWall() {
		Wall wall = null;
		for (Wall w : currentRoom.getWalls()) {
			if (wall.getDirection().equals(currentDirection)) {
				wall = w;
			}
		}
		return wall;
	}
	
	public Room getCurrentRoom(){
		return this.currentRoom;
	}
	
	/**
	 * Add the provided item to this players inventory
	 * 
	 * @param item
	 *            - the item to add to the inventory
	 */
	public void addItem(MovableItem item) {
		inventory.add(item);
	}

	/**
	 * Remove the provided item from this players inventory
	 * 
	 * @param item
	 *            - the item to remove from the inventory
	 */
	public void removeItem(MovableItem item) {
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
			if (direction.equals("L"))
				currentDirection = Direction.WEST;
			else
				currentDirection = Direction.EAST;
		case EAST:
			if (direction.equals("L"))
				currentDirection = Direction.NORTH;
			else
				currentDirection = Direction.SOUTH;
		case SOUTH:
			if (direction.equals("L"))
				currentDirection = Direction.EAST;
			else
				currentDirection = Direction.WEST;
		case WEST:
			if (direction.equals("L"))
				currentDirection = Direction.SOUTH;
			else
				currentDirection = Direction.NORTH;
		}
		
		return "You are now facing the " + currentDirection.toString() + " wall in the " + currentRoom.toString();
	}

	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
	public ArrayList<Item> getInventory(){
		return inventory;
	}
}