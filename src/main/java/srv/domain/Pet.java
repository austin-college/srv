package srv.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AJ Pritchard (For Now)
 *
 *         The pet class is supposed to represent the information we are holding
 *         for a pet, or a service organization
 */
public class Pet {

	private String name;
	private String description;
	private Contact petContact;
	private List<Event> events; // TODO this pet should also hold onto its own events

	public Pet(String name, String description, String email, String phone_number) {
		petContact = new Contact(phone_number, email);
		this.name = name;
		this.description = description;

		events = new ArrayList<Event>();
	}

	public String getEmail() {
		return petContact.getEmail();
	}

	public String getPhoneNumber() {
		return petContact.getPhoneNumber();
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void addEvent(Event newEvent) {
		events.add(newEvent);
	}
}
