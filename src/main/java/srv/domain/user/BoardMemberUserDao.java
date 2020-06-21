package srv.domain.user;

import java.util.List;

/**
 * Data Access Object Interface for BoardMemberUser.java that defines
 * the standard operations (CRUD) to be performed on the BoardMemberUser
 * model object as well as fetching a board member user by their unique id.
 * 
 * @author Lydia House
 *
 */
public interface BoardMemberUserDao {
	
	public List<BoardMemberUser> listAllBoardMemberUsers() throws Exception;
	
	public BoardMemberUser fetchBoardMemberUserById(int userId) throws Exception;
	
	public BoardMemberUser create(String username, Boolean coChair) throws Exception;
	
	public void update(int userId, Boolean coChair, Integer sgid, Integer expectedGradYr, 
			Boolean hasCar, Integer carCap, Integer contactId) throws Exception;
	
	public void delete(int userId) throws Exception;
}
