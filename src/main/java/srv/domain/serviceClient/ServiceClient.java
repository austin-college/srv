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
	private Contact mainContact; // the primary contact for service client
	private Contact otherContact; // the secondary contact for service client
	private String boardMember; // current board member responsible for the service client
	private String category; // type of work client does ex: animals


	// putting these to the side for now due to database - lhouse
	private String eventLoc; // location of an event - We get rid of this
	private String eventDescrp; // description of an event - we might rename this to ServiceClientDesc
	private int servantsReq; // amount of personel needed for an event

	public ServiceClient(Integer scid, String new_name, Contact con1, Contact con2, String bm, String cat) { 
		this.scid = scid;
		this.name = new_name;
		this.mainContact = con1;
		this.otherContact = con2;
		this.boardMember = bm;
		this.category = cat;

	}
	
	public ServiceClient() {
		super();
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

	public int getServantsReq() {
		return servantsReq;
	}

	public ServiceClient setServantsReq(int servantsReq) {
		this.servantsReq = servantsReq;
		return this;
	}

}
