package srv.domain.user;
import java.util.ArrayList;

import srv.domain.event.Event;
import srv.domain.serviceClient.ServiceClient;
/**
 * An instance of this class represents the Board Member User base.
 * These members will have more power than your average user, and be able to manage
 * a list of Clients(pets) that are given to them.
 * @author hunter
 *
 */
public class BoardMemberUser extends ServantUser {
	
	private ArrayList<ServiceClient> managedGroups;

	public BoardMemberUser(String userID, String password, char classification) {
		super(userID, password, classification);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * On call, this method takes a ServiceClient and allows the BoardMember to edit
	 * its contents
	 * <p>
	 * this will eventually show a prompt to the board member or it will take them to another page
	 * that will allow them to do the editing
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param edditedPet
	 * @return
	 */
	public ServiceClient editPet(ServiceClient edditedPet) {
		return edditedPet;
	}
	
	/**
	 * On call, this method takes an Event and adds its content to a list somewhere
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param addedEvent
	 * @return
	 */
	public Event addEvent(Event addedEvent) {
		return addedEvent;
	}
	
	/**
	 * On call, this method takes an Event and removes its content from a list somewhere
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param addedEvent
	 * @return
	 */
	public Event removeEvent(Event removedEvent) {
		return removedEvent;
	}
	
	/**
	 * On call, this method takes an Event and allows the board
	 * member to edit its contents. This will either show a pop-up window for the board member
	 * or take them to another page where they can edit the Event
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param addedEvent
	 * @return
	 */
	public Event editEvent(Event edditedEvent) {
		return edditedEvent;
	}
	
	/**
	 * On call this method will either display a pop-up or take the board member to another page.
	 * this method will allow the Board Member to write a summary for an Event
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @return
	 */
	public String writeSummary(Event writtenEven) {
		return null;
	}
	
	

}
