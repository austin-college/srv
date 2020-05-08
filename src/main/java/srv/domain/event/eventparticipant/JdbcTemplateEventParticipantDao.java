package srv.domain.event.eventparticipant;

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
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.user.JdbcTemplateUserDao;

/**
 * The JDBC Template that implements the EventParticipant DAO (data access object) interface.
 * An instance of this class is responsible to get data from the eventParticipants table in the schema.sql
 * database. The methods this class implements are creating a new event participant query, updating an
 * existing event participant query, deleting a event participant query, and fetching a event participant query by its unique
 * primary id (eventParticipantId). 
 * 
 * @author Lydia House
 *
 */
@ComponentScan("srv.config")
public class JdbcTemplateEventParticipantDao extends JdbcTemplateAbstractDao implements EventParticipantDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	@Autowired
	private JdbcTemplateUserDao userDao;

	@Autowired
	private JdbcTemplateEventDao eventDao;

	public JdbcTemplateEventParticipantDao() {
		super();
	}

	/*
	 * Lists all the current event participants that are in the schema.sql database
	 */
	@Override
	public List<EventParticipant> listAll() throws Exception {
		List<EventParticipant> results = getJdbcTemplate().query(
				"select eventParticipantId, eventId, userId from eventParticipants", new EventParticipantRowMapper());

		return results;
	}

	/*
	 * Creates a new EventParticipant in the schema.sql database. An exception is thrown if the new
	 * event participant is a duplicate
	 */
	@Override
	public EventParticipant create(int eventId, int userId) throws Exception {

		// SQL statement that is to be executed
		final String sql = "insert into eventParticipants (eventId, userId) values(?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventParticipantId" });
			ps.setInt(1, eventId);
			ps.setInt(2, userId);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			String msg = String.format("Unable to insert new EventParticipant [%s]", eventId, userId);
			log.warn(msg);
			throw new Exception(msg);
		}

		log.debug("generated EventParticipant id is {}", num);

		return this.fetchEventParticipantById((int) num);
	}

	/*
	 * Removes the desired EventParticipant (by id) from the schema.sql database. An
	 * exception is thrown if the event participant is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int eventParticipantId) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from eventParticipants where eventParticipantId= ?",
				new Object[] { eventParticipantId });

		if (rc != 1) {
			String msg = String.format("unable to delete EventParticipant titled [%s]", eventParticipantId);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	/*
	 * Updates the desired EventParticipant (by id) in the schema.sql database with the new
	 * specified content. An exception is thrown if the event participant is unable to updated (does not exist)
	 */
	@Override
	public void update(int eventParticipantId, int eventId, int userId) throws Exception {
		// SQL statement that is to be executed
		final String sql = "update eventParticipants set eventId = ?, userId = ? where eventParticipantId = ?";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventParticipantId" });
			ps.setInt(1, eventId);
			ps.setInt(2, userId);
			ps.setInt(3, eventParticipantId);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("unable to update [{}]", eventId);
		}
	}

	/*
	 * Finds the corresponding EventParticipant given the specified id and returns the first instance of an
	 * EventParticipant in the database. An exception is thrown if the EventParticipant is unable to be fetched (does not exist).
	 */
	@Override
	public EventParticipant fetchEventParticipantById(int eventParticipantId) throws Exception {
		String sqlStr = String.format(
				"select eventParticipantId, eventId, userId from eventParticipants where eventParticipantId = %d",
				eventParticipantId);
		log.debug(sqlStr);

		List<EventParticipant> results = getJdbcTemplate().query(sqlStr, new EventParticipantRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch EventParticipant reason [{}]", eventParticipantId);
			return null;
		}

		return results.get(0);
	}

	/*
	 * Finds the corresponding EventParticipant given the specified id and returns the list of all the people (users)
	 * that have RSVPed for the specified event.
	 */
	@Override
	public List<EventParticipant> fetchAllEventParticipantsByEventId(int eventId) throws Exception {

		String sqlStr = String
				.format("select eventParticipantId, eventId, userId from eventParticipants where eventId = %d", eventId);

		List<EventParticipant> results = getJdbcTemplate().query(sqlStr, new EventParticipantRowMapper());

		return results;
	}

	/**
	 * This class maps a EventParticipant database record to the EventParticipant model object by using
	 * a RowMapper interface to fetch the records for a EventParticipant from the database.
	 */
	class EventParticipantRowMapper implements RowMapper<EventParticipant> {
		/**
		 * Returns the EventParticipant in the given row
		 */
		@Override
		public EventParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {

			EventParticipant ep = new EventParticipant();

			try {

				ep.setEventParticipantId(rs.getInt("eventParticipantId"))
						.setEvent(eventDao.fetchEventById(rs.getInt("eventId")))
						.setUser(userDao.fetchUserById(rs.getInt("userId")));
			} catch (Exception e) {

				e.printStackTrace();
			}

			return ep;
		}
	}
}
