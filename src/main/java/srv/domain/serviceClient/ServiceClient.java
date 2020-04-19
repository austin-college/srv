package srv.domain.serviceClient;

import java.io.Serializable;

import srv.domain.contact.Contact;

/**
 * @author
 *
 *         The ServiceClient class is supposed to represent the information we
 *         are holding for a pet, or a service organization
 */
public class ServiceClient implements Serializable {

	private Integer scid; // unique id for each service client
	private String name; // the name of the service client
	// private Contact mainContact; // the primary contact for service client
	private int mainContact;
	private int otherContact;
	// private Contact otherContact; // the secondary contact for service client
	private String boardMember; // current board member responsible for the service client
	private String category; // type of work client does ex: animals

	// putting these to the side for now - lhouse
	private String eventLoc; // location of an event
	private String eventDescrp; // description of an event
	private int servantsReq; // amount of personel needed for an event

	public ServiceClient(Integer scid, String new_name, int con1, int con2, String bm, String cat) { // int
																										// servantsReq,
																										// String
																										// eventLoc,
																										// String
																										// eventDescrp)
																										// {
		super();
		this.scid = scid;
		this.name = new_name;
		this.mainContact = con1;
		this.otherContact = con2;
		this.boardMember = bm;
		this.category = cat;

		/*
		 * setting these aside for database support - lydia this.servantsReq =
		 * servantsReq; this.eventLoc = eventLoc; this.eventDescrp = eventDescrp;
		 */
	}

	public Integer getClientId() {
		return scid;
	}

	public ServiceClient getClientId(Integer scid) {
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

	public int getMainContact() {
		return mainContact;
	}

	public ServiceClient setMainContact(int con) {
		this.mainContact = con;
		return this;
	}

	public int getOtherContact() {
		return otherContact;
	}

	public ServiceClient setOtherContact(int con) {
		this.otherContact = con;
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
