package srv.domain;

import java.io.Serializable;

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
public class Contact implements Serializable {

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
	
	public Contact() {
		super();
	}
	public String getFirstName() {
		return firstName;
	}

	public Contact setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public Contact setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Contact setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhoneNumWork() {
		return phoneNumWork;
	}

	public Contact setPhoneNumWork(String phoneNumWork) {
		this.phoneNumWork = phoneNumWork;
		return this;
	}

	public String getPhoneNumMobile() {
		return phoneNumMobile;
	}

	public Contact setPhoneNumwork(String phoneNumMobile) {
		this.phoneNumMobile = phoneNumMobile;
		return this;
	}

	public String getAddress() { // returns full address
		return address;
	}

	public Contact setAddress() {
		this.address = street + ", " + city + ", " + state + " " + zipcode;
		return this;
	}

	public String getStreet() {
		return street;
	}

	public Contact setStreet(String street) {
		this.street = street;
		this.setAddress(); //update address
		return this;
	}

	public String getCity() {
		return city;
	}

	public Contact setCity(String city) {
		this.city = city;
		this.setAddress();
		return this;
	}

	public String getState() {
		return state;

	}

	public Contact setState(String state) {
		this.state = state;
		this.setAddress();
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public Contact setZipcode(String zipcode) {
		this.zipcode = zipcode;
		this.setAddress();
		return this;
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
