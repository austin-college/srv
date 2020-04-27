package srv.domain.event.eventParticipant;

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
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.user.JdbcTemplateUserDao;

@ComponentScan("srv.config")
public class JdbcTemplateEventParticipantDao extends JdbcTemplateAbstractDao implements EventParticipantDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	@Autowired
	private JdbcTemplateUserDao userDao;

	@Autowired
	private JdbcTemplateEventDao eventDao;

	/*
	 * TODO We need an eventParticipantsDao if we want to pull a list of
	 * participants
	 */
//	@Autowired
//	private JdbcTemplateEventsParticipantsDao eventParticipantsDao;

	public JdbcTemplateEventParticipantDao() {
		super();
	}

	@Override
	public List<EventParticipant> listAll() throws Exception {
		List<EventParticipant> results = getJdbcTemplate().query(
				"select eventParticipantId, eventId, userId from eventParticipants", new EventParticipantRowMapper());

		return results;
	}

	@Override
	public EventParticipant create(int eid, int uid) throws Exception {

		// SQL statement that is to be executed
		final String sql = "insert into eventParticipants (eventId, userId) values(?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventParticipantId" });
			ps.setInt(1, eid);
			ps.setInt(2, uid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			String msg = String.format("Unable to insert new EventParticipant [%s]", eid, uid);
			log.warn(msg);
			throw new Exception(msg);
		}

		log.debug("generated EventParticipant id is {}", num);

		return this.fetchEventParticipantById((int) num);

	}

	@Override
	public void delete(int epid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from eventParticipants where eventParticipantId= ?", new Object[] { epid });

		if (rc != 1) {
			String msg = String.format("unable to delete EventParticipant titled [%s]", epid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	@Override
	public void update(int epid, int eid, int uid) throws Exception {
		// SQL statement that is to be executed
		final String sql = "update eventParticipants set eventId = ?, userId = ? where eventParticipantId = ?";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// fills in the SQL statements ?'s
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "eventParticipantId" });
			ps.setInt(1, eid);
			ps.setInt(2, uid);
			ps.setInt(3, epid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("unable to update [{}]", eid);
		}
	}

	@Override
	public EventParticipant fetchEventParticipantById(int eid) throws Exception {
		String sqlStr = String.format(
				"select eventParticipantId, eventId, userId from eventParticipants where eventParticipantId = %d",
				eid);
		log.debug(sqlStr);

		List<EventParticipant> results = getJdbcTemplate().query(sqlStr, new EventParticipantRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch EventParticipant reason [{}]", eid);
			return null;
		}

		return results.get(0);
	}

	class EventParticipantRowMapper implements RowMapper<EventParticipant> {
		/**
		 * Returns the User in the given row
		 */
		@Override
		public EventParticipant mapRow(ResultSet rs, int rowNum) throws SQLException {

			EventParticipant ep = new EventParticipant();

			/*
			 * We use the ContactDao in order to access the contacts table in the data.sql
			 * database, so that the service client has a handle on that contact.
			 */

			/*
			 * We use the serviceClientDao in order to access the contacts table in the
			 * data.sql database, so that the service client has a handle on that contact.
			 */

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
