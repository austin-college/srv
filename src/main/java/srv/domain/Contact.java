package srv.domain;

/**
 * @author Emma Driscoll
 * 
 *         This here is the ol' contact class
 * 
 *         This class is a data holder for contact info. Info includes
 *         firstName, lastName, email, phoneNum(ber), and address(street, city,
 *         state, zipcode)
 *
 */
public class Contact {

	private String firstName; // name of contact
	private String lastName;
	private String email; // email
	private String phoneNumWork; // optional work number for pets
	private String phoneNumMobile; // mobile phone for anyone
	private String address; // street,city,state,state, and zipcode combined for full address
	private String street;
	private String city;
	private String state;
	private String zipcode;

	public Contact(String firstName, String lastName, String email, String phoneNumWork, String phoneNumMobile,
			String street, String city, String state, String zipcode) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumWork = phoneNumWork;
		this.phoneNumMobile = phoneNumMobile;
		this.address = street + ", " + city + ", " + state + " " + zipcode;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
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

	public String getPhoneNumWork() {
		return phoneNumWork;
	}

	public void setPhoneNumWork(String phoneNumWork) {
		this.phoneNumWork = phoneNumWork;
	}

	public String getPhoneNumMobile() {
		return phoneNumMobile;
	}

	public void setPhoneNumwork(String phoneNumMobile) {
		this.phoneNumMobile = phoneNumMobile;
	}

	public String getAddress() { // returns full address
		return address;
	}

	public void setAddress() {
		this.address = street + ", " + city + ", " + state + " " + zipcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
		this.setAddress(); //update address
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		this.setAddress();
	}

	public String getState() {
		return state;

	}

	public void setState(String state) {
		this.state = state;
		this.setAddress();
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
		this.setAddress();
	}

	/**
	 * toString for contact info
	 */
	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + " lastName=" + lastName + ", email=" + email + ", Work phone number="
				+ phoneNumWork + ", Mobile phone number=" + phoneNumMobile + ", address=" + address + "]";
	}

}
