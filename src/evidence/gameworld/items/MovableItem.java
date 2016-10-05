package evidence.gameworld.items;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Class for a movable item.
 * A movable item is one that can be picked up and put into a players inventory or a container
 * @author Georgina Murphy
 *
 */
@XmlRootElement
public class MovableItem extends Item {
	private int size;
	
	public MovableItem(String name, String description, List<String> actions, List<String> images, int size) {
		super(name, description, actions, images);
		this.size = size;
	}
	public MovableItem(){
		
	}

	@XmlElement
	public int getSize() {
		return size;
	}
	
	public void setSize(int s){
		this.size = s;
	}

}
