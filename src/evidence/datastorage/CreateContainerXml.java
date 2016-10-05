package evidence.datastorage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.javafx.collections.MappingChange.Map;

import evidence.gameworld.actions.Unlock;
import evidence.gameworld.items.Container;
import evidence.gameworld.items.Evidence;
import evidence.gameworld.items.Item;
import evidence.gameworld.items.Key;

/**
 * Creates an XML file for a single container, the main method is passed the name of the
 * Container eg Safe,Box,Chest... and the appropriate method is chosen as to what file to create.
 * A separate XML file is made for each differn't container.
 *
 * @author Connor
 *
 */
public class CreateContainerXml {

	public static void main(String[] args){

		List<String> actions = new ArrayList<String>();
		actions.add("Enter");
		actions.add("Unlock");
		actions.add("Lock");
		List<String> images = new ArrayList<String>();
		images.add("hammer.png");
		Container safe = new Container("safe", "Metal Safe",actions,images, false, 3);
		safe.setXPos(20);
		safe.setYPos(30);
		safe.setCurrentImage("hammer.png");

		Key hammer = new Key("hammer","a hammer",actions,images,6,42);


		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(hammer.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			File file = new File("SavedGamed.xml");
			//formats and writes to the file
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//prints to file
			jaxbMarshaller.marshal(hammer,file);
			//prints to the console
			jaxbMarshaller.marshal(hammer, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
}
