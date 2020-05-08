package srv.domain.servicegroup;

import java.io.Serializable;

import srv.domain.contact.Contact;

/**
 * @author Emma Driscoll This class is a data holder for Service
 *         groups(organizations/clubs on campus) info.
 *
 */
public class ServiceGroup implements Serializable{

	private Integer sgid; // unique id for each group
	private String shortName; // nickname/acronym for group i.e. APO
	private String title; // full name for group i.e Alpha Phi Omega
	private Contact contactInfo; // contact for head of group

	public ServiceGroup(Integer sgid, String shortName, String title, Contact contactInfo) {
		super();
		this.sgid = sgid;
		this.shortName = shortName;
		this.title = title;
		this.contactInfo = contactInfo;
	}
	
	public ServiceGroup() {
		super();
	}

	public Integer getSgid() {
		return sgid;
	}

	public ServiceGroup setSgid(Integer sgid) {
		this.sgid = sgid;
		return this;
	}

	public String getShortName() {
		return shortName;
	}

	public ServiceGroup setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ServiceGroup setTitle(String title) {
		this.title = title;
		return this;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public ServiceGroup setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
		return this;
	}

}
