package srv.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

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

	private int eid;
	private String title; // I'm not sure each event will have title
	private String address; // address of location
	private int contactId; // contact info
	private String date; // date of event
	private String type; // eventType TODO
	private boolean continous;// if event repeats??
	private int volunteersNeeded; // 3 of volunteers needed
	private int serviceClientId; // pet associated with event
	private int participantsListId;

	public Event() {
		super();

	}

	public Event(String date, int serviceClientId, String type) {
		super();
		this.date = date;
		this.serviceClientId = serviceClientId;
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public Event setAddress(String address) {
		this.address = address;
		return this;
	}

	public int getContactId() {
		return contactId;
	}

	public Event setContactId(int contactId) {
		this.contactId = contactId;
		return this;

	}

	public String getDate() {
		return date;
	}

	public Event setDate(String date) {
		this.date = date;
		return this;

	}

	public int getServiceClientId() {
		return serviceClientId;
	}

	public Event setSetServiceClientId(int serviceClientId) {
		this.serviceClientId = serviceClientId;
		return this;

	}

	public String getType() {
		return type;
	}

	public Event setType(String type) {
		this.type = type;
		return this;

	}

	public int getParticipantsListId() {
		return participantsListId;
	}

	public Event setParticipantsListId(int participantsListId) {
		this.participantsListId = participantsListId;
		return this;

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

	public boolean isContinous() {
		return continous;
	}

	public Event setContinuous(boolean continous) {
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

}
