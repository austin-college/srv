package srv.domain.serviceHours;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
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
	private JdbcTemplateServiceClientDao serviceClientDao;
	private JdbcTemplateUserDao userDao;
	private JdbcTemplateEventDao eventDao;
	
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
						+ "status from serviceHours", new ServiceHourRowMapper());
		
		return results;
	}

	@Override
	public ServiceHours create(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int shid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * An instance of this method fetched the ServiceHour by its id
	 */
	@Override
	public ServiceHours fetchHoursById(int shid) throws Exception {
		
		String sqlStr = String.format("SELECT serviceHourId, serviceClientId, userId, eventId, hours,"
				+ " status FROM serviceHours WHERE serviceClientId = %d", shid);
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
				.setStatus(rs.getString("status"));
				
				
			}	catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return sh; 
		}
		
		
	}

	
}
