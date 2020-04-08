package srv.domain;

/**
 * @author AJ Pritchard (For Now)
 *
 *         The pet class is supposed to represent the information we are holding
 *         for a pet, or a service organization
 */
public class Pet {

	private Contact petContact;
	//private List<Event> events //TODO this pet should also hold onto its own events
	
	
	public Pet(String name, String email, String phone_number) {
		petContact = new Contact(name, phone_number, email);
	}
	
	public String getEmail() {
		return petContact.getEmail();
	}
	
	public String getPhoneNumber() {
		return petContact.getPhoneNumber();
	}
	
	public String getName() {
		return petContact.getName();
	}
}
