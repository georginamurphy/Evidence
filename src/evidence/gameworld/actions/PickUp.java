package evidence.gameworld.actions;

import evidence.gameworld.Player;
import evidence.gameworld.items.Item;

/**
 * Pick up action
 * allows player to pickup up an item and put it in their inventory
 * 
 * @author Georgina Murphy
 */
public class PickUp extends Action {
	
	public PickUp(){
		super("Pick up", "Pick up this item and put it in your inventory");
	}
	
	/**
	 * Method to apply the pickup action to the provided item
	 * 
	 * @param item - the item the action is being applied to
	 * @return string - updated state
	 */
	@Override
	public String apply(Item item, Player player) {
		// TODO Auto-generated method stub
		return "Cannot perform " + this.toString() + " on " + item.toString();
	}
}
