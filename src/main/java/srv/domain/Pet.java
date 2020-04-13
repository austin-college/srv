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
	/*
	 * private String personOfContact; // name of pet representative or person of
	 * contact private String contactNumber; // contact phone number for pet private
	 * String email; // contact email for pet
	 */
	private Contact contact; // contact for pet
	private int servantsReq; // amount of personel needed for an event
	private String eventLoc; // location of an event
	private String eventDescrp; // description of an event
	
	public Pet(String petName, Contact contact, int servantsReq, String eventLoc, String eventDescrp) {
		super();
		this.petName = petName;
		this.contact = contact;
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

	

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	

	public int getServantsReq() {
		return servantsReq;
	}

	public void setServantsReq(int servantsReq) {
		this.servantsReq = servantsReq;
	}
	
}
