package srv.domain.servicehours;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.user.JdbcTemplateUserDao;

/** 
 * Credit to Lydia House for this code
 * 
 * An instance of this class is the JDBC Template that implements the ServiceHoursDao 
 * This class is responsible for retrieving data from the serviceHours table in the 
 * data.sql database. The methods implemented are to create a new service hour query, 
 * to update an existing service hours query, delete a service hour query and fetching
 * a service hour query by its primary id.
 * 
 * @author fancynine9
 *
 */
@ComponentScan("srv.config")
public class JdbcTemplateServiceHoursDao extends JdbcTemplateAbstractDao implements ServiceHoursDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceHoursDao.class);
 
	@Autowired
	JdbcTemplateServiceClientDao serviceClientDao;
	@Autowired
	JdbcTemplateUserDao userDao;
	@Autowired
	JdbcTemplateEventDao eventDao;
	
	/**
	 * Default constructor. 
	 */
	public JdbcTemplateServiceHoursDao() {
		
		super();
	}
	
	
	@Override
	public List<ServiceHours> listAll() throws Exception {
		
		List<ServiceHours> results = getJdbcTemplate()
				.query("select serviceHourId, serviceClientId, userId, eventId, hours, "
						+ "status, reflection, description from serviceHours", new ServiceHourRowMapper());
		
		return results;
	}

	@Override
	public ServiceHours create(Integer scid, Integer uid, Integer eid, double hours, String stat, String reflection,
			String description) throws Exception {
		
		 final String sql = "INSERT INTO serviceHours (serviceClientId, userId, eventId, hours, status, reflection, description) VALUES(?, ?, ?, ?, ?, ?, ?)";
			
		  final KeyHolder keyHolder = new GeneratedKeyHolder();

		  /* 
		   * in the following code we are using java8's closure (lambda expression) feature .  It's like an anonymous inline 
		   * class definition of a listener.  JdbcTemplate update allows us to pass a snippet of code, given an 
		   * untyped parameter (connection).   Inside our code snippet, we can refer to the parameter by name. 
		   * Our snippet returns a prepared statement (which is what the JdbcTemplate.update method requires as 
		   * the first parameter.   The second parameter of the update method is a keyholder object that we can ask 
		   * for the database assigned auto number key value (a number).
		   * 
		   * Note: The preparedStatement's string array names the columns that are auto-number keys.   For the
		   * serviceHours table, the auto number key is serviceHourId.
		   * 
		   */
	      getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"serviceHourId"});
	                  ps.setInt(1, scid);
	                  ps.setInt(2, uid);
	                  ps.setInt(3, eid);
	                  ps.setDouble(4, hours);
	                  ps.setString(5, stat);
	                  ps.setString(6, reflection);
	                  ps.setString(7,  description);
	                  
	                  return ps;
	              }, keyHolder);
		
	     Number num = keyHolder.getKey();
	     
		if (num == null ) {
			String msg = String.format("Unable to insert new service hour [%d]", scid);
			log.warn(msg);
			throw new Exception("Unable to insert new service hour.");
		}
	   
	   log.debug("generated id is {}", num);
	   
	   return this.fetchHoursById((int)num);
		
		
	}


	@Override
	public void update(Integer shid, Integer scid, Integer uid, Integer eid, double hours, String stat,
			String reflection, String description) throws Exception {
	
		int rc = getJdbcTemplate().update("UPDATE serviceHours SET serviceClientId = ?, userId = ?, eventId = ?, hours = ?,"
				+ "status = ?, reflection = ?, description = ? WHERE serviceHourId = ?", 
				new Object[] { scid, uid, eid, hours, stat, reflection, description, shid});

		if (rc < 1) {
			log.error("Unable to update service hour [{}]", shid);
		}
		
	}
	/**
	 * Removes the desired Service Hour (by id) from the data.sql database. An exception is thrown if 
	 * a service hour doesn't exist. 
	 */
	@Override
	public void delete(int shid) throws Exception {
		
		int rc = getJdbcTemplate().update("DELETE FROM serviceHours WHERE serviceHourId= ?", new Object[] { shid });
		
		if (rc != 1) {
			String msg = String.format("Unable to delete Service Hour [%s]", shid);
			log.warn(msg);
			throw new Exception(msg);
		}
		
	}


	/**
	 * An instance of this method fetched the ServiceHour by its id
	 */
	@Override
	public ServiceHours fetchHoursById(int shid) throws Exception {
		
		String sqlStr = String.format("SELECT serviceHourId, serviceClientId, userId, eventId, hours,"
				+ " status, reflection, description FROM serviceHours WHERE serviceHourId = %d", shid);
		log.debug(sqlStr);

		List<ServiceHours> results = getJdbcTemplate().query(sqlStr, new ServiceHourRowMapper());
		
		if (results.size() < 1) {
			log.error("unable to fetch servant client id [{}]", shid);
			return null;
		}

		return results.get(0);
	}
	
	/**
	 * Private helper class that allows the ServiceHoursDao to be mapped to the ServiceClientDao,
	 * the UserDao, and the EventDao.
	 * 
	 * @author fancynine9
	 *
	 */
	private class ServiceHourRowMapper implements RowMapper<ServiceHours> {

		@Override
		public ServiceHours mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			/*
			 * We use the daos for each seperate entity
			 */
			 ServiceHours sh = new ServiceHours();
			 
			try {
				
				sh.setShid(rs.getInt("serviceHourId"))
				.setServedPet(serviceClientDao.fetchClientById(rs.getInt("serviceClientId")))
				.setServant(userDao.fetchUserById(rs.getInt("userId")))
				.setEvent(eventDao.fetchEventById(rs.getInt("eventId")))
				.setHours(rs.getDouble("hours"))
				.setStatus(rs.getString("status"))
				.setReflection(rs.getString("reflection"))
				.setDescription(rs.getString("description"));
				
				
			}	catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return sh; 
		}
		
		
	}	
}