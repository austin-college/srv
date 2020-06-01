package srv.domain.contact;

import java.io.Serializable;

/**
 * An instance of this class is a data holder for contact information. The information
 * included is the contact's first and last name, their mobile and work phone numbers, an
 * email address, and their address broken down into street, city, state and zipcode, and
 * a unique integer id.
 * 
 * @author Lydia House
 */
public class Contact implements Serializable {
	
	private Integer cid; // Unique id for contact, given by database
	private String firstName; 
	private String lastName;
	private String email; 
	private String phoneNumWork; 
	private String phoneNumMobile; 
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String address; // street, city, state and zipcode combined for full address

	public Contact() {
		super();
	}
	
	public Contact(Integer new_id, String firstName, String lastName, String email, String phoneNumWork, String phoneNumMobile,
			String street, String city, String state, String zipcode) {
		
		super();
		this.cid = new_id;
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
	
	/**
	 * Provides the contacts full name or a non-null piece.  Returns
	 * null if both first and last names are unknown.
	 * 
	 * @return  full name of contact
	 */
	public String fullName() {
		if (this.firstName == null) return this.lastName;
		if (this.lastName == null) return this.firstName;
		
		return this.firstName+" "+this.lastName;
	}
	
	public Integer getContactId() {
		return cid;
	}
	
	public Contact setContactId(Integer new_id) {
		this.cid = new_id;
		return this;
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

	public Contact setPhoneNumMobile(String phoneNumMobile) {
		this.phoneNumMobile = phoneNumMobile;
		return this;
	}
	
	/*
	 * Returns full address (street, city, state, and zipcode)
	 */
	public String getAddress() {
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
		this.setAddress(); 
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
