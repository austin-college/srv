package srv.domain.serviceclient;
import java.io.Serializable;

/**
 * An instance of this class encapsulates a service client (Pet).
 * 
 * @author lahouse
 *
 */
public class ServiceClient implements Serializable {

	private Integer scid;
	private String title;
	private String contact;
	private String boardMember;
	private String category;

	public ServiceClient(Integer scid, String title, String contact, String bm, String cat) {
		super();
		this.scid = scid;
		this.title = title;
		this.contact = contact;
		this.boardMember = bm;
		this.category = cat;
	}

	public ServiceClient() {
		super();
	}

	public Integer getScid() {
		return scid;
	}

	public ServiceClient setScid(Integer scid) {
		this.scid = scid;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ServiceClient setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public String getContact() {
		return contact;
	}
	
	public ServiceClient setContact(String contact) {
		this.contact = contact;
		return this;
	}
	
	public String getBoardMember(String bm) {
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

}