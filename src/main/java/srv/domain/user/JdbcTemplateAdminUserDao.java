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
import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;

public class JdbcTemplateAdminUserDao extends JdbcTemplateAbstractDao implements AdminUserDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateAdminUserDao.class);
		
	@Autowired
	private JdbcTemplateUserDao uDao;
	
	@Autowired 
	private JdbcTemplateContactDao contactDao;
	
	public JdbcTemplateAdminUserDao() {
		super();
	}

	
	/*
	 * Returns a list of all current servant users.
	 */
	@Override
	public List<AdminUser> listAllAdminUsers() throws Exception {
		
		List<AdminUser> currentAdminUsers = getJdbcTemplate().query("SELECT * FROM adminUsers", new AdminUserRowMapper());
		
		return currentAdminUsers;
	}
	
	/*
	 * Returns the AdminUser from the database with the specified user id.
	 */
	@Override
	public AdminUser fetchAdminUserById(int userId) throws Exception {
		
		// SQL statement that is to be executed
		String sql = String.format("SELECT * FROM adminUsers WHERE userId = %d", userId);
		log.debug(sql);
		
		List<AdminUser> specifiedAdminUser = getJdbcTemplate().query(sql, new AdminUserRowMapper());
		
		if (specifiedAdminUser.size() != 1) {
			log.error("Unable to fetch admin user with id [{}]", userId);
			return null;
		}
		
		return specifiedAdminUser.get(0);
		
	}
	
	/*
	 * Creates and returns a new AdminUser by the given username.
	 * 
	 * If the username does not already exist, create a new User with new contact email
	 * and first name that are the same as the username. Then, finishes creating new AdminUser
	 * based on existing or newly created user.
	 * 
	 * Throws an exception if the username was not specified since it is required.
	 */
	@Override
	public AdminUser create(String username) throws Exception {
		
		int userId;
		
		// username shall not be null
		if (username == null)
			throw new Exception("Username shall not be null.");
		
		// first get the user from the users data table
		User user = uDao.fetchUserByUsername(username);
		
		// verify if the user already exists or if a new one needs to be created
		if (user != null)
			userId = user.getUid();
		else {

			// Create and get a handle on a new contact for our new user
			String email = username + "@austincollege.edu";
			Contact newContact = contactDao.create(username, null, email, null, null, null, null, null, null);

			User newUser = uDao.create(username, newContact.getContactId());
			userId = newUser.getUid();
		}
		
		// SQL statement that is to be executed
		String sql = "INSERT INTO adminUsers (userId) VALUES (?)";
		
		getJdbcTemplate().update(sql, userId);
		
		return fetchAdminUserById(userId);
	}
	
	/**
	 * This class maps an AdminUser database record to the AdminUser model object by using
	 * a RowMapper interface to fetch the records for an Admin user from the data table.
	 */
	class AdminUserRowMapper implements RowMapper<AdminUser> {
		
		/*
		 * Returns the AdminUser in the given row
		 */
		@Override
		public AdminUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			AdminUser adminUser = new AdminUser();
			
			try {
				
				User currentUser = uDao.fetchUserById(rs.getInt("userId"));
				
				/*
				 * If the given object is null, then the current AdminUser's variable will be set
				 * to null since it is not required. Otherwise, we set the variable using daos
				 * when needed.
				 */
				Contact currentContactInfo = currentUser.getContactInfo() != null ? currentUser.getContactInfo() : null;

				adminUser.setUid(currentUser.getUid())
					.setUsername(currentUser.getUsername())
					.setContactInfo(currentContactInfo)
					.setRoll(AppConstants.ROLE_ADMIN);
					;
					
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return adminUser;
			
		}
	}
}
