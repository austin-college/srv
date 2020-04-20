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
 * 
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

	@Override
	public List<User> listAll() throws Exception {
		List<User> results = getJdbcTemplate().query(
				"select userId, username, password, totalHoursServed, contactId from users", new UserRowMapper());

		return results;
	}

	@Override
	public User create(String username, String password, double totalHoursServed, int cid) throws Exception {

		// update executes the sql script that is in its paramaters. The sequel script
		// inserts a new user into the database
		int rc = jdbcTemplate.update("insert into users (username, password, totalHoursServed, contactId) values ( '"
				+ username + "', '" + password + "', " + totalHoursServed
				+ ", (select contactId from contacts where contactId = " + cid + "))");

		if (rc != 1) {
			String msg = String.format("Unable to insert new user [%s]", username, password, totalHoursServed, cid);
			log.warn(msg);
			throw new Exception("Unable to insert new unique user.");
		}

		User results = getJdbcTemplate().queryForObject(String.format(
				"SELECT userId, username, password, totalHoursServed, contactId FROM users WHERE username = '%s'",
				username), new UserRowMapper());

		return results;
	}

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
	 * Change Password method
	 */
	public void changePassword(int uid, String newPassword) throws Exception {
		int rc = getJdbcTemplate().update("update users set password = ? where userId = ?",
				new Object[] { newPassword, uid });

		if (rc < 1) {
			log.error("unable to update passord [{}]", uid);
		}
	}

	@Override
	public void Update(int uid, String newUsername, String newPassword, double newHoursServed, int newContact)
			throws Exception {

		int rc = getJdbcTemplate().update(
				"update users set username = ?, password = ?, totalHoursServed = ?, contactId = ? where userId = ?",
				new Object[] { newUsername, newPassword, newHoursServed, newContact, uid });

		if (rc < 1) {
			log.error("unable to update [{}]", uid);
		}
	}

	@Override
	public User fetchUserById(int uid) throws Exception {
		String sqlStr = String.format(
				"select userId, username, password, totalHoursServed, contactId from users where userId = %d", uid);
		log.debug(sqlStr);

		List<User> results = getJdbcTemplate().query(sqlStr, new UserRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch reason [{}]", uid);
			return null;
		}

		return results.get(0);
	}

	@Override
	public void AddHoursServed(int uid, double amount) throws Exception {

		List<User> uidUser = getJdbcTemplate().query(
				"select userId, username, password, totalHoursServed, contactId from users where userId = " + uid,
				new UserRowMapper());

		if (uidUser.size() != 1) {
			log.error("unable to update hoursServed [{}]", uid);
			return;
		}

		int rc = getJdbcTemplate().update("update users set username = ? where userId = ?",
				new Object[] { amount + uidUser.get(0).getTotalHoursServed(), uid });

	}

	@Override
	public void changeUserName(int uid, String newUsername) throws Exception {
		int rc = getJdbcTemplate().update("update users set username = ? where userId = ?",
				new Object[] { newUsername, uid });

		if (rc < 1) {
			log.error("unable to update username [{}]", uid);
		}

	}

	@Override
	public Contact fetchUserContactById(int uid) throws Exception {

		JdbcTemplateContactDao contactDao = new JdbcTemplateContactDao();

		List<User> uidUser = getJdbcTemplate().query(
				"select userId, username, password, totalHoursServed, contactId from users where userId = " + uid,
				new UserRowMapper());

		if (uidUser.size() < 1) {
			return null;
		}

		Contact result = contactDao.fetchContactById(uidUser.get(0).getCid());

		return result;
	}

	public int size() {
		return getJdbcTemplate()
				.query("select userId, username, password, totalHoursServed, contactId from users", new UserRowMapper())
				.size();
	}

	class UserRowMapper implements RowMapper<User> {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {

			User us = new User().setUid(rs.getInt("userId")).setUserID(rs.getString("username"))
					.setPassword(rs.getString("password")).setTotalHoursServed(rs.getDouble("totalHoursServed"))
					.setCid(rs.getInt("contactId"));

			return us;
		}
	}

}
