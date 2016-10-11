package evidence.gameworld.actions;

import javax.xml.bind.annotation.XmlRootElement;

import evidence.gameworld.Player;
import evidence.gameworld.items.Door;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.MovableItem;

/**
 * Enter action Allows players to go through a door into the next room
 *
 * @author Georgina Murphy
 */
@XmlRootElement
public class Enter extends Action {
	private static final long serialVersionUID = -4219760638031297711L;

	public Enter() {
		super("Enter", "Go into this item");
	}

	/**
	 * Method to apply the enter action to the provided item
	 *
	 * @param item
	 *            - the item the action is being applied to
	 * @return string - updated state
	 */
	@Override
	public String apply(Item gameItem, MovableItem inventoryItem, Player player) {
		if (gameItem == null) {
			return "Need an item from the game";
		}
		String feedback = "";
		if (gameItem instanceof Door) {
			Door door = (Door) gameItem;
			if(player.getBloody() && !door.getBloody()){
				door.makeBloody();
			}
			player.setCurrentRoom(door.getRoom());
			feedback = "You are now in the " + player.getCurrentRoom().toString();
		} else {
			feedback = "Cannot perform " + this.toString() + " on " + gameItem.toString();
		}
		return feedback;
	}

	public String toString() {
		return super.getName();
	}

}
