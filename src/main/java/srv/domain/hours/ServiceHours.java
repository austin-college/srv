package srv.domain.hours;

import java.io.Serializable;
import java.util.Date;

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
	
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_PENDING = "Pending";
	

	private Integer shid; // Unique id for each hour, given by database
	private ServiceClient servedSc; // Associated service client
	private User servant; // Servant worked for service client
	private Event event; // Event held for service
	private Double hours;	// Hours served
	private Date date; // Date Served
	private String reflection; // Thoughts on the event
	private String description; // Description of the event
	private String status; // Tells if the hours are pending, approved or rejected.
	private String feedback; // provide the user feedback on approved/rejected hours
	private String contactName; // name of contact to validate service
	private String contactContact; // may contain phone or email or both
	
	/**
	 * Constructor for ServiceHours
	 * @param id
	 * @param servedPet
	 * @param servant
	 * @param event
	 * @param hours
	 * @param reflection
	 * @param description
	 * @param contactName
	 * @param contactContact
	 */
	public ServiceHours(Integer id, ServiceClient servedPet, User servant, Event eventName, Double hours, 
			String stat, String feedback, String contactName, String contactContact) {
		super();
		this.shid = id;
		this.servedSc = servedPet;
		this.servant = servant;
		this.event = eventName;
		this.hours = hours;
		this.status = stat;
		this.feedback = feedback;
		this.contactName = contactName;
		this.contactContact = contactContact;
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
	 * @param contactName
	 * @param contactContact
	 */
	public ServiceHours(ServiceClient servedPet, User servant, Event eventName, Double hours, String reflection, String description, String stat, 
			String feedback, String contactName, String contactContact) {
		super();
		this.servedSc = servedPet;
		this.servant = servant;
		this.event = eventName;
		this.hours = hours;
		this.date = eventName.getDate();
		this.reflection = reflection;
		this.description = description;
		this.status = stat;
		this.feedback = feedback;
		this.contactName = contactName;
		this.contactContact = contactContact;
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


	public Double getHours() {
		return hours;
	}


	public ServiceHours setHours(Double hours) {
		this.hours = hours;
		return this;
	}


	public Date getDate() {
		return date;
	}


	public ServiceHours setDate(Date date) {
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
	
	public String getFeedback() {
		return this.feedback;
	}
	
	public ServiceHours setFeedback(String feedback) {
		this.feedback = feedback;
		return this;
	}
	
	public String getContactName() {
		return this.contactName;
	}
	
	public ServiceHours setContactName(String newContactName) {
		this.contactName = newContactName;
		return this;
	}
	
	public String getContactContact() {
		return this.contactContact;
	}
	
	public ServiceHours setContactContact(String newContactContact) {
		this.contactContact = newContactContact;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((feedback == null) ? 0 : feedback.hashCode());
		result = prime * result + ((hours == null) ? 0 : hours.hashCode());
		result = prime * result + ((reflection == null) ? 0 : reflection.hashCode());
		result = prime * result + ((servant == null) ? 0 : servant.hashCode());
		result = prime * result + ((servedSc == null) ? 0 : servedSc.hashCode());
		result = prime * result + ((shid == null) ? 0 : shid.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		result = prime * result + ((contactContact == null) ? 0 : contactContact.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceHours other = (ServiceHours) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (feedback == null) {
			if (other.feedback != null)
				return false;
		} else if (!feedback.equals(other.feedback))
			return false;
		if (hours == null) {
			if (other.hours != null)
				return false;
		} else if (!hours.equals(other.hours))
			return false;
		if (reflection == null) {
			if (other.reflection != null)
				return false;
		} else if (!reflection.equals(other.reflection))
			return false;
		if (servant == null) {
			if (other.servant != null)
				return false;
		} else if (!servant.equals(other.servant))
			return false;
		if (servedSc == null) {
			if (other.servedSc != null)
				return false;
		} else if (!servedSc.equals(other.servedSc))
			return false;
		if (shid == null) {
			if (other.shid != null)
				return false;
		} else if (!shid.equals(other.shid))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		if (contactContact == null) {
			if (other.contactContact != null)
				return false;
		} else if (!contactContact.equals(other.contactContact))
			return false;
		return true;
	}
	
	

	
	
}
