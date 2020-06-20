package srv.domain.user;

import java.util.List;

/**
 * Data Access Object Interface for ServantUser.java that defines the standard operations (CRUD)
 * to be performed on the ServantUser model object as well as fetching a servant user by their 
 *unique id.
 * 
 * @author Lydia House
 *
 */
public interface ServantUserDao {
	
	public List<ServantUser> listAllServantUsers() throws Exception;
	
	public ServantUser create(String username, Integer sgid, Integer expectedGradYr) throws Exception;
	
	public ServantUser fetchServantUserById(int userId) throws Exception;
	
	public void update(int userId, Integer sgid, Integer expectedGradYr) throws Exception;
	
	public void delete(int userId) throws Exception;

}
