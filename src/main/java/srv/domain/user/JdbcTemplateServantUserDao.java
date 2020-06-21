package srv.domain.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import srv.AppConstants;
import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.servicegroup.JdbcTemplateServiceGroupDao;

public class JdbcTemplateServantUserDao extends JdbcTemplateAbstractDao implements ServantUserDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServantUserDao.class);

	@Autowired
	private JdbcTemplateServiceGroupDao srvGroupDao;
	
	@Autowired
	private JdbcTemplateUserDao uDao;
	
	@Autowired 
	private JdbcTemplateContactDao contactDao;
	
	public JdbcTemplateServantUserDao() {
		super();
	}

	
	/*
	 * Returns a list of all current servant users.
	 */
	@Override
	public List<ServantUser> listAllServantUsers() throws Exception {
		
		List<ServantUser> currentSrvUsers = getJdbcTemplate().query("SELECT * FROM servantUsers", new ServantUserRowMapper());
		
		return currentSrvUsers;
	}
	
	/*
	 * Creates and returns a new ServantUser by the given username.
	 * 
	 * If the username does not already exist, create a new User with new contact email and first name
	 * that are the same as the username. Then, finishes creating new ServantUser based on existing
	 * or newly created user.
	 * 
	 * Throws an exception if the username was not specified since it is required.
	 */
	@Override
	public ServantUser create(String username, Integer sgid, Integer expectedGradYr, Boolean hasCar, Integer carCap) throws Exception {
		
		int userId;
		
		// username shall not be null
		if (username == null)
			throw new Exception("Thy username shall not be null.");
		
		// first get the user from the users data table 
		User user = uDao.fetchUserByUsername(username);
		
		// verify if the user already exists or if a new one needs to be created
		if (user != null) { 
			
			userId = user.getUid();
			
		}
		else {

			// Create and get a handle on a new contact for our new user
			String email = username + "@austincollege.edu";
			Contact newContact = contactDao.create(username, null, email, null, null, null, null, null, null);
			
			User newUser = uDao.create(username, newContact.getContactId());
			userId = newUser.getUid();
			
		}
		
		// SQL statement that is to be executed
		String sql = "INSERT INTO servantUsers (userId, sgid, expectedGradYear, hasCar, carCapacity) VALUES (?, ?, ?, ?, ?)";

		getJdbcTemplate().update(sql, userId, sgid, expectedGradYr, hasCar, carCap);

		return fetchServantUserById(userId);

	}
	
	/*
	 * Updates the specified ServantUser (by userId) with the given values.
	 * 
	 * Allowed to also update the contact id from User
	 */
	@Override
	public void update(int userId, Integer sgid, Integer expectedGradYr, Boolean hasCar, Integer carCap, Integer contactId) throws Exception {
		
		uDao.update(userId, contactId);
		
		// SQL statement that is to be executed
		final String sql = "UPDATE servantUsers SET sgid = ?, expectedGradYear = ?, hasCar = ?, carCapacity = ? WHERE userId = ?"; 
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "userId" });
			
			fillInTheBlanks(sgid, expectedGradYr, hasCar, carCap, ps);
			
			ps.setInt(5, userId);
			return ps;
		}, keyHolder);
		
		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("Unable to update servant user [{}]", userId);
			throw new Exception("Unable to update servant user " + userId);
		}
		
	}
	
	/*
	 * Removes the specified ServantUser (by userId) from the database. An exception 
	 * is thrown if the ServantUser is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int userId) throws Exception {
		
		int rc = getJdbcTemplate().update("DELETE FROM servantUsers WHERE userId = ?", new Object[] { userId} );
		
		if (rc != 1) {
			String errMsg = String.format("Unable to delete servant user [%d]", userId);
			log.error(errMsg);
			throw new Exception(errMsg);
		}
	}
	
	/*
	 * Returns the ServantUser from the database with the specified user id.
	 */
	@Override
	public ServantUser fetchServantUserById(int userId) throws Exception {
		
		String sqlStr = String.format("SELECT * FROM servantUsers WHERE userId = %d", userId);
		log.debug(sqlStr);
		
		List<ServantUser> specifiedSrvUser = getJdbcTemplate().query(sqlStr, new ServantUserRowMapper());
		
		if (specifiedSrvUser.size() != 1) {
			log.error("unable to fetch servant user with id [{}]", userId);
			return null;
		}
		
		return specifiedSrvUser.get(0);
		
	}
	
	/**
	 * Helper method used to set or nullify the blanks in a prepared statement. You must refer to the
	 * SQL schema to ensure we are using the right types when nullifying.
	 */
	private void fillInTheBlanks(Integer sgid, Integer expectedGradYr, Boolean hasCar, Integer carCap, PreparedStatement ps) throws SQLException {
		
		if (sgid == null)
			ps.setNull(1, java.sql.Types.INTEGER);
		else
			ps.setInt(1, sgid);
		
		if (expectedGradYr == null)
			ps.setNull(2, java.sql.Types.INTEGER);
		else
			ps.setInt(2, expectedGradYr);
		
		if (hasCar == null)
			ps.setNull(3, java.sql.Types.BOOLEAN);
		else
			ps.setBoolean(3, hasCar);
		
		if (carCap == null)
			ps.setNull(4, java.sql.Types.INTEGER);
		else
			ps.setInt(4, carCap);
	}
	
	/**
	 * This class maps a ServantUser database record to the ServantUser model object by using
	 * a RowMapper interface to fetch the records for a Servant user from the data store.
	 */
	class ServantUserRowMapper implements RowMapper<ServantUser> {
		
		/*
		 * Returns the ServantUser in the given row
		 */
		@Override
		public ServantUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				
			ServantUser srvUser = new ServantUser();
			
			try {
				
				User currentUser = uDao.fetchUserById(rs.getInt("userId"));
				
				
				/*
				 * If the given object is null, then the current ServantUser's variable will be set
				 * to null since it is not required. Otherwise, we set the variable using daos
				 * when needed.
				 */
				if (rs.getObject("sgid") != null) 
					srvUser.setAffiliation(srvGroupDao.fetchServiceGroupById(rs.getInt("sgid")));
				else
					srvUser.setAffiliation(null);
				
				Integer currentExpectedGradYr = rs.getObject("expectedGradYear") != null ? rs.getInt("expectedGradYear") : null;
				Boolean currentHasCar = rs.getObject("hasCar") != null ? rs.getBoolean("hasCar") : null;
				Integer currentCarCap = rs.getObject("carCapacity") != null ? rs.getInt("carCapacity") : null;
				Contact currentContactInfo = currentUser.getContactInfo() != null ? currentUser.getContactInfo() : null;
								
				srvUser.setExpectedGradYear(currentExpectedGradYr)
					.setHasCar(currentHasCar)
					.setCarCapacity(currentCarCap)
					.setUid(currentUser.getUid())
					.setContactInfo(currentContactInfo)
					.setUsername(currentUser.getUsername())
					.setRoll(AppConstants.ROLE_SERVANT);
					;
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return srvUser;
		}
	}

	

}
