package srv.domain.event;

import java.io.Serializable;
import java.util.ArrayList;

import srv.domain.contact.Contact;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.user.ServantUser;

/**
 * @author Segun Adekunle
 *
 *         This class should hold various events. Each event should be held by
 *         its pet and should also hold its pet.
 * 
 *         Importantly Event also holds and controls a list of servants
 *         
 *         Also holds onto the EventParticipants class
 */
public class Event implements Serializable {

	private int eid;
	private String title; // I'm not sure each event will have title
	private String address; // address of location
	private Contact contact; // contact info
	private String date; // date of event
	private EventType type; // eventType TODO
	private boolean continous;// if event repeats??
	private int volunteersNeeded; // 3 of volunteers needed
	private ServiceClient serviceClient; // pet associated with event
	private ArrayList<ServantUser> participantsList;// list of all ServantUsers signed up for the event

	public Event() {
		super();
	}

	public Event(String date, ServiceClient serviceClient, EventType type) {
		super();
		this.date = date;
		this.serviceClient = serviceClient;
		this.type = type;
	}

	public int getEid() {
		return eid;
	}

	public Event setEid(int eid) {
		this.eid = eid;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Event setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Event setAddress(String address) {
		this.address = address;
		return this;
	}

	public Contact getContact() {
		return contact;
	}

	public Event setContact(Contact contact) {
		this.contact = contact;
		return this;
	}

	public String getDate() {
		return date;
	}

	public Event setDate(String date) {
		this.date = date;
		return this;
	}

	public EventType getType() {
		return type;
	}

	public Event setType(EventType type) {
		this.type = type;
		return this;
	}

	public boolean isContinous() {
		return continous;
	}

	public Event setContinous(boolean continous) {
		this.continous = continous;
		return this;
	}

	public int getVolunteersNeeded() {
		return volunteersNeeded;
	}

	public Event setVolunteersNeeded(int volunteersNeeded) {
		this.volunteersNeeded = volunteersNeeded;
		return this;
	}

	public ServiceClient getServiceClient() {
		return serviceClient;
	}

	public Event setServiceClient(ServiceClient serviceClient) {
		this.serviceClient = serviceClient;
		return this;
	}

	public ArrayList<ServantUser> getParticipantsList() {
		return participantsList;
	}

	public Event setParticipantsList(ArrayList<ServantUser> participantsList) {
		this.participantsList = participantsList;
		return this;
	}

	public class EventParticipants {
		
	}
}
