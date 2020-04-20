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
import srv.domain.contact.JdbcTemplateContactDao;

/**
 * @author AJ Pritchard
 *
 */
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

		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("data.sql")// script to create
																									// person table
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

	/**
	 * Returns a list of all users
	 */
	@Override
	public List<User> listAll() throws Exception {
		List<User> results = getJdbcTemplate().query("select userId, username, totalHoursServed, contactId from users",
				new UserRowMapper());

		return results;
	}

	/**
	 * Creates a new User and adds it to the database
	 */
	@Override
	public User create(String username, double totalHoursServed, int cid) throws Exception {

		// update executes the sql script that is in its paramaters. The sequel script
		// inserts a new user into the database
//		int rc = getJdbcTemplate().update(
//				"insert into users (username, totalHoursServed, contactId) values (?, ?, (select contactId from contacts where contactId = ?))",
//				new Object[] { username, totalHoursServed, cid });
		int rc = jdbcTemplate.update("insert into users (username, totalHoursServed, contactId) values ( '" + username + "', " + totalHoursServed + ", (select contactId from contacts where contactId = " + cid + "))");

		if (rc != 1) {
			String msg = String.format("Unable to insert new user [%s]", username, totalHoursServed, cid);
			log.warn(msg);
			throw new Exception("Unable to insert new unique user.");
		}

		User results = getJdbcTemplate().queryForObject(
				String.format("SELECT userId, username, totalHoursServed, contactId FROM users WHERE username = '%s'",
						username),
				new UserRowMapper());

		return results;
	}

	/**
	 * Deletes the given uid
	 */
	@Override
	public void delete(int uid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from users where userId= ?", new Object[] { uid });

		if (rc != 1) {
			String msg = String.format("unable to delete user [%s]", uid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	/**
	 * Asks for a new version of every variable to update
	 */
	@Override
	public void Update(int uid, String newUsername, double newHoursServed, int newContact) throws Exception {

		int rc = getJdbcTemplate().update(
				"update users set username = ?, totalHoursServed = ?, contactId = ? where userId = ?",
				new Object[] { newUsername, newHoursServed, newContact, uid });

		if (rc < 1) {
			log.error("unable to update [{}]", uid);
		}
	}

	/**
	 * Gets the user with the userId uid
	 */
	@Override
	public User fetchUserById(int uid) throws Exception {
		String sqlStr = String
				.format("select userId, username, totalHoursServed, contactId from users where userId = %d", uid);
		log.debug(sqlStr);

		List<User> results = getJdbcTemplate().query(sqlStr, new UserRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch reason [{}]", uid);
			return null;
		}

		return results.get(0);
	}

	/**
	 * Asks for an amount of hours served and then adds that to the current amount
	 */
	@Override
	public void AddHoursServed(int uid, double amount) throws Exception {

		List<User> uidUser = getJdbcTemplate().query(
				"select userId, username, totalHoursServed, contactId from users where userId = " + uid,
				new UserRowMapper());

		if (uidUser.size() != 1) {
			log.error("unable to update hoursServed [{}]", uid);
			return;
		}

		int rc = getJdbcTemplate().update("update users set username = ? where userId = ?",
				new Object[] { amount + uidUser.get(0).getTotalHoursServed(), uid });

	}

	/**
	 * Changes the username
	 */
	@Override
	public void changeUserName(int uid, String newUsername) throws Exception {
		int rc = getJdbcTemplate().update("update users set username = ? where userId = ?",
				new Object[] { newUsername, uid });

		if (rc < 1) {
			log.error("unable to update username [{}]", uid);
		}

	}

	/**
	 * Fetches the contact with the contactId that the UserId given holds
	 */
	@Override
	public Contact fetchUserContactById(int uid) throws Exception {

		JdbcTemplateContactDao contactDao = new JdbcTemplateContactDao();

		List<User> uidUser = getJdbcTemplate().query(
				"select userId, username, totalHoursServed, contactId from users where userId = " + uid,
				new UserRowMapper());

		if (uidUser.size() < 1) {
			return null;
		}

		Contact result = contactDao.fetchContactById(uidUser.get(0).getCid());

		return result;
	}

	public int size() {
		return getJdbcTemplate()
				.query("select userId, username, totalHoursServed, contactId from users", new UserRowMapper()).size();
	}

	class UserRowMapper implements RowMapper<User> {
		/**
		 * Returns the User in the given row
		 */
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {

			User us = new User().setUid(rs.getInt("userId")).setUserID(rs.getString("username"))
					.setTotalHoursServed(rs.getDouble("totalHoursServed")).setCid(rs.getInt("contactId"));

			return us;
		}
	}

}
