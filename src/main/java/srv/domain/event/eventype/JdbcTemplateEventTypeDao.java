package srv.domain.event.eventype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.servicegroup.JdbcTemplateServiceGroupDao;

/**
 * The JDBC Template that implements the EventType DAO (data access object) interface.
 * An instance of this class is responsible to get data from the eventTypes table in the schema.sql
 * database. The methods this class implements are creating a new event type query, updating an
 * existing event type query, deleting a event type query, and fetching a event type query by its unique
 * primary id (etId). 
 * 
 * @author Lydia House
 *
 */
@ComponentScan("srv.config")
public class JdbcTemplateEventTypeDao extends JdbcTemplateAbstractDao implements EventTypeDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateEventTypeDao.class);
	
	@Autowired
	JdbcTemplateServiceClientDao scdao;
	
	@Autowired
	JdbcTemplateServiceGroupDao sgdao;
	
	
	// Default Constructor
	public JdbcTemplateEventTypeDao() {
		super();
	}
	
	/*
	 * Lists all the current event types that are in the schema.sql database.
	 */
	@Override
	public List<EventType> listAll() throws Exception {
		
		List<EventType> results = getJdbcTemplate()
				.query("select eventTypeId, name, description, defaultHours, pinHours, "
						+ "serviceClientId, serviceGroupId from eventTypes", new EventTypeRowMapper());
		 
		return results;
	}
	
	/*
	 * Creates a new EventType in the schema.sql database.
	 * An exception is thrown if the new EventType is a duplicate. 
	 */
	@Override
	public EventType create(String name, String description, int defHours, boolean pinHours, Integer scid, Integer sgid) throws Exception {

		// SQL statement that is to be executed
		final String sql = "INSERT INTO eventTypes (name, description, defaultHours, pinHours, serviceClientId, serviceGroupId) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
	    getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"eventTypeId"});
	                  ps.setString(1, name);
	                  ps.setString(2, description);
	                  ps.setInt(3, defHours);
	                  ps.setBoolean(4, pinHours);
	                  ps.setInt(5, scid);
	                  ps.setInt(6, sgid);
	                  return ps;
	              }, keyHolder);
		
	     Number num = keyHolder.getKey();
	     
		if (num == null ) {
			String msg = String.format("Unable to insert new eventType [%s]", name);
			log.warn(msg);
			throw new Exception("Unable to insert new unique eventType.");
		}
	   
	   log.debug("generated event type id is {}", num);
		
	   return this.fetchEventTypeById((int)num);
	   
	}
	
	/*
	 * Removes the desired EventType (by id) from the schema.sql database. 
	 * An exception is thrown if the eventType is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int etid) throws Exception {
		
		int rc = getJdbcTemplate().update("DELETE FROM eventTypes WHERE eventTypeId= ?", new Object[] { etid });
		
		if (rc != 1) {
			String msg = String.format("Unable to delete eventType [%s]",etid);
			log.warn(msg);
			throw new Exception(msg);
		}
		
	}
	
	/* 
	 * Updates the desired EventType (by id) in the schema.sql database with the new specified content.
	 * An exception is thrown if the EventType is unable to be updates (does not exist).
	 */
	@Override
	public void update(int etid,  String name, String description, int defHours, boolean pinHours, Integer scid, Integer sgid) throws Exception {
		
		int rc = getJdbcTemplate().update(
				"UPDATE eventTypes SET name = ?, description = ?, defaultHours = ?, "
				+ "pinHours = ?, serviceClientId = ?, serviceGroupId = ? WHERE eventTypeId = ?", 
				new Object[] {name, description, defHours, pinHours, scid, sgid, etid});

		if (rc < 1) {
			String msg = String.format("Unable to update eventType [%]", etid);
			log.error(msg);
			throw new Exception(msg);
		}
		
	}
	
	/*
	 * Finds the corresponding EventType given the specified id. 
	 * An exception is thrown if the EventType is unable to be fetched (does not exist).
	 */
	@Override
	public EventType fetchEventTypeById(int etid) throws Exception {
		
		String sqlStr = String.format("SELECT eventTypeId, name, description, defaultHours, pinHours, serviceClientId, serviceGroupId"
				+ " FROM eventTypes WHERE eventTypeId = %d", etid);
		log.debug(sqlStr);
		
		List<EventType> results = getJdbcTemplate().query(sqlStr, new EventTypeRowMapper());
		
		if (results.size() < 1) {
			log.error("Unable to fetch event type id [{}]", etid);
			return null;
		}
		
		return results.get(0);
	}

	/*
	 * This class maps a EventType database record to the EventType model object by using
	 * a RowMapper interface to fetch the records for a EventType from the database.
	 */
	private class EventTypeRowMapper implements RowMapper<EventType> {
	    @Override
	    public EventType mapRow(ResultSet rs, int rowNum) throws SQLException {

	    	EventType et = new EventType();
	    	
	    	try {
	    			et.setEtid(rs.getInt("eventTypeId"))
	    			.setName(rs.getString("name"))
	    			.setDescription(rs.getString("description"))
	    			.setDefHours(rs.getInt("defaultHours"))
	    			.setPinHours(rs.getBoolean("pinHours"))
	    			.setDefClient(scdao.fetchClientById(rs.getInt("serviceClientId")))
	    			.setDefOrg(sgdao.fetchServiceGroupById(rs.getInt("serviceGroupId")));
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return et;
	    }
	}	
}
