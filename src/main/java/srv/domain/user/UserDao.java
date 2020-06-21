package srv.domain.user;

import java.util.List;

/**
 *  Data Access Object Interface for User.java that defines the standard operations
 *  (CRUD) to be performed on the User model object as well as fetching a user by their username
 *  and unique id.
 *  
 * @author Lydia House
 *
 */
public interface UserDao {

	public List<User> listAll() throws Exception;
	
	
	/**
	 * @param userID Username
	 * @param totalHoursServed sum of hours served
	 * @param cid contactID
	 * @return
	 * @throws Exception
	 */
	public User create(String userID, int cid) throws Exception; 

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
	
	public User fetchUserByUsername(String uName) throws Exception;
	
	public void update(int uid, int newContact) throws Exception;
	
}
