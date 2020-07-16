package srv.domain.contact;

import java.io.Serializable;

/**
 * An instance of this class is a data holder for contact information. The information
 * included is the contact's first and last name, their phone numbers, an
 * email address, and their address broken down into street, city, state and zipcode, and
 * a unique integer id.
 * 
 * @author Lydia House
 */
public class Contact implements Serializable {
	
	private static final long serialVersionUID = -6804336704154302018L;

	
	private int cid; // Unique id for contact, given by database
	private String firstName; 
	private String lastName;
	private String email; 
	private String primaryPhone; 
	private String secondaryPhone; 
	private String street;
	private String city;
	private String state;
	private String zipcode;


	public Contact() {
		super();
	}
	
	public Contact(int new_id, String firstName, String lastName, String email, String primaryPhone, String secondaryPhone,
			String street, String city, String state, String zipcode) {
		
		super();
		this.cid = new_id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.primaryPhone = primaryPhone;
		this.secondaryPhone = secondaryPhone;
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
	
	public int getContactId() {
		return cid;
	}
	
	public Contact setContactId(int new_id) {
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

	public String getPrimaryPhone() {
		return primaryPhone;
	}

	public Contact setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
		return this;
	}

	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	public Contact setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
		return this;
	}
	
	/*
	 * Returns full address (street, city, state, and zipcode)
	 */
	public String getAddress() {
		return street + ", " + city + ", " + state + " " + zipcode;
	}


	public String getStreet() {
		return street;
	}

	public Contact setStreet(String street) {
		this.street = street;
		return this;
	}

	public String getCity() {
		return city;
	}

	public Contact setCity(String city) {
		this.city = city;
		return this;
	}

	public String getState() {
		return state;

	}

	public Contact setState(String state) {
		this.state = state;
		return this;
	}

	public String getZipcode() {
		return zipcode;
	}

	public Contact setZipcode(String zipcode) {
		this.zipcode = zipcode;
		return this;
	}

	/**
	 * toString for contact info
	 */
	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + " lastName=" + lastName + ", email=" + email + ", Primary phone number="
				+ primaryPhone + ", Secondary phone number=" + secondaryPhone + ", address=" + this.getAddress() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (cid != other.cid)
			return false;
		return true;
	}

}
