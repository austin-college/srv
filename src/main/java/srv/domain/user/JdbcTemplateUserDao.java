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
                .addScript("user.sql")//script to create person table
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
		List<User> results = getJdbcTemplate().query("select uid, userID, password, totalHoursServed, cid", new UserRowMapper());
		 
		return results;
	}

	@Override
	public User create(String userID, String password, double totalHoursServed, int cid) throws Exception {
		int rc = jdbcTemplate.update("INSERT INTO users (userID, password, totalHoursServed, cid)", new Object[] {userID, password, totalHoursServed, cid});

		if (rc != 1) {
			String msg = String.format("Unable to insert new user [%s]", userID, password, totalHoursServed, cid);
			log.warn(msg);
			throw new Exception("Unable to insert new unique user.");
		}
	
		//TODO check this
	   User results = getJdbcTemplate().queryForObject(String.format("SELECT uid, userID, password, totalHoursServed, cid FROM users WHERE userID = '%s'", userID), new UserRowMapper());
	   
	   return results;
	}

	@Override
	public void delete(int uid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from users where uid= ?", new Object[] { uid });
		
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
		int rc = getJdbcTemplate().update("update users set password = ? where uid = ?", new Object[] { newPassword, uid });

		if (rc < 1) {
			log.error("unable to update passord [{}]",uid);
		}

	}

	@Override
	public User fetchUserById(int uid) throws Exception {
		String sqlStr = String.format("select uid, userID, password, totalHoursServed, cid from users where uid = %d",uid);
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
	    			.setUid(rs.getInt("uid"))
	        		.setUserID(rs.getString("userID"))
	        		.setPassword(rs.getString("password"))
	        		.setTotalHoursServed(rs.getDouble("totalHours"))
	    			.setCid(rs.getInt("cid"));
	        
	        return us;
	    }
	}
}
