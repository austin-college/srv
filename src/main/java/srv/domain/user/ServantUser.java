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
	
	protected Integer expectedGradYear; 
	protected ServiceGroup affiliation; // a group that they serve with
	protected Boolean hasCar; 
	protected Integer carCapacity;
	
	//TODO Add favorite categories for events

	// Default Constructor
	public ServantUser() {
		
	}
	
	public ServantUser(Integer uid, String userID, Contact contactInfo, Integer expectedGradYear, ServiceGroup aff, Boolean hasCar, Integer carCapacity) {
		super(uid, userID, contactInfo);
		this.affiliation = aff;
		this.expectedGradYear = expectedGradYear;
		this.hasCar = hasCar;
		this.carCapacity = carCapacity;
	}
	
	public Integer getExpectedGradYear() {
		return expectedGradYear;
	}

	public ServiceGroup getAffiliation() {
		return affiliation;
	}
	
	public Boolean getHasCar() {
		return hasCar;
	}
	
	public Integer getCarCapacity() {
		return carCapacity;
	}
	
	public ServantUser setExpectedGradYear(Integer newExpectedGradYear) {
		this.expectedGradYear = newExpectedGradYear;
		return this;
	}

	public ServantUser setAffiliation(ServiceGroup affiliation) {
		this.affiliation = affiliation;
		return this;
	}
	
	public ServantUser setHasCar(Boolean hasCar) {
		this.hasCar = hasCar;
		return this;
	}
	
	public ServantUser setCarCapacity(Integer carCapacity) {
		this.carCapacity = carCapacity;
		return this;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((affiliation == null) ? 0 : affiliation.hashCode());
		result = prime * result + ((carCapacity == null) ? 0 : carCapacity.hashCode());
		result = prime * result + ((expectedGradYear == null) ? 0 : expectedGradYear.hashCode());
		result = prime * result + ((hasCar == null) ? 0 : hasCar.hashCode());
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
