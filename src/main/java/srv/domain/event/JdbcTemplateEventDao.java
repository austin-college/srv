package srv.domain.event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

@ComponentScan("srv.config")
public class JdbcTemplateEventDao extends JdbcTemplateAbstractDao implements EventDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	@Autowired
	private JdbcTemplateContactDao contactDao;
	

	public JdbcTemplateEventDao() {
		super();
	}

	@Override
	public List<User> listAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event create(String title, String addr, String date, int cid, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId) throws Exception {

		// update executes the sql script that is in its paramaters. The sequel script
		// inserts a new user into the database
		int rc = getJdbcTemplate().update(
				"insert into events (title, address, contactId, dateOf, eventType, continuous, volunteersNeeded, organizationId, participantsListId) values ( '"
						+ title + "', '" + addr + "', '" + date + "', " + cid + ", '" + eventType + "', " + continuous
						+ ", " + volunteersNeeded + ", " + organizationId
						+ " (select contactId from contacts where contactId = " + cid + "))");

		if (rc != 1) {
			String msg = String.format("Unable to insert new event [%s]", title, addr, date, cid, eventType, continuous,
					volunteersNeeded, organizationId);
			log.warn(msg);
			throw new Exception("Unable to insert new unique user.");
		}

		Event results = getJdbcTemplate().queryForObject(String.format(
				"SELECT eventId, title, contactId, dateOf, eventType, continuous, volunteersNeeded, organizationId, participantsListId FROM users WHERE username = '%s'",
				title), new EventRowMapper());

		getJdbcTemplate().update("CREATE TABLE participants" + results.getEid() + " (participantNum integer auto_increment,"
				+ "	userId int, numHoursServed double, primary key (participantNum),"
				+ "	foreign key (userId) references users(userId) on delete set NULL);");

		// Run update method so that results is updated with the new name for the participantslistId
		// Reobtain results TODO
		
		return results;
	}

	@Override
	public void delete(int eid) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void changePassword(int eid, String newPassword) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Event fetchEventById(int eid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact fetchEventContactById(int cid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User fetchFromParticipantsList(int uid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	class EventRowMapper implements RowMapper<Event> {
		/**
		 * Returns the User in the given row
		 */
		@Override
		public Event mapRow(ResultSet rs, int rowNum) throws SQLException {

			Event us = new Event();
			
			try {
				us.setEid(rs.getInt("eventId"))
				  .setTitle(rs.getString("title"))
				  .setAddress(rs.getString("address"))
				
				  .setContact(contactDao.fetchContactById(rs.getInt("contactId")))
						  
				  .setDate(rs.getString("dateOf"))
				  
				  //.setType(rs.getString("eventType"))  must fix doa for event type first
				  
				  // .setContinuous(rs.getBoolean("continuous"))  use the right param
				  
				  .setVolunteersNeeded(rs.getInt("volunteersNeeded"))
				  // .setSetServiceClientId(rs.getInt("organizationId")) check param types
				  
				  // .setParticipantsListId(rs.getInt("participantsListId"));
				  ;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return us;
		}
	}
}
