package srv.domain.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import aj.org.objectweb.asm.Type;
import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.event.eventype.JdbcTemplateEventTypeDao;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.user.JdbcTemplateUserDao;

/**
 * The JDBC Template that implements the Event DAO (data access object) interface.
 * An instance of this class is responsible to get data from the events table in the schema.sql
 * database. The methods this class implements are creating a new event query, updating an
 * existing event query, deleting a event query, and fetching a event query by its unique
 * primary id (eId). 
 * 
 * @author Lydia House
 *
 */
@ComponentScan("srv.config")
public class JdbcTemplateEventDao extends JdbcTemplateAbstractDao implements EventDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	@Autowired
	private JdbcTemplateContactDao contactDao;

	@Autowired
	private JdbcTemplateServiceClientDao serviceClientDao;
	
	@Autowired 
	private JdbcTemplateEventTypeDao eventTypeDao;

	public JdbcTemplateEventDao() {
		super();
	}

	/*
	 * Lists all the current events that are in the schema.sql database.
	 */
	@Override
	public List<Event> listAll() throws Exception {
		List<Event> results = getJdbcTemplate().query(
				"select * from events",
				new EventRowMapper());

		return results;
	}

	/*
	 * Creates a new Event in the schema.sql database. An exception is thrown
	 * if the new event is a duplicate. 
	 */
	@Override
	public Event create(String title, 
			String addr, 
			Integer cid,
			Date date, 
			Integer eventTypeId, 
			Boolean continuous, 
			Integer volunteersNeeded, 
			Integer scid, 
			Double neededVolunteerHours, 
			Double rsvpVolunteerHours,
			String freeTextField) throws Exception {

		assert(eventTypeId != null);
		
		// SQL statement that is to be executed
		final String sql = "insert into events (title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventId" });
			
			
			fillInTheBlanks(title, 
					addr, 
					cid, 
					date, 
					eventTypeId, 
					continuous, 
					volunteersNeeded, 
					scid,
					neededVolunteerHours, 
					rsvpVolunteerHours, 
					freeTextField, 
					ps);
			
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			String msg = String.format("Unable to insert new event [%s]", title);
			log.warn(msg);
			throw new Exception(msg);
		}

		log.debug("generated event id is {}", num);

		return this.fetchEventById((int) num);

	}

	/**
	 * Helper method used to set or nullify the blanks in a prepared statement.  You must
	 * refer to the SQL schema to ensure we are using the right types when nullifying.  See
	 * the SQL CREATE statement.   
	 */
	private void fillInTheBlanks(String title, String addr, Integer cid, Date date, Integer eventTypeId,
			Boolean continuous, Integer volunteersNeeded, Integer scid, Double neededVolunteerHours,
			Double rsvpVolunteerHours, String freeTextField, PreparedStatement ps) throws SQLException {
		
		if (title == null) 
			ps.setNull(1, java.sql.Types.VARCHAR);
		else
			ps.setString(1, title);
		
		if (addr == null)
			ps.setNull(2, java.sql.Types.VARCHAR);
		else
			ps.setString(2, addr);
		
		if (cid == null)
			ps.setNull(3, java.sql.Types.INTEGER);
		else
			ps.setInt(3, cid.intValue());
		
		
		
		if (date == null)
			ps.setNull(5, java.sql.Types.TIMESTAMP);
		else {
			Timestamp ts=new Timestamp(date.getTime());  // convert java.util.Date to a timestamp
			ps.setTimestamp(4,  ts);
		}
			
		if (eventTypeId == null)
			ps.setNull(5, java.sql.Types.INTEGER);
		else
			ps.setInt(5, eventTypeId);   
		
		if (continuous == null)
			ps.setNull(6, java.sql.Types.BOOLEAN);
		else
			ps.setBoolean(6, continuous);
		
		if (volunteersNeeded == null)
			ps.setNull(7, java.sql.Types.INTEGER);
		else
			ps.setInt(7, volunteersNeeded);
		
		if (scid == null)
			ps.setNull(8, java.sql.Types.INTEGER);
		else
			ps.setInt(8, scid);
		
		if (neededVolunteerHours == null)
			ps.setNull(9, java.sql.Types.INTEGER);
		else
			ps.setDouble(9, neededVolunteerHours);
		
		if (rsvpVolunteerHours == null)
			ps.setNull(10, java.sql.Types.DOUBLE);
		else
			ps.setDouble(10, rsvpVolunteerHours);
		
		if (freeTextField == null)
			ps.setNull(11, java.sql.Types.VARCHAR);
		else
			ps.setString(11, freeTextField);
	}

	/*
	 * Removes the desired Event (by id) from the schema.sql database. An
	 * exception is thrown if the event is unable to be removed (does not exist).
	 * 
	 * NOTE: in schema.sql we are setting references that have event as a foreign key, to be null when a 
	 * event is removed.
	 */
	@Override
	public void delete(int eid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from events where eventId= ?", new Object[] { eid });

		if (rc != 1) {
			String msg = String.format("unable to delete event titled [%s]", eid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	/* 
	 * Updates the desired Event (by id) in the schema.sql database with the new 
	 * specified content. An exception is thrown if the event is unable to be updated (does not exist).
	 */
	@Override
	public void update(int eid, String title, 
			String addr, 
			Integer cid,

			Date date, Integer eventTypeId, Boolean continuous,
			Integer volunteersNeeded, 
			Integer scid,
			
			Double neededVolunteerHours, 
			Double rsvpVolunteerHours,
			String freeTextField) throws Exception {
		
		// SQL statement that is to be executed
		final String sql = "update events "
				+ "set title = ?, address = ?, contactId = ?, dateOf = ?, eventTypeId = ?, continuous = ?, volunteersNeeded = ?, serviceClientId = ?, neededVolunteerHours = ?, rsvpVolunteerHours = ?, note = ? where eventId = ?";


		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventId" });
			
			this.fillInTheBlanks(title, addr, cid, date, eventTypeId, continuous, volunteersNeeded, scid, neededVolunteerHours, rsvpVolunteerHours, freeTextField, ps);
			
			ps.setInt(12, eid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("unable to update [{}]", eid);
		}
	}

	/*
	 * Finds the corresponding Event given the specified id. An exception is thrown
	 * if the event is unable to be fetched (does not exist).
	 */
	@Override
	public Event fetchEventById(int eid) throws Exception {
		String sqlStr = String.format(
				"select eventId, title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note from events where eventId = %d",
				eid);
		log.debug(sqlStr);

		List<Event> results = getJdbcTemplate().query(sqlStr, new EventRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch event reason [{}]", eid);
			return null;
		}

		return results.get(0);
	}
	
	/**
	 * Filters the list of events by dates, event types, service clients, and
	 * board members.
	 * 
	 */
	@Override
	public List<Event> listByFilter(String startDate, String endDate, Integer eTypeId, Integer scId, Integer bmId) throws Exception {
	
		// Allows for dynamic building of query string based on parameters
		StringBuffer queryBuff = new StringBuffer("select * from events ");
		
		// Flag for if the parameter is first in the query
		boolean first = true;
	
		// Filters for past events
		if (startDate != null) {
			if (first) {
				first = false;
				queryBuff.append("where ");
			}
			else {
				queryBuff.append("and ");
			}
			queryBuff.append("dateOf <= ");
			queryBuff.append("'");
			queryBuff.append(startDate);
			queryBuff.append("' ");
		}
		
		// Filters for future events
		if (endDate != null) {
			if (first) {
				first = false;
				queryBuff.append("where ");
			}
			else
				queryBuff.append("and ");
			
			queryBuff.append("dateOf >= ");
			queryBuff.append("'");
			queryBuff.append(endDate);
			queryBuff.append("' ");
		}
		
		// Filters by event type
		if (eTypeId != null) {
			if (first) {
				first = false;
				queryBuff.append("where ");
			}
			else
				queryBuff.append("and ");
			
			queryBuff.append("eventTypeId = ");
			queryBuff.append("'");
			queryBuff.append(eTypeId);
			queryBuff.append("' ");
		}
		
		// Filters by service client
		if (scId != null) {
			if (first) {
				first = false;
				queryBuff.append("where ");
			}
			else
				queryBuff.append("and ");
			
			queryBuff.append("serviceClientId = ");
			queryBuff.append("'");
			queryBuff.append(scId);
			queryBuff.append("' ");
		}
		
		// TODO wait until there is dao support for board member users
		// Filters by board member
		if (bmId != null) {
			
		}
		
		log.debug(queryBuff.toString());
		
		List<Event> results = getJdbcTemplate().query(queryBuff.toString(),	new EventRowMapper());

		return results;	
	}
	
	
	/**
	 * This class maps a Event database record to the Event model object by using
	 * a RowMapper interface to fetch the records for a Event from the database.
	 */
	class EventRowMapper implements RowMapper<Event> {
		/**
		 * Returns the Event in the given row
		 */
		@Override
		public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

			Event ev = new Event();

			try {

				ev.setEid(rs.getInt("eventId"))
						.setTitle(rs.getString("title"))
						.setAddress(rs.getString("address"))
						.setContact(contactDao.fetchContactById(rs.getInt("contactId")))
						.setDate(rs.getTimestamp("dateOf"))
						.setType(eventTypeDao.fetchEventTypeById(rs.getInt("eventTypeId"))) // rs.getString("boardMem"))
						.setContinous(rs.getBoolean("continuous")).setVolunteersNeeded(rs.getInt("volunteersNeeded"))
						.setServiceClient(serviceClientDao.fetchClientById(rs.getInt("serviceClientId")))
						.setNeededVolunteerHours(rs.getDouble("neededVolunteerHours"))
						.setRsvpVolunteerHours(rs.getDouble("rsvpVolunteerHours"))
						.setNote(rs.getString("note"));

			} catch (Exception e) {

				e.printStackTrace();
			}

			return ev;
		}
	}

}
