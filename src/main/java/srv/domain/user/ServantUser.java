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
	
	private char classification; // The year in school they are
	private ServiceGroup affiliation; // a group that they serve with
	//TODO Add favorite categories for events

	public ServantUser(Integer uid, String userID,Contact contactInfo, char classification) {
		super(uid, userID, contactInfo);
		this.classification = classification;
	}
	
	public char getClassification() {
		return classification;
	}

	public ServiceGroup getAffiliation() {
		return affiliation;
	}

	public void setClassification(char classification) {
		this.classification = classification;
	}

	public void setAffiliation(ServiceGroup affiliation) {
		this.affiliation = affiliation;
	}

}
