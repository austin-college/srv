package srv.domain.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.JdbcTemplateContactDao;

/**
 * 
 * The JDBC Template that implements the User DAO (data access object) interface.
 * An instance of this class is responsible to get data from the users table in the schema.sql
 * database. The methods this class implements are creating a new user query, updating an
 * existing user query, deleting a user query, and fetching a user query by its unique
 * primary id (user) and their username. 
 * 
 * @author AJ Pritchard
 *
 */
public class JdbcTemplateUserDao extends JdbcTemplateAbstractDao implements UserDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateUserDao.class);

	@Autowired
	private JdbcTemplateContactDao dao;

	public JdbcTemplateUserDao() {
		super();
	}

	/*
	 * Returns a list of all users
	 */
	@Override
	public List<User> listAll() throws Exception {
		List<User> results = getJdbcTemplate().query("select userId, username, contactId from users",
				new UserRowMapper());

		return results;
	}

	/*
	 * Creates a new User and adds it to the database
	 */
	@Override
	public User create(String username, int cid) throws Exception {

		// sql is the sql statement that we are sending to the server
		final String sql = "INSERT INTO users (username, contactId) VALUES(?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// this prepared statement fills in the ?'s in the sql statement with our data
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "userId" });
			ps.setString(1, username);
			ps.setInt(2, cid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		// checks to see if the sql statement correctly ran
		if (num == null) {
			String msg = String.format("Unable to insert new user [%s]", username);
			log.warn(msg);
			throw new Exception(msg);
		}

		log.debug("generated user id is {}", num);

		return this.fetchUserById((int) num);
	}

	/*
	 * Deletes the given uid
	 * 
	 * NOTE: Also deletes the associated ServantUser
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

	/*
	 * Asks for a new version of every variable to update
	 */
	@Override
	public void update(int uid, String newUsername, int newContact) throws Exception {

		// the sequel statement
		final String sql = "update users set username = ?, contactId = ? where userId = ?";

		final KeyHolder keyHolder = new GeneratedKeyHolder();

		// this prepared statement fills in the ?'s in the sql statement with our data
		getJdbcTemplate().update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] { "userId" });
			ps.setString(1, newUsername);
			ps.setInt(2, newContact);
			ps.setInt(3, uid);
			return ps;
		}, keyHolder);

		Number num = keyHolder.getKey();

		if (num == null) {
			log.error("unable to update [{}]", uid);
		}
	}

	/**
	 * Gets the user with the userId uid
	 */
	@Override
	public User fetchUserById(int uid) throws Exception {
		String sqlStr = String.format("select userId, username, contactId from users where userId = %d", uid);
		log.debug(sqlStr);

		List<User> results = getJdbcTemplate().query(sqlStr, new UserRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch UserID [{}]", uid);
			return null;
		}

		return results.get(0);
	}
	/*
	 * Gets the user with the userName uName
	 */
	@Override
	public User fetchUserByUsername(String uName) throws Exception {
		String sqlStr = String.format("select userId, username, contactId from users where username = '%s'", uName);
		log.debug(sqlStr);

		List<User> results = getJdbcTemplate().query(sqlStr, new UserRowMapper());

		if (results.size() != 1) {
			log.error("unable to fetch Username [{}]", uName);
			return null;
		}

		return results.get(0);
	}

	/*
	 * Changes the username
	 */
	@Override
	public void changeUsername(int uid, String newUsername) throws Exception {
		int rc = getJdbcTemplate().update("update users set username = ? where userId = ?",
				new Object[] { newUsername, uid });

		if (rc < 1) {
			log.error("unable to update username [{}]", uid);
		}

	}

	/**
	 * This class maps a User database record to the User model object by using
	 * a RowMapper interface to fetch the records for a User from the database.
	 */
	class UserRowMapper implements RowMapper<User> {
		/**
		 * Returns the User in the given row
		 */
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {

			User sc = new User();

			try {

				sc.setUid(rs.getInt("userId")).setUsername(rs.getString("username"))
						.setContactInfo(dao.fetchContactById(rs.getInt("contactId")));

			} catch (Exception e) {

				e.printStackTrace();
			}

			return sc;
		}
	}

}
