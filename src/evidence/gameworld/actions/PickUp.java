package evidence.gameworld.actions;

import evidence.gameworld.items.Item;

/**
 * Pick up action
 * allows player to pickup up an item and put it in their inventory
 * 
 * @author Georgina Murphy
 */
public class PickUp extends Action {
	
	public PickUp(){
		setName("Pick up");
		setDescription("Pick up this item and put it in your inventory");
	}
	
	/**
	 * Method to apply the pickup action to the provided item
	 * 
	 * @param item - the item the action is being applied to
	 * @return item - a new item with an updated state
	 */
	@Override
	public Item apply(Item item) {
		// TODO Auto-generated method stub
		return null;
	}
}
