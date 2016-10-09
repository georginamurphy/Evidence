/**
 *
 */
package evidence.gameworld.actions;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

import evidence.gameworld.Player;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.MovableItem;

/**
 * 
 * 
 * @author Georgina Murphy
 *
 */
@XmlRootElement
public class CutUp extends Action {

	public CutUp() {
		super("Cut Up", "Cut this item up");
	}

	@Override
	public String apply(Item gameItem, MovableItem inventoryItem, Player player) {
		if (gameItem == null) {
			return "Need an item from the game";
		}
		if (inventoryItem == null) {
			return "Need an item from the inventory";
		}
		String feedback = "";
		if (gameItem.toString().equals("Body")) {
			if (inventoryItem.toString().equals("Saw") || inventoryItem.toString().equals("Knife")) {
				player.getWall().addItem(makeBodyPart("img/barm.png", gameItem.getXPos() * 2, gameItem.getYPos(), 1));
				player.getWall().addItem(makeBodyPart("img/bhead.png", gameItem.getXPos(), gameItem.getYPos(), 4));
				player.getWall()
						.addItem(makeBodyPart("img/arm.png", (gameItem.getXPos() * 2), gameItem.getYPos() + 50, 1));
				player.getWall().removeItem(gameItem);

				feedback = gameItem.toString() + " was " + this.toString();
			} else
				feedback = "Cannot perform " + this.toString() + " using " + inventoryItem.toString();
		} else {
			feedback = "Cannot perform " + this.toString() + " on " + gameItem.toString();
		}

		return feedback;
	}

	public MovableItem makeBodyPart(String image, int x, int y, int size) {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add("Pickup");
		MovableItem item = new MovableItem("Body Part", "This is a body part", actions, size);
		item.setCurrentImage(image);
		item.setXPos(x);
		item.setYPos(y);
		return item;
	}

}
