package srv.domain.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.Contact;

public class JdbcTemplateEventTypeDao extends JdbcTemplateAbstractDao implements EventTypeDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateEventTypeDao.class);
	
	// Default Constructor
	public JdbcTemplateEventTypeDao() {
		super();
	}
	
	/*
	 * Lists all the current event types that are in the data.sql database.
	 */
	@Override
	public List<EventType> listAll() throws Exception {
		
		List<EventType> results = getJdbcTemplate()
				.query("select eventTypeId, name, description from eventTypes", new EventTypeRowMapper());
		 
		return results;
	}
	
	/*
	 * Creates a new EventType in the data.sql database.
	 * An exception is thrown if the new EventType is a duplicate. 
	 */
	@Override
	public EventType create(String name, String description) throws Exception {

		// SQL statement that is to be executed
		final String sql = "INSERT INTO eventTypes (name, description) VALUES(?, ?)";
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
	    getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"eventTypeId"});
	                  ps.setString(1, name);
	                  ps.setString(2, description);
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
	 * Removes the desired EventType (by id) from the data.sql database. 
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
	 * Updates the desired EventType (by id) in the data.sql database with the new specified content.
	 * An exception is thrown if the EventType is unable to be updates (does not exist).
	 */
	@Override
	public void update(int etid,  String name, String description) throws Exception {
		
		int rc = getJdbcTemplate().update(
				"UPDATE eventTypes SET name = ?, description = ? WHERE eventTypeId = ?", 
				new Object[] {name, description, etid});

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
		
		String sqlStr = String.format("SELECT eventTypeId, name, description"
				+ " FROM eventTypes WHERE eventTypeId = %d", etid);
		log.debug(sqlStr);
		
		List<EventType> results = getJdbcTemplate().query(sqlStr, new EventTypeRowMapper());
		
		if (results.size() < 1) {
			log.error("Unable to fetch event type id [{}]", etid);
			return null;
		}
		
		return results.get(0);
	}

	private class EventTypeRowMapper implements RowMapper<EventType> {
	    @Override
	    public EventType mapRow(ResultSet rs, int rowNum) throws SQLException {

	    	EventType et = new EventType();
	    	
	    	try {
	    			et.setEtid(rs.getInt("eventTypeId"))
	    			.setName(rs.getString("name"))
	    			.setDescription(rs.getString("description"));
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	return et;
	    }
	}

	
	
}