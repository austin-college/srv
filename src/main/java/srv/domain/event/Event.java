package srv.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import srv.domain.contact.Contact;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.user.ServantUser;
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

	public void setEid(int eid) {
		this.eid = eid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public boolean isContinous() {
		return continous;
	}

	public void setContinous(boolean continous) {
		this.continous = continous;
	}

	public int getVolunteersNeeded() {
		return volunteersNeeded;
	}

	public void setVolunteersNeeded(int volunteersNeeded) {
		this.volunteersNeeded = volunteersNeeded;
	}

	public ServiceClient getServiceClient() {
		return serviceClient;
	}

	public void setServiceClient(ServiceClient serviceClient) {
		this.serviceClient = serviceClient;
	}

	public ArrayList<ServantUser> getParticipantsList() {
		return participantsList;
	}

	public void setParticipantsList(ArrayList<ServantUser> participantsList) {
		this.participantsList = participantsList;
	}


}
