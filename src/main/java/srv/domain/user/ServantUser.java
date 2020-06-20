package srv.domain.user;

import srv.domain.contact.Contact;
import srv.domain.servicegroup.ServiceGroup;

/**
 * This is class is an extension of the User class, and represents
 * the majority of our User-base. Each Servant User will have a classification
 * on creation.
 * 
 * We will leave the affiliation to be set manually by the user
 * 
 * @author hunter
 *
 */
public class ServantUser extends User {
	
	private Integer expectedGradYear; 
	private ServiceGroup affiliation; // a group that they serve with
	//TODO Add favorite categories for events

	// Default Constructor
	public ServantUser() {
		
	}
	
	public ServantUser(Integer uid, String userID, Contact contactInfo, Integer expectedGradYear, ServiceGroup aff) {
		super(uid, userID, contactInfo);
		this.affiliation = aff;
		this.expectedGradYear = expectedGradYear;
	}
	

	public Integer getExpectedGradYear() {
		return expectedGradYear;
	}

	public ServiceGroup getAffiliation() {
		return affiliation;
	}

	public ServantUser setExpectedGradYear(Integer newExpectedGradYear) {
		this.expectedGradYear = newExpectedGradYear;
		return this;
	}

	public ServantUser setAffiliation(ServiceGroup affiliation) {
		this.affiliation = affiliation;
		return this;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((affiliation == null) ? 0 : affiliation.hashCode());
		result = prime * result + ((expectedGradYear == null) ? 0 : expectedGradYear.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		
		ServantUser other = (ServantUser) obj;
		
		if(other.getUid() == this.getUid())
			return true;
		return false;
		
	}	

}
