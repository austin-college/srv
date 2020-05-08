package srv.domain.hours;

import java.io.Serializable;

import srv.domain.event.Event;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.User;

/**
 * @author Min Shim
 *
 *	This class is a data holder for Service hours info. 
 *	ServiceHours will include basic informations such as:
 *	Associated service client, Servant(or Board Member), Event, and hours served.
 */
public class ServiceHours implements Serializable{

	private Integer shid; // Unique id for each hour, given by database
	private ServiceClient servedSc; // Associated service client
	private User servant; // Servant worked for service client
	private Event event; // Event held for service
	private Double hours;	// Hours served
	private String date; // Date Served
	private String reflection; // Thoughts on the event
	private String description; // Description of the event
	private String status; // Tells if the hours are pending, approved or rejected.
	
	/**
	 * Constructor for ServiceHours
	 * @param id
	 * @param servedPet
	 * @param servant
	 * @param event
	 * @param hours
	 * @param reflection
	 * @param description
	 */
	public ServiceHours(Integer id, ServiceClient servedPet, User servant, Event eventName, Double hours, 
			String stat) {
		super();
		this.shid = id;
		this.servedSc = servedPet;
		this.servant = servant;
		this.event = eventName;
		this.hours = hours;
		this.status = stat;
	}
	
	/**
	 * Constructor for ServiceHours
	 * This constructor includes a reflection, date and description of the event
	 * <p> 
	 * we have two constructors as of now, to avoid any conflicting problems with me adding
	 * these three items into the class.
	 * @param servedPet
	 * @param servant
	 * @param event
	 * @param hours
	 * @param reflection
	 * @param description
	 */
	public ServiceHours(ServiceClient servedPet, User servant, Event eventName, double hours, String reflection, String description, String stat) {
		super();
		this.servedSc = servedPet;
		this.servant = servant;
		this.event = eventName;
		this.hours = hours;
		this.date = eventName.getDate();
		this.reflection = reflection;
		this.description = description;
		this.status = stat; 
	}

	public ServiceHours() {
		super();
	}

	public Integer getShid() {
		return this.shid;
	}
	
	public ServiceHours setShid(Integer id) {
		this.shid = id;
		return this;
		
	}
	public ServiceClient getServedPet() {
		return servedSc;
	}


	public ServiceHours setServedPet(ServiceClient servedPet) {
		this.servedSc = servedPet;
		return this;
	}


	public User getServant() {
		return servant;
	}


	public ServiceHours setServant(User servant) {
		this.servant = servant;
		return this;
	}


	public Event getEvent() {
		return event;
	}


	public ServiceHours setEvent(Event eventName) {
		this.event = eventName;
		return this;
		
	}


	public double getHours() {
		return hours;
	}


	public ServiceHours setHours(double hours) {
		this.hours = hours;
		return this;
	}


	public String getDate() {
		return date;
	}


	public ServiceHours setDate(String date) {
		this.date = date;
		return this; 
	}


	public String getReflection() {
		return reflection;
	}


	public ServiceHours setReflection(String reflection) {
		this.reflection = reflection;
		return this;
	}


	public String getDescription() {
		return description;
	}


	public ServiceHours setDescription(String description) {
		
		this.description = description;
		return this;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public ServiceHours setStatus(String stat) {
		this.status = stat;
		return this;
	}

	
	
}
