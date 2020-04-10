package srv.domain;
 
/**
 * @author Emma Driscoll
 * 
 * This here is the ol' contact class
 * 
 * This class is a data holder for contact info. 
 * Info includes pet(association), firstName, lastName, email, phoneNum(ber), and address
 *
 */
public class Contact {
	
	private String pet;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNum;
	private String address;
	
	public String getPet() {
		return pet;
	}
	public void setPet(String pet) {
		this.pet = pet;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	

}
