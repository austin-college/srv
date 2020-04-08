package srv.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AJ Pritchard
 *
 *         This class should hold various events. Each event should be held by
 *         its pet and should also hold its pet.
 *         
 *         Importantly Event also holds and controls a list of servants
 */
public class Event {

	private String name;
	private String description;
	private String time; // TODO Make this the appropriate object
	private String date; // TODO Make this the appropriate object
	private List<User> servants; // TODO Holds onto its servants
	private Pet organziation;
// private EventType type; //TODO Add this in after EventType has been created

	public Event(String name, String description, String time, String date, Pet organization) {
		this.name = name;
		this.description = description;
		this.time = time;
		this.date = date;
		this.organziation = organization;

		servants = new ArrayList<User>();
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getDate() {
		return date;
	}
	
	public Pet getOrganization() {
		return organziation;
	}
	
	public List<User> getServants(){
		return servants;
	}
	
	public void addServant(User servant) {
		servants.add(servant);
	}
}
