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
		
		List<ServantUser> currentSrvUsers = getJdbcTemplate().query("select * from servantUsers", new ServantUserRowMapper());
		
		return currentSrvUsers;
	}
	
	/*
	 * 
	 * if given userid (rbuckle19). create new User if not already created with userid (rbuckle19) with 
	 * new contact email (rbuckle19@austincollege.edu) and user’s first name might also be same as userid 
	 * until they edit someday. Once new user created, use to finish creating new ServantUser based on exiting
	 *  or newly created user 
	 */
	@Override
	public ServantUser create(String username, Integer sgid, Integer expectedGradYr) throws Exception {
		
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
		String sql = "INSERT INTO servantUsers (userId, sgid, expectedGradYear) VALUES (?, ?, ?)";


		getJdbcTemplate().update(sql, userId, sgid, expectedGradYr);

		return fetchServantUserById(userId);

	}
	
	/*
	 * Returns the ServantUser from the database with the specified user id.
	 */
	@Override
	public ServantUser fetchServantUserById(int userId) throws Exception {
		
		String sqlStr = String.format("select * from servantUsers where userId = %d", userId);
		log.debug(sqlStr);
		
		List<ServantUser> specifiedSrvUser = getJdbcTemplate().query(sqlStr, new ServantUserRowMapper());
		
		if (specifiedSrvUser.size() != 1) {
			log.error("unable to fetch servant user with id [{}]", userId);
			return null;
		}
		
		return specifiedSrvUser.get(0);
		
	}
	
	/*
	 * Returns the ServantUser from the database with the specified username.
	 */
	@Override
	public ServantUser fetchServantUserByUsername(String username) throws Exception {
		
	//	String sqlStr
		
		return null;
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
				Contact currentContactInfo = currentUser.getContactInfo() != null ? currentUser.getContactInfo() : null;
				
				srvUser.setExpectedGradYear(currentExpectedGradYr)
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
