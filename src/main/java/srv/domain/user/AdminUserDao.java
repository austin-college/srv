package srv.domain.user;

import java.util.List;

/**
 * Data Access Object Interface for AdminUser.java that defines the standard operations (CRUD)
 * to be performed on the AdminUser model object as well as fetching an admin user by their unique id.
 * 
 * @author Lydia House
 *
 */
public interface AdminUserDao {

	public List<AdminUser> listAllAdminUsers() throws Exception;
	
	public AdminUser fetchAdminUserById(int userId) throws Exception;
	
	public AdminUser create(String username) throws Exception;
	
	public void update(int userId, Integer contactId) throws Exception;
	
	public void delete(int userId) throws Exception;
}
