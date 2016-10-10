package evidence.clientserver.infoholders;

import java.io.Serializable;
import java.util.List;

import evidence.gameworld.Wall;
import evidence.gameworld.items.Item;

/**
 * A RenderPackage is a class that holds the objects a client needs to render 
 * the game view to the user.  Currently, a RenderPackage holds two walls, a list
 * of items and a String.  The wall represents the Wall in the game the user is currently looking at,
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
	
	// String containing feedback for the last event that occured for the player this RenderPackage
	// is being sent to.
	String feedback;
	
	/**
	 * A constructor for a RenderPackage
	 * 
	 * @param frontWall - The frontWall to render
	 * @param backWall - The backWall to render
	 * @param inventory - The inventory to render
	 * @param feedback - The feedbackString to render
	 */
	public RenderPackage(Wall frontWall, Wall backWall, List<Item> inventory, String feedback){
		this.front = frontWall;
		this.back = backWall;
		this.inventory = inventory;
		this.feedback = feedback;
	}
	
	/**
	 * Getter for the frontWall
	 * 
	 * @return - The backWall to render
	 */
	public Wall getFrontWall(){
		return this.front;
	}
	
	/**
	 * Getter for the wall field
	 * 
	 * @return - The frontWall to render
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
	
	/**
	 * Getter for the feedback field
	 * 
	 * @return - The feedback String to render
	 */
	public String getFeedback(){
		return this.feedback;
	}
}
