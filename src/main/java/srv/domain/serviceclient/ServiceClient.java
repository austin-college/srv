package srv.domain.serviceclient;

import java.io.Serializable;

import srv.domain.contact.Contact;
import srv.domain.user.User;

/**
 * The ServiceClient class represents a service organization entity (referred to as a pet organization) 
 * that works frequently with the Service Station. 
 * 
 *  The information contained includes a unique integer id, the service client's name, main and other contacts, 
 *  the current board member responsible for the service client, and the category of volunteer work the 
 *  organization does (medical, seniors, education, animals, etc).
 *  
 * @author Lydia House
 *
 */
public class ServiceClient implements Serializable {

	private Integer scid; // unique id for each service client, given by database
	private String name; // the name of the service client
	private Contact mainContact; // the primary contact for the service client
	private Contact otherContact; // the secondary contact for the service client
	private User currentBoardMember; 
	private String category; // type of work client 

	public ServiceClient() {
		super();
	}

	public ServiceClient(Integer scid, String new_name, Contact con1, Contact con2, User bm, String cat) { 
		this.scid = scid;
		this.name = new_name;
		this.mainContact = con1;
		this.otherContact = con2;
		this.currentBoardMember = bm;
		this.category = cat;
	}

	public Integer getScid() {
		return scid;
	}

	public ServiceClient setClientId(Integer new_id) { 
		this.scid = new_id;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public ServiceClient setName(String new_name) {
		this.name = new_name;
		return this;
	}

	public User getBoardMember() {
		return currentBoardMember;
	}

	public ServiceClient setBoardMember(User bm) {
		this.currentBoardMember = bm;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public ServiceClient setCategory(String cat) {
		this.category = cat;
		return this;
	}

	public Contact getMainContact() {
		return mainContact;
	}

	public ServiceClient setMainContact(Contact con) {
		this.mainContact = con;
		return this;
	}

	public Contact getOtherContact() {
		return otherContact;
	}

	public ServiceClient setOtherContact(Contact con) {
		this.otherContact = con;
		return this;
	}
}
