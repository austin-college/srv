package srv.domain;

import srv.domain.event.Event;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.user.User;

/**
 * @author Min Shim
 *
 *	This class is a data holder for Service hours info. 
 *	ServiceHours will include basic informations such as:
 *	Associated Pet, Servant(or Board Member), Event, and hours served.
 *
 *	Initially this class is keeping track of single event, 
 *	but later it can be turned into a class tracking of all service opportunities (I need to hear more opinions about this) 
 */
public class ServiceHours {

	private ServiceClient servedPet; // Associated Pet
	private User servant; // Servant worked for Pet
	private Event eventName; // Event held for service
	private double hours;	// Hours served
	private String date; // Date Served
	private String reflection; // Thoughts on the event
	private String descritpion; // Description of the event

	
	/**
	 * Constructor for ServiceHours
	 * @param servedPet
	 * @param servant
	 * @param event
	 * @param hours
	 * @param reflection
	 * @param description
	 */
	public ServiceHours(ServiceClient servedPet, User servant, Event eventName, double hours, String reflection, String description) {
		super();
		this.servedPet = servedPet;
		this.servant = servant;
		this.eventName = eventName;
		this.hours = hours;
		this.date = eventName.getDate();
		this.reflection = reflection;
		this.descritpion = description;
	}


	public ServiceClient getServedPet() {
		return servedPet;
	}


	public void setServedPet(ServiceClient servedPet) {
		this.servedPet = servedPet;
	}


	public User getServant() {
		return servant;
	}


	public void setServant(User servant) {
		this.servant = servant;
	}


	public Event getEventName() {
		return eventName;
	}


	public void setEventName(Event eventName) {
		this.eventName = eventName;
	}


	public double getHours() {
		return hours;
	}


	public void setHours(double hours) {
		this.hours = hours;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getReflection() {
		return reflection;
	}


	public void setReflection(String reflection) {
		this.reflection = reflection;
	}


	public String getDescritpion() {
		return descritpion;
	}


	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
	
	
}
