package srv.domain.user;

import srv.domain.ServiceGroup;
import srv.domain.ServiceHours;

/**
 * This is class is an extension of the User class, and represents
 * the majority of our User-base. Each Servant User will have a classification
 * on creation, and their total hours served will be set to 0 at first as well
 * 
 * We will leave the affiliation to be set manually by the user
 * 
 * @author hunter
 *
 */
public class ServantUser extends User {
	
	private char classification; // The year in school they are
	private ServiceGroup affiliation; // a group that they serve with
	private double totalHoursServed; // the total amount of hours they have served
	//TODO Add favorite categories for events

	public ServantUser(String userID, String password, char classification) {
		super(userID, password);
		this.classification = classification;
		totalHoursServed = 0.0;
	}
	
	/**
	 * this method on call will collect a ServiceHours class,
	 * and from this class will we will send the data to be approved
	 * 
	 * as of now, this method is a stub and is unimplemented
	 * @param submittedHours
	 * @return
	 */
	public ServiceHours logHours(ServiceHours submittedHours) {
		return submittedHours;
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

	public void setTotalHoursServed(double totalHoursServed) {
		this.totalHoursServed = totalHoursServed;
	}
		

}
