package srv.domain.event;

import java.util.ArrayList;
import java.util.List;

import srv.domain.contact.Contact;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.user.User;

/**
 * @author Segun Adekunle
 *
 *         This class should hold various events. Each event should be held by
 *         its pet and should also hold its pet.
 * 
 *         Importantly Event also holds and controls a list of servants
 */
public class Event {

	// private String title; //I'm not sure each event will have title
	private String addr; //address of location
	private Contact contact; // contact info
	private String date; //date of event
	private EventType type; // event type
	private boolean continous;// if event repeats??
	private int volunteersNumber; // 3 of volunteers needed
	private ServiceClient pet; // pet associated with event
	private ArrayList<User> servantList; //list of servants signed up for the event

	public Event() {
		super();

	}

	public Event(String date, ServiceClient eventPet, EventType type) {
		super();
		this.date = date;
		this.pet = eventPet;
		this.type = type;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ServiceClient getEventPet() {
		return pet;
	}

	public void setEventPet(ServiceClient eventPet) {
		this.pet = eventPet;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public ArrayList<User> getServantList() {
		return servantList;
	}

	public void setServantList(ArrayList<User> servantList) {
		this.servantList = servantList;
	}

}
