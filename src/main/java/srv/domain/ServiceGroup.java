package srv.domain;

import srv.domain.contact.Contact;

/**
 * @author Emma Driscoll This class is a data holder for Service
 *         groups(organizations/clubs on campus) info.
 *
 */
public class ServiceGroup {

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

	public Integer getSgid() {
		return sgid;
	}

	public void setSgid(Integer sgid) {
		this.sgid = sgid;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
	}

}
