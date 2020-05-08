package srv.domain.event;

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
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
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



	public JdbcTemplateEventDao() {
		super();
	}

	@Override
	public List<Event> listAll() throws Exception {
		List<Event> results = getJdbcTemplate().query(
				"select eventId, title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField from events",
				new EventRowMapper());

		return results;
	}

	@Override
	public Event create(String title, String addr, int cid, String date, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId, double neededVolunteerHours, double rsvpVolunteerHours,
			String freeTextField) throws Exception {

		// SQL statement that is to be executed
		final String sql = "insert into events (title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventId" });
			ps.setString(1, title);
			ps.setString(2, addr);
			ps.setInt(3, cid);
			ps.setString(4, date);
			ps.setString(5, eventType);
			ps.setBoolean(6, continuous);
			ps.setInt(7, volunteersNeeded);
			ps.setInt(8, organizationId);
			ps.setDouble(9, neededVolunteerHours);
			ps.setDouble(10, rsvpVolunteerHours);
			ps.setString(11, freeTextField);
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

	@Override
	public void delete(int eid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from events where eventId= ?", new Object[] { eid });

		if (rc != 1) {
			String msg = String.format("unable to delete event titled [%s]", eid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	@Override
	public void update(int eid, String title, String addr, int cid, String date, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId, double neededVolunteerHours, double rsvpVolunteerHours,
			String freeTextField) throws Exception {
		// SQL statement that is to be executed
		final String sql = "update events set title = ?, address = ?, contactId = ?, dateOf = ?, eventType = ?, continuous = ?, volunteersNeeded = ?, serviceClientId = ?, neededVolunteerHours = ?, rsvpVolunteerHours = ?, freeTextField = ? where eventId = ?";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventId" });
			ps.setString(1, title);
			ps.setString(2, addr);
			ps.setInt(3, cid);
			ps.setString(4, date);
			ps.setString(5, eventType);
			ps.setBoolean(6, continuous);
			ps.setInt(7, volunteersNeeded);
			ps.setInt(8, organizationId);
			ps.setDouble(9, neededVolunteerHours);
			ps.setDouble(10, rsvpVolunteerHours);
			ps.setString(11, freeTextField);
			ps.setInt(12, eid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("unable to update [{}]", eid);
		}
	}

	@Override
	public Event fetchEventById(int eid) throws Exception {
		String sqlStr = String.format(
				"select eventId, title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, freeTextField from events where eventId = %d",
				eid);
		log.debug(sqlStr);

		List<Event> results = getJdbcTemplate().query(sqlStr, new EventRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch event reason [{}]", eid);
			return null;
		}

		return results.get(0);
	}

	class EventRowMapper implements RowMapper<Event> {
		/**
		 * Returns the Event in the given row
		 */
		@Override
		public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

			Event ev = new Event();

			try {

				ev.setEid(rs.getInt("eventId")).setTitle(rs.getString("title")).setAddress(rs.getString("address"))
						.setContact(contactDao.fetchContactById(rs.getInt("contactId"))).setDate(rs.getString("dateOf"))
						.setTypeString(rs.getString("eventType")) // rs.getString("boardMem"))
						.setContinous(rs.getBoolean("continuous")).setVolunteersNeeded(rs.getInt("volunteersNeeded"))
						.setServiceClient(serviceClientDao.fetchClientById(rs.getInt("serviceClientId")))
						.setNeededVolunteerHours(rs.getDouble("neededVolunteerHours"))
						.setRsvpVolunteerHours(rs.getDouble("rsvpVolunteerHours"))
						.setFreeTextField(rs.getString("freeTextField"));

			} catch (Exception e) {

				e.printStackTrace();
			}

			return ev;
		}
	}
}
