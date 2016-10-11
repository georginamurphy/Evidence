package evidence.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import evidence.clientserver.infoholders.EventPackage;
import evidence.gameworld.items.Item;

/**
 * A listener for the PopUp Menu that appears when the user
 * clicks on an item in the render view
 *
 * @author Callum Crosby, Tyler Jones
 */
class PopupListener implements ActionListener {

	// The item in the game window the user has clicked on to create this PopupActionListener
	private Item item;

	/**
	 * A constructor for a PopupActionListener
	 *
	 * @param item - The item a user clicked on to instantiate the PopupActionListener, eg
	 *					the item in the renderCanvas
	 */
	public PopupListener(Item item){
		this.item = item;
	}

	/**
	 * Called when the user selects one of the options from the PopupMenu this listener
	 * is attached to.  Will create an Event object for the action the user wishes to peform
	 * and send it to the server via the ClientPipe.
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		EventPackage event = new EventPackage(ClientWindow.getSelected(), item, actionEvent.getActionCommand(), ClientWindow.getID());
		ClientWindow.sendEvent(event);
		ClientWindow.currentlySelected = null;
	}
}