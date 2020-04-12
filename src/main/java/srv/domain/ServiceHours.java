package srv.domain;

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

	private Pet servedPet; // Associated Pet
	private User servant; // Servant worked for Pet
	private Event eventName; // Event held for service
	private double hours;	// Hours served

	
	/**
	 * Constructor for ServiceHours
	 * @param servedPet
	 * @param servant
	 * @param event
	 * @param hours
	 */
	public ServiceHours(Pet servedPet, User servant, Event eventName, double hours) {
		super();
		this.servedPet = servedPet;
		this.servant = servant;
		this.eventName = eventName;
		this.hours = hours;
	}


	public Pet getServedPet() {
		return servedPet;
	}


	public void setServedPet(Pet servedPet) {
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
	
	
}
