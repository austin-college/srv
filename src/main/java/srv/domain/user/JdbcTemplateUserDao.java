package srv.domain.user;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import srv.domain.contact.Contact;
/**
 * AJ !!!! 
 * I MADE CHANGES TO NAMING CONVENTIONS SO LIKE
 * BEFORE YOU HAD uid and userID AND I CHANGED IT TO 
 * userId and username, respectively. I ALSO CHANGED cid TO contactId.
 * I JUST THINK THE NAMING CONVENTION WILL BE MORE UNDERSTANDABLE FOR EVERYONE
 * BUT IF YOU DON'T LIKE, FEEL FREE TO CHANGE IT AND I APOLOGIZE
 * 
 * @author Lydia House
 *
 */
public class JdbcTemplateUserDao implements UserDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;  
	public DataSource getDataSource() {
    	if (dataSource == null)
    		dataSource = h2DataSource();
    	
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public DataSource h2DataSource() {
	    
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("data.sql")//script to create person table
                .build();
    }
	
	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null)
			jdbcTemplate = new JdbcTemplate(getDataSource());
		
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplateUserDao() {
		super();
	}
	
	@Override
	public List<User> listAll() throws Exception {
		List<User> results = getJdbcTemplate().query("select userId, username, password, totalHoursServed, contactId", new UserRowMapper());
		 
		return results;
	}

	@Override
	public User create(String username, String password, double totalHoursServed, int cid) throws Exception {
		int rc = jdbcTemplate.update("INSERT INTO users (username, password, totalHoursServed, contactId)", new Object[] {username, password, totalHoursServed, cid});

		if (rc != 1) {
			String msg = String.format("Unable to insert new user [%s]", username, password, totalHoursServed, cid);
			log.warn(msg);
			throw new Exception("Unable to insert new unique user.");
		}
	
		//TODO check this
	   User results = getJdbcTemplate().queryForObject(String.format("SELECT userId, username, password, totalHoursServed, contactId FROM users WHERE username = '%s'", username), new UserRowMapper());
	   
	   return results;
	}

	@Override
	public void delete(int uid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from users where userId= ?", new Object[] { uid });
		
		if (rc != 1) {
			String msg = String.format("unable to delete user [%s]", uid);
			log.warn(msg);
			throw new Exception(msg);
		}		
	}

	/**
	 *Change Password method
	 */
	@Override
	public void update(int uid, String newPassword) throws Exception {
		int rc = getJdbcTemplate().update("update users set password = ? where userId = ?", new Object[] { newPassword, uid });

		if (rc < 1) {
			log.error("unable to update passord [{}]",uid);
		}

	}

	@Override
	public User fetchUserById(int uid) throws Exception {
		String sqlStr = String.format("select userId, username, password, totalHoursServed, contactId from users where userId = %d",uid);
		log.debug(sqlStr);
		
		List<User> results = getJdbcTemplate().query(sqlStr, new UserRowMapper());
		
		if (results.size() != 1) {
			log.error("unable to fetch reason [{}]",uid);
		}
		return results.get(0);
	}

	@Override
	public Contact fetchUserContactById(int uid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

	class UserRowMapper implements RowMapper < User > {
	    @Override
	    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

	    	User us = new User()
	    			.setUid(rs.getInt("userId"))
	        		.setUserID(rs.getString("username"))
	        		.setPassword(rs.getString("password"))
	        		.setTotalHoursServed(rs.getDouble("totalHours"))
	    			.setCid(rs.getInt("contactId"));
	        
	        return us;
	    }
	}
}
