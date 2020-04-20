package srv.domain.user;

import java.util.List;

import srv.domain.contact.Contact;

public interface UserDao {

	public List<User> listAll() throws Exception;
	
	
	/**
	 * @param userID Username
	 * @param totalHoursServed sum of hours served
	 * @param cid contactID
	 * @return
	 * @throws Exception
	 */
	public User create(String userID, double totalHoursServed, int cid) throws Exception; 

	/**
	 * @param uid unique user ID
	 * @throws Exception
	 */
	public void delete(int uid) throws Exception;

	/**
	 * @param uid unique user ID
	 * @return
	 * @throws Exception
	 */
	public User fetchUserById(int uid) throws Exception;
	
	/**
	 * @param cid users contact id
	 * @return
	 * @throws Exception
	 */
	public Contact fetchUserContactById(int cid) throws Exception;
	
	public void Update(int uid, String newUsername, double newHoursServed, int newContact) throws Exception;

	public void AddHoursServed(int uid, double amount) throws Exception;
	
	public void changeUserName(int uid, String newUsername) throws Exception;
}
