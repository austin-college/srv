package srv.domain.serviceclient;

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

/**
 * The JDBC Template that implements the ServiceClient DAO (data access object)
 * interface. An instance of this class is responsible to get data from the
 * serviceClients table in the data.sql database. The methods this class
 * implement are creating a new service client query, updating an existing
 * service client query, deleting a service client query, and fetching a service
 * client query by its unique primary id (serviceClientId).
 * 
 * @author Lydia House
 *
 */

@ComponentScan("srv.config")
public class JdbcTemplateServiceClientDao  extends JdbcTemplateAbstractDao implements ServiceClientDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceClientDao.class);

	@Autowired
	JdbcTemplateContactDao dao;
	
	/**
	 * Default constructor.
	 */
	public JdbcTemplateServiceClientDao() {
		super();
	}

	/**
	 * Lists all the current service clients that are in the database.
	 */
	@Override
	public List<ServiceClient> listAll() throws Exception {

		List<ServiceClient> results = getJdbcTemplate()
				.query("select serviceClientId, title, primaryContactId, secondContactId, "
						+ "boardMem, category from serviceClients", new ServiceClientRowMapper());

		return results;

	}

	/**
	 * Creates a new ServiceClient query in the database. An exception is
	 * thrown if the new service client is a duplicate.
	 */
	@Override
	public ServiceClient create(String title, Integer cid1, Integer cid2, String bm, String cat) throws Exception {

		  final String sql = "INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) VALUES(?, ?, ?, ?, ?)";
			
		  final KeyHolder keyHolder = new GeneratedKeyHolder();

	      getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"serviceClientId"});
	                  ps.setString(1, title);
	                  ps.setInt(2, cid1);
	                  ps.setInt(3, cid2);
	                  ps.setString(4, bm);
	                  ps.setString(5, cat);
	                  return ps;
	              }, keyHolder);
		
	     Number num = keyHolder.getKey();
	     
		if (num == null ) {
			String msg = String.format("Unable to insert new service client [%s]", title);
			log.warn(msg);
			throw new Exception(msg);
		}
	   
	   log.debug("generated service client id is {}", num);
		
	   return this.fetchClientById((int)num);
	}


	/**
	 * Removes the desired Service Client (by id) from the database. An
	 * exception is thrown if the service client is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int scid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from serviceClients where serviceClientId= ?", new Object[] { scid });

		if (rc != 1) {
			String msg = String.format("unable to delete title [%s]", scid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	/**
	 * Updates the desired ServiceClient (by id) in the schema.sql database with the
	 * new specified content. An exception is thrown if the service client is unable
	 * to be updates (does not exist).
	 */
	@Override
	public void update(int scid, String name, Integer cid1, Integer cid2, String bm, String cat) throws Exception {
		int rc = getJdbcTemplate().update(
				"UPDATE serviceClients SET title = ?, primaryContactId = ?, "
						+ "secondContactId = ?, boardMem = ?, category = ? WHERE serviceClientId = ?",
				new Object[] { name, cid1, cid2, bm, cat, scid });

		if (rc < 1) {
			String msg = String.format("unable to update service client [%]", scid); 
			log.error(msg);
			throw new Exception(msg);
		}

	}

	/**
	 * Finds the corresponding ServiceClient given the specified id. An exception is
	 * thrown if the service client is unable to be fetched (does not exist).
	 */
	@Override
	public ServiceClient fetchClientById(int scid) throws Exception {

		String sqlStr = String.format("SELECT serviceClientId, title, primaryContactId, secondContactId, boardMem,"
				+ " category FROM serviceClients WHERE serviceClientId = %d", scid);
		log.debug(sqlStr);

		List<ServiceClient> results = getJdbcTemplate().query(sqlStr, new ServiceClientRowMapper());
	
		if (results.size() < 1) {
			log.error("unable to fetch servant client id [{}]", scid);
			return null;
		}

		return results.get(0);
	}

	/**
	 * This class maps a ServiceClient database record to the ServiceClient model object by using
	 * a RowMapper interface to fetch the records for a ServiceClient from the database.
	 */
	private class ServiceClientRowMapper implements RowMapper<ServiceClient> {
		@Override
		public ServiceClient mapRow(ResultSet rs, int rowNum) throws SQLException {

			ServiceClient sc = new ServiceClient();

			try {

				sc.setClientId(rs.getInt("serviceClientId")).setName(rs.getString("title"))
						.setMainContact(dao.fetchContactById(rs.getInt("primaryContactId")))
						.setOtherContact(dao.fetchContactById(rs.getInt("secondContactId")))
						.setBoardMember(rs.getString("boardMem"))
						.setCategory(rs.getString("category"));

			} catch (Exception e) {

				e.printStackTrace();
			}
			
			return sc;
		}
	}
}
