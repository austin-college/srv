package srv.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 
 *
 *         The pet class is supposed to represent the information we are holding
 *         for a pet, or a service organization
 */
public class Pet {

	private String petName; // the name of the pet
	private String personOfContact; // name of pet representative or person of contact
	private String contactNumber; // contact phone number for pet
	private String email; // contact email for pet
	private int servantsReq; // amount of personel needed for an event
	private String eventLoc; // location of an event
	private String eventDescrp; // description of an event
	
	public Pet(String petName, String personOfContact, String contactNumber, String email, int servantsReq, String eventLoc, String eventDescrp) {
		super();
		this.petName = petName;
		this.personOfContact = personOfContact;
		this.contactNumber = contactNumber;
		this.email = email;
		this.servantsReq = servantsReq;
		this.eventLoc = eventLoc;
		this.eventDescrp = eventDescrp;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPersonOfContact() {
		return personOfContact;
	}

	public void setPersonOfContact(String personOfContact) {
		this.personOfContact = personOfContact;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getServantsReq() {
		return servantsReq;
	}

	public void setServantsReq(int servantsReq) {
		this.servantsReq = servantsReq;
	}
	
}
