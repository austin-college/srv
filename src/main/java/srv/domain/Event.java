package srv.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Segun Adekunle 
 *
 *         This class should hold various events. Each event should be held by
 *         its pet and should also hold its pet.
 *         
 *         Importantly Event also holds and controls a list of servants
 */
public class Event {
	
	
	//private String title; //I'm not sure each event will have title
	private String addr;
	private Contact contact;
	private String date;
	private EventType type;
	private boolean continous;
	private int volunteersNumber;
	private ServiceClient eventPet;
	private ArrayList<User> servantList;
	
	
	public Event() {
		super();
	
	}
	
	public Event(String date, ServiceClient eventPet, EventType type) {
		super();
		this.date = date;
		this.eventPet = eventPet;
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
		return eventPet;
	}
	public void setEventPet(ServiceClient eventPet) {
		this.eventPet = eventPet;
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
