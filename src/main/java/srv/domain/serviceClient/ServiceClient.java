package srv.domain.serviceClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import srv.domain.contact.Contact;

/**
 * @author 
 *
 *         The ServiceClient class is supposed to represent the information we are holding
 *         for a pet, or a service organization
 */
public class ServiceClient implements Serializable {
	private Integer scid; // unique id for each client
	private String name; // the name of the pet
	
	/*
	 * private String personOfContact; // name of pet representative or person of
	 * contact private String contactNumber; // contact phone number for pet private
	 * String email; // contact email for pet
	 */
	private Contact contact; // contact for pet
	private String boardMember; // current board member responsible for the service client
	private String category; // type of work client does ex: animals
	
	
	// putting these to the side for now - lhouse
	private String eventLoc; // location of an event
	private String eventDescrp; // description of an event
	private int servantsReq; // amount of personel needed for an event
	

	public ServiceClient(Integer scid, String new_name, Contact con, String bm, String cat) { //int servantsReq, String eventLoc, String eventDescrp) {
		super();
		this.scid = scid;
		this.name = new_name;
		this.contact = con;
		this.boardMember = bm;
		this.category = cat;		
		
		/* setting these aside for database support - lydia
		this.servantsReq = servantsReq;
		this.eventLoc = eventLoc;
		this.eventDescrp = eventDescrp;
		*/
	}
	
	public Integer getScid() {
		return scid;
	}

	public ServiceClient setScid(Integer scid) {
		this.scid = scid;
		return this;
	}
	
	public ServiceClient() {
		super();
	}

	public String getName() {
		return name;
	}

	public ServiceClient setName(String new_name) {
		this.name = new_name;
		return this;
	}
	public String getBoardMember() {
		return boardMember;
	}
	
	public ServiceClient setBoardMember(String bm) {
		this.boardMember = bm;
		return this;
	}
	
	public String getCategory() {
		return category;
	}
	
	public ServiceClient setCategory(String cat) {
		this.category = cat;
		return this;
	}	

	public Contact getContact() {
		return contact;
	}

	public ServiceClient setContact(Contact contact) {
		this.contact = contact;
		return this;
	}
	
	public int getServantsReq() {
		return servantsReq;
	}

	public ServiceClient setServantsReq(int servantsReq) {
		this.servantsReq = servantsReq;
		return this;
	}
	
}
