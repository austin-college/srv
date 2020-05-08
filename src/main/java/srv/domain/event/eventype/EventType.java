package srv.domain.event.eventype;

import java.io.Serializable;

import srv.domain.serviceclient.ServiceClient;
import srv.domain.servicegroup.ServiceGroup;

/**
 * An instance of this class is a data holder, mainly for sorting purposes for events. It represents
 * whether an event is an Austin College event such as Great Day of Service (GSD) or tutoring through
 * the Service Station.
 * 
 * 
 * @author Lydia House
 *
 */
public class EventType implements Serializable {
	
	private Integer etId; // unique id for each event type
	private String name; // name of the event type
	private String description; // description of event type
	private Integer defHours; // use to populate the hours served as user  
	private boolean pinHours; // true -> user must report using the defHours value
	private ServiceClient defClient; //  identifies the default ServiceClient
	private ServiceGroup defOrg; // identifies the default ServiceGroup affiliation
	
	public EventType() {
		super();
	}
	
	public EventType(Integer etid, String name, String description, int defHours, boolean pinHours, ServiceClient sc, ServiceGroup sg) {
		this.etId = etid;
		this.name = name;
		this.description = description;
		this.defHours = defHours;
		this.pinHours = pinHours;
		this.defClient = sc;
		this.defOrg = sg;
	}
	
	public Integer getEtid() {
		return etId;
	}

	public EventType setEtid(Integer etid) {
		this.etId = etid;
		return this;
	}

	public String getName() {
		return name;
	}

	public EventType setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EventType setDescription(String description) {
		this.description = description;
		return this;
	}

	public Integer getDefHours() {
		return defHours;
	}

	public EventType setDefHours(Integer defHours) {
		this.defHours = defHours;
		return this;
	}

	public boolean isPinHours() {
		return pinHours;
	}

	public EventType setPinHours(boolean pinHours) {
		this.pinHours = pinHours;
		return this;
	}

	public ServiceClient getDefClient() {
		return defClient;
	}

	public EventType setDefClient(ServiceClient defClient) {
		this.defClient = defClient;
		return this;
	}

	public ServiceGroup getDefOrg() {
		return defOrg;
	}

	public EventType setDefOrg(ServiceGroup defOrg) {
		this.defOrg = defOrg;
		return this;
	}
}
