package srv.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import srv.AppConstants;
import srv.domain.JdbcTemplateAbstractDao;

public class JdbcTemplateBoardMemberUserDao extends JdbcTemplateAbstractDao implements BoardMemberUserDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServantUserDao.class);
	
	@Autowired
	private JdbcTemplateServantUserDao srvUserDao;
	
	@Autowired
	private JdbcTemplateUserDao uDao;
	
	public JdbcTemplateBoardMemberUserDao() {
		super();
	}
	
	/*
	 * Returns a list of all current board member users
	 */
	@Override
	public List<BoardMemberUser> listAllBoardMemberUsers() throws Exception {
		
		List<BoardMemberUser> currentBmUsers = getJdbcTemplate().query("SELECT * FROM boardMemberUsers", new BoardMemberUserRowMapper());
		
		return currentBmUsers;
	}
	
	/*
	 * Returns the BoardMemberUser from the database with the specified user id.
	 */
	@Override
	public BoardMemberUser fetchBoardMemberUserById(int userId) throws Exception {
		
		String sql = String.format("SELECT * FROM boardMemberUsers WHERE userId = %d", userId);
		log.debug(sql);
		
		List<BoardMemberUser> specifiedBmUser = getJdbcTemplate().query(sql, new BoardMemberUserRowMapper());
		
		if (specifiedBmUser.size() != 1) {
			log.error("Unable to fetch board member user with id [{}]", userId);
			return null;
		}
		
		return specifiedBmUser.get(0);
	}
	
	/* 
	 * Creates and returns a new BoardMemberUser by the given username.
	 * 
	 * If the username does not already exist, create a new Servant User with null values for its
	 * variables Then, finish creating new BoardMemberUser based on existing or newly created user.
	 * 
	 * Throws an exception if the username was not specified since it is required.
	 */
	@Override
	public BoardMemberUser create(String username, Boolean coChair) throws Exception {
		
		int userId;
		
		// username shall not be null
		if (username == null)
			throw new Exception("Username shall not be null.");
		
		// first get the user from the user data table
		User user = uDao.fetchUserByUsername(username);
		
		// verify if the user already exists in the users table
		if (user != null) {
			
			userId = user.getUid();
			
			// verify if the user already exists in the servantUsers table, if not 
			// creates a new servant user
			if (srvUserDao.fetchServantUserById(userId) == null) {
				
				ServantUser newSrvUser = srvUserDao.create(username, null, null, null, null);
				userId = newSrvUser.getUid();
			}
		}
		else {
			
			// Create and get a handle on a new servant user for our new board member 
			// user by delegating to the ServantUserDao
			ServantUser newSrvUser = srvUserDao.create(username, null, null, null, null);
			userId = newSrvUser.getUid();
		}
		
		// SQL statement that is to be executed
		String sql = "INSERT INTO boardMemberUsers (userId, isCoChair) VALUES (?, ?)";
		
		getJdbcTemplate().update(sql, userId, coChair);
		
		return fetchBoardMemberUserById(userId);
		
	}
	
	/**
	 * This class maps a BoardMemberUser database record to the BoardMemberUser model object by using
	 * a RowMapper interface to fetch the records for a BoardMember user from the data store.
	 */
	class BoardMemberUserRowMapper implements RowMapper<BoardMemberUser> {
		
		/*
		 * Returns the BoardMemberUser in the given row
		 */
		@Override
		public BoardMemberUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			BoardMemberUser bmUser = new BoardMemberUser();
			
			try {
				
				ServantUser srvUser = srvUserDao.fetchServantUserById(rs.getInt("userId"));
				
				/*
				 * If the given object is null, then the current BoardMemberUser's variable
				 * will be set to null since it is not required. Otherwise, we set the variable
				 * using daos when needed.
				 */
				Boolean currentIsCoChair = rs.getObject("isCoChair") != null ? rs.getBoolean("isCoChair") : null;
				
				
				bmUser.setIsCoChair(currentIsCoChair)
					.setExpectedGradYear(srvUser.getExpectedGradYear())
					.setAffiliation(srvUser.getAffiliation())
					.setHasCar(srvUser.getHasCar())
					.setCarCapacity(srvUser.getCarCapacity())
					.setUid(srvUser.getUid())
					.setUsername(srvUser.getUsername())
					.setContactInfo(srvUser.getContactInfo())
					.setRoll(AppConstants.ROLE_BOARDMEMBER);
					;
			
				
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return bmUser;
		}
	}
}
