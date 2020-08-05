package srv.domain.user;

import java.util.ArrayList;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.servicegroup.ServiceGroup;

/**
 * An instance of this class represents the Board Member User base. These
 * members will have more power than your average user, and be able to manage a
 * list of Clients(pets) that are given to them.
 * 
 * @author hunter
 *
 */
public class BoardMemberUser extends ServantUser {

	private Boolean isCoChair;

	// Default constructor
	public BoardMemberUser() {
		
	}
	public BoardMemberUser(Integer uid, String userID, Contact contactInfo, Integer expectedGradYear, ServiceGroup aff, Boolean hasCar, Integer carCapacity) {
		super(uid, userID, contactInfo, expectedGradYear, aff, hasCar, carCapacity);
	}

	public Boolean getIsCoChair() {
		return isCoChair;
	}
	
	public BoardMemberUser setIsCoChair(Boolean isCoChair) {
		this.isCoChair = isCoChair;
		return this;
	}
	
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((isCoChair == null) ? 0 : isCoChair.hashCode());
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
		
		
		BoardMemberUser other = (BoardMemberUser) obj;
		
		if(other.getUid() == this.getUid())
			return true;
		return false;
	}

}
