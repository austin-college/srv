package srv.domain;

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
	
	private char classification;
	private ServiceGroup affiliation;
	private double totalHoursServed;
	//TODO private EventType favoriteTypes; this is on the data model but I'm not sure if its supposed to be an event type

	public ServantUser(String userID, String password, char classification) {
		super(userID, password);
		this.classification = classification;
		totalHoursServed = 0;
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
	
	
	

}
