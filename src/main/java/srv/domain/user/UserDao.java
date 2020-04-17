package srv.domain.user;

import java.util.List;

import srv.domain.contact.Contact;

public interface UserDao {

	public List<Contact> listAll() throws Exception;
	
	
	/**
	 * @param userID Username
	 * @param password password
	 * @param totalHoursServed sum of hours served
	 * @param cid contactID
	 * @return
	 * @throws Exception
	 */
	public Contact create(String userID, String password, double totalHoursServed, int cid) throws Exception; 

	/**
	 * @param uid unique user ID
	 * @throws Exception
	 */
	public void delete(int uid) throws Exception;
	
	/**
	 * @param uid unique user ID
	 * @param newVal
	 * @throws Exception
	 */
	public void update(int uid, String newVal) throws Exception;

	/**
	 * @param uid unique user ID
	 * @return
	 * @throws Exception
	 */
	public User fetchUserById(int uid) throws Exception;
}
