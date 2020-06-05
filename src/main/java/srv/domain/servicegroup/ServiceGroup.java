package srv.domain.servicegroup;

import java.io.Serializable;

import srv.domain.contact.Contact;

/**
 * An instance of this represents a Service group entity which are organizations/clubs at 
 * Austin College such as APO. The information included is a unique integer id, the group's
 * abbreviated name (ex: APO), the group's full name (ex: Alpha Phi Omega) and the group's
 * contact information. 
 * 
 * @author Lydia House
 *
 */
public class ServiceGroup implements Serializable{

	private Integer sgid; // unique id for each group, given by database
	private String shortName; // nickname/acronym for group i.e. APO
	private String title; // full name for group i.e Alpha Phi Omega
	private Contact contactInfo; // contact for head of group

	public ServiceGroup() {
		super();
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sgid == null) ? 0 : sgid.hashCode());
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
		ServiceGroup other = (ServiceGroup) obj;
		if (sgid == null) {
			if (other.sgid != null)
				return false;
		} else if (!sgid.equals(other.sgid))
			return false;
		return true;
	}
	
	
}
