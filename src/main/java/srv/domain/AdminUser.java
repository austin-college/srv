package srv.domain;


/**
 * An instance of this class represents our Admins for the system,
 * they are an advanced user type that a great deal of control over our system
 * 
 * @author hunter
 * 
 *
 */
public class AdminUser extends User {

	public AdminUser(String userID, String password) {
		super(userID, password);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * On call this method will collect a "pet" (serviceClient) and
	 * add it to a list or our data base.
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param addedPet
	 * @return
	 * The SeriveClient being entered
	 */
	public ServiceClient addPet(ServiceClient addedPet) {
		return addedPet;
	}
	
	/**
	 * On call this method will collect a "pet" (serviceClient) and search
	 * either our list or a data base for the selected entry.
	 * Once its found it will remove the entry from the list/data base
	 * and we will return the removed ServiceClient. 
	 * <p>
	 * If we do not find the serviceClient in our list, we will return a 
	 * null value to show that there was nothing to remove.
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param addedPet
	 * @return
	 * The SeriveClient being removed OR returns a null value if nothing was removed
	 */
	public ServiceClient removePet(ServiceClient removedPet) {
		return removedPet;
	}
	
	/**
	 * On call this method will allow the Admin user the ability to edit
	 * information about a given "pet" (serviceClient)
	 * <p>
	 * This method will probably bring the Admin to a page or dialog
	 * that allows them to specifically alter the pet
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param editedPet
	 * @return
	 */
	public ServiceClient editPet(ServiceClient editedPet) {
		return editedPet;
	}
	
	/**
	 * On call this method will collect ServiceHours from a source,
	 * and prompt the admin to either confirm or deny the hours.
	 * <p>
	 * For now this is an unimplemented stub until we have somewhere we can place data
	 * @param hoursToApprove
	 * @return
	 */
	public ServiceHours approveHours(ServiceHours hoursToApprove) {
		return hoursToApprove;
	}
	
	

}
