package srv.domain;
 
/**
 * @author AJ Pritchard (For Now)
 * 
 * This here is the ol' contact class
 * 
 * It is one of two things, we need to figure out if it is the controller for the contact page, 
 * if so it needs to get moved to a controllers folder.
 * I am pretty sure however that this is a domain object for our pets that control their contact information.
 *
 */
public class Contact {

	
	private String phone_number;
	private String name;
	private String email;
	// private BoardMember connection; TODO
	
	public Contact(String name, String phone_number, String email) { // TODO import connection
		this.name = name;
		this.phone_number = phone_number;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhoneNumber() {
		return phone_number;
	}
	
	public String getName() {
		return name;
	}
}
