package srv.domain.user;

import java.util.ArrayList;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.serviceclient.ServiceClient;

/**
 * An instance of this class represents the Board Member User base. These
 * members will have more power than your average user, and be able to manage a
 * list of Clients(pets) that are given to them.
 * 
 * @author hunter
 *
 */
public class BoardMemberUser extends ServantUser {

	public BoardMemberUser(Integer uid, String userID, Contact contactInfo, char classification) {
		super(uid, userID, contactInfo, classification);
	}

	private ArrayList<ServiceClient> managedGroups; // Organizations the Board Member can Manage
	private Boolean isCoChair;

	public ArrayList<ServiceClient> getManagedGroups() {
		return managedGroups;
	}

	public BoardMemberUser setManagedGroups(ArrayList<ServiceClient> managedGroups) {
		this.managedGroups = managedGroups;
		return this;
	}

	public Boolean getIsCoChair() {
		return isCoChair;
	}

	public Boolean isCoChair() {
		if (this.isCoChair == null) return false;
		return isCoChair.booleanValue();
	}
	
	public BoardMemberUser setIsCoChair(Boolean isCoChair) {
		this.isCoChair = isCoChair;
		return this;
	}

}
