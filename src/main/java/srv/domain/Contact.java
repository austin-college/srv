package srv.domain;
 
/**
 * @author AJ Pritchard (For Now)
 * 
 * This here is the ol' contact class
 * 
 * This class is a data holder for contact info. 
 *
 */
public class Contact {

	
	private String phone_number;
	private String email;
	// private BoardMember connection; TODO
	
	public Contact(String phone_number, String email) { // TODO import connection
		this.phone_number = phone_number;
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPhoneNumber() {
		return phone_number;
	}
}
