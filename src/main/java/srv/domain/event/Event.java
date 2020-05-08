package srv.domain.event;

import java.io.Serializable;

import srv.domain.contact.Contact;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceClient.ServiceClient;

/**
 * An instance of this class represents an event from the Service Station. The information
 * contained includes the event name, location/address, contact information, date, the event type (AC or adhoc),
 * needed volunteers (in hours and by persons), the service client associated with the event, the number
 * of hours registered for the event and a free text field that provides a description of the event.
 * 
 * 
 * @author Segun Adekunle
 *
 */
public class Event implements Serializable {

	private int eid;
	private String title; 
	private String address; // address of location
	private Contact contact; // contact info
	private String date; // date of event
	private EventType type; // eventType TODO
	private String typeString; //testing for eventType until it's implemented
	private boolean continuous;// if event repeats??
	private int volunteersNeeded; // 3 of volunteers needed
	private ServiceClient serviceClient; // service client associated with event
	private double neededVolunteerHours; // volunteer hours needed for event
	private double rsvpVolunteerHours; // volunteer hours already registered for event
	private String freeTextField; // freeform text space

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
	
	/**
	 * Temporary setter method to check for eventType in table until eventType dao is 
	 * implemented
	 * @param s
	 * @return
	 */
	public Event setTypeString(String s) {
	    this.typeString = s;
	    return this;
	}
	
	public String getTypeString() {
		return typeString;
	}

	public EventType getType() {
		return type;
	}

	public Event setType(EventType type) {
		this.type = type;
		return this;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public Event setContinous(boolean continous) {
		this.continuous = continous;
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

	public double getNeededVolunteerHours() {
		return neededVolunteerHours;
	}

	public Event setNeededVolunteerHours(double neededVolunteerHours) {
		
		this.neededVolunteerHours = neededVolunteerHours;
		return this; 
	}

	public double getRsvpVolunteerHours() {
		return rsvpVolunteerHours;
	}

	public Event setRsvpVolunteerHours(double rsvpVolunteerHours) {
		
		this.rsvpVolunteerHours = rsvpVolunteerHours;
		return this; 
	}

	public String getFreeTextField() {
		return freeTextField;
	}

	public Event setFreeTextField(String freeTextField) {
		this.freeTextField = freeTextField;
		return this; 
	}
}
