package srv.domain.serviceGroup;

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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.contact.JdbcTemplateContactDao;

@Repository
public class JdbcTemplateServiceGroupDao extends JdbcTemplateAbstractDao implements ServiceGroupDao {
		
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceGroupDao.class);
	
	@Autowired
	private JdbcTemplateContactDao contactDao;
	
	// Default Constructor
	public JdbcTemplateServiceGroupDao() {
		super();
	}
	
	/*
	 * Lists all the current Service Groups that are in the data.sql database.
	 */
	@Override
	public List<ServiceGroup> listAll() throws Exception {

		List<ServiceGroup> results = getJdbcTemplate()
				.query("SELECT serviceGroupId, shortName, title, contactId FROM serviceGroups", new ServiceGroupRowMapper());
		
		return results;
	}

	/*
	 * Creates a new ServiceGroup in the data.sql database.
	 * An exception is thrown if the new ServiceGroup is a duplicate. 
	 */
	@Override
	public ServiceGroup create(String shortName, String title, int cid) throws Exception {
		
		// SQL statement that is to be executed
		final String sql = "INSERT INTO serviceGroups (shortName, title, contactId) VALUES(?, ?, ?)";
		
		final KeyHolder keyHolder = new GeneratedKeyHolder();
		
		// fills in the SQL statements ?'s
	    getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"serviceGroupId"});
	                  ps.setString(1, shortName);
	                  ps.setString(2, title);
	                  ps.setInt(3, cid);
	                  return ps;
	              }, keyHolder);
		
	    Number num = keyHolder.getKey();
	    
	    if (num == null ) {
			String msg = String.format("Unable to insert new serviceGroup [%s]", shortName);
			log.warn(msg);
			throw new Exception("Unable to insert new unique serviceGroup.");
		}
	   
	   log.debug("generated service group id is {}", num);
		
	   return this.fetchServiceGroupById((int)num);
	}
	
	/*
	 * Removes the desired ServiceGroup (by id) from the data.sql database. 
	 * An exception is thrown if the serviceGroup is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int sgid) throws Exception {

		int rc = getJdbcTemplate().update("DELETE FROM serviceGroups WHERE serviceGroupId = ?", new Object[] {sgid} );
		
		if (rc != 1) {
			String msg = String.format("Unable to delete serviceGroup [%s]", sgid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}

	/* 
	 * Updates the desired ServiceGroup (by id) in the data.sql database with the new specified content. 
	 * An exception is thrown if the ServiceGroup is unable to be updates (does not exist).
	 */
	@Override
	public void update(int sgid, String shortName, String title, int cid) throws Exception {
		
		// FLAG
		int rc = getJdbcTemplate().update(
				"UPDATE serviceGroups SET shortName = ?, title = ?, contactId = ? WHERE serviceGroupId = ?",
				new Object[] {shortName, title, cid, sgid});
		
		if (rc < 1) {
			String msg = String.format("Unable to update serviceGroup [%]", sgid);
			log.error(msg);
			throw new Exception(msg);
		}
		
	}

	/*
	 * Finds the corresponding ServiceGroup given the specified id. 
	 * An exception is thrown if the ServiceGroup is unable to be fetched (does not exist).
	 */
	@Override
	public ServiceGroup fetchServiceGroupById(int sgid) throws Exception {

		String sqlStr = String.format("SELECT serviceGroupId, shortName, title, contactId"
				+ " FROM serviceGroups WHERE serviceGroupId = %d", sgid);
		log.debug(sqlStr);
		
		List<ServiceGroup> results = getJdbcTemplate().query(sqlStr, new ServiceGroupRowMapper());
		
		if (results.size() < 1) {
			log.error("Unable to fetch service group id [{}]", sgid);
			return null;
		}
		
		return results.get(0);
	}
	
	private class ServiceGroupRowMapper implements RowMapper<ServiceGroup> {

		@Override
		public ServiceGroup mapRow(ResultSet rs, int rowNum) throws SQLException {

			ServiceGroup sg = new ServiceGroup();
			
			try {
				sg.setSgid(rs.getInt("serviceGroupId"))
				.setShortName(rs.getString("shortName"))
				.setTitle(rs.getString("title"))
				.setContactInfo(contactDao.fetchContactById(rs.getInt("contactId")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return sg;
		}
		
	}
	

}
