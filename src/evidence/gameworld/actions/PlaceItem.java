package evidence.gameworld.actions;

import javax.xml.bind.annotation.XmlRootElement;

import evidence.gameworld.Player;
import evidence.gameworld.items.Container;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.MovableItem;

@XmlRootElement
public class PlaceItem extends Action {

	public PlaceItem(){
		super("Place Item", "Place an item from your inventroy into a container");
	}

	@Override
	public String apply(Item gameItem, MovableItem inventoryItem, Player player) {
		if(gameItem == null ){
			return "Need an item from the game";
		}
		if(inventoryItem == null){
			return "Need an item from the inventory";
		}
		String feedback = "";
		if(gameItem instanceof Container){
			Container container = (Container)gameItem;
			if(container.getBloodie()){
				inventoryItem.makeBloodie();
			}
			feedback = container.putItem(inventoryItem, player);
			if(inventoryItem.getBloodie()){
				container.makeBloodie();
			}
		}else
			feedback = "Cannot perform " + this.toString() + " on " + gameItem.toString();

	
		return feedback;
	}

}
