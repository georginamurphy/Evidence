package evidence.clientserver.infoholders;

import java.io.Serializable;
import java.util.List;

import evidence.gameworld.Wall;
import evidence.gameworld.items.Item;

/**
 * A RenderPackage is a class that holds the objects a client needs to render 
 * the game view to the user.  Currently, a RenderPackage hold a wall, and a list
 * of items.  The wall represents the Wall in the game the user is currently looking at,
 * and the list of items represents the player's inventory.
 * 
 * These are put into a RenderPackage to make serialization of all render components easier,
 * and easily accessible through one instance of a RenderPackage.
 * 
 * @author Tyler Jones
 *
 */
public class RenderPackage implements Serializable{
	private static final long serialVersionUID = -2017422499501784164L;
	
	// The wall the player is facing
	private Wall front, back;
	
	// The player's inventory of items
	private List<Item> inventory;
	
	// String containing feedback for the event that just happened
	String feedback;
	
	/**
	 * A constructor for a RenderPackage
	 * 
	 * @param frontWall - The wall to render
	 * @param inventory - The inventory to render
	 */
	public RenderPackage(Wall frontWall, Wall backWall, List<Item> inventory, String feedback){
		this.front = frontWall;
		this.back = backWall;
		this.inventory = inventory;
		this.feedback = feedback;
	}
	
	/**
	 * Getter for the wall field
	 * 
	 * @return - The wall to render
	 */
	public Wall getFrontWall(){
		return this.front;
	}
	
	/**
	 * Getter for the wall field
	 * 
	 * @return - The wall to render
	 */
	public Wall getBackWall(){
		return this.back;
	}
	
	/**
	 * Getter for the inventory field
	 * 
	 * @return - The inventory to render
	 */
	public List<Item> getInventory(){
		return this.inventory;
	}
	
	public String getFeedback(){
		return this.feedback;
	}
}
