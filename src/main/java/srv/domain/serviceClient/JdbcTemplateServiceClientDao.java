package srv.domain.serviceClient;
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
 * The JDBC Template that implements the ServiceClient DAO (data access object) interface.
 * An instance of this class is responsible to get data from the serviceClients table in the data.sql
 * database. The methods this class implements are creating a new service client query, updating an
 * existing service client query, deleting a service client query, and fetching a service client query by its unique
 * primary id (serviceClientId). 
 * 
 * @author Lydia House
 *
 */
public class JdbcTemplateServiceClientDao implements ServiceClientDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceClientDao.class);

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
	    
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("data.sql")
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

	public JdbcTemplateServiceClientDao() {
		super();
	}

	/*
	 * Lists all the current service clients that are in the data.sql database.
	 */
	@Override
	public List<ServiceClient> listAll() throws Exception {
		
		List<ServiceClient> results = getJdbcTemplate().query("select serviceClientId, title, primaryContactId, secondContactId, "
				+ "boardMem, category from serviceClients", new ServiceClientRowMapper());
		 
	   return results;
		
	}
	
	/*
	 * Creates a new ServiceClient query in the data.sql database. An exception is thrown
	 * if the new service client is a duplicate. 
	 */
	@Override
	public ServiceClient create(String name, Integer cid1, Integer cid2, String bm, String cat) throws Exception {
		
			int rc = jdbcTemplate.update("INSERT INTO serviceClients (title, primaryContactId, secondContactId, boardMem, category) "
					+ "VALUES(?, ?, ?, ?, ?)", new Object[] {name, cid1, cid2, bm, cat});
			
			if (rc != 1) {
				String msg = String.format("unable to insert new title [%s]", name);
				log.warn(msg);
				throw new Exception("Unable insert new unique title. Maybe a duplicate?");
			}

			ServiceClient results = getJdbcTemplate().queryForObject(String.format("SELECT serviceClientId, title, primaryContactId, "
					+ "secondContactId, boardMem, category FROM serviceClients WHERE title = '%s'", name), new ServiceClientRowMapper());
	   
	   return results;
	}
	
	
	/*
	 * Removes the desired Service Client (by id) from the data.sql database. An
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
	
	/* 
	 * Updates the desired ServiceClient (by id) in the data.sql database with the new 
	 * specified content. An exception is thrown if the service client is unable to be updates (does not exist).
	 */
	@Override
	public void update(int scid, String name, Integer cid1, Integer cid2, String bm, String cat) throws Exception {
		int rc = getJdbcTemplate().update("UPDATE serviceClients SET title = ?, primaryContactId = ?, "
				+ "secondContactId = ?, boardMem = ?, category = ? WHERE serviceClientId = ?", new Object[] {name, cid1, cid2, bm, cat, scid});

		if (rc < 1) {
			log.error("unable to update title [{}]",scid);
		}

	}

	/*
	 * Finds the corresponding ServiceClient given the specified id. An exception is thrown
	 * if the service client is unable to be fetched (does not exist).
	 */
	@Override
	public ServiceClient fetchClientId(int scid) throws Exception {
		
		String sqlStr = String.format("SELECT serviceClientId, title, primaryContactId, secondContactId, boardMem,"
				+ " category FROM serviceClients WHERE serviceClientId = %d",scid);
		log.debug(sqlStr);
		
		List<ServiceClient> results = getJdbcTemplate().query(sqlStr, new ServiceClientRowMapper());
		
		if (results.size() != 1) {
			log.error("unable to fetch servant client id [{}]",scid);
		}
		return results.get(0);
	}

	
	
	private class ServiceClientRowMapper implements RowMapper <ServiceClient> {
	    @Override
	    public ServiceClient mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	
	    	/*
	    	 * We use the ContactDao in order to access the contacts table in the data.sql 
	    	 * database, so that the service client has a handle on that contact.
	    	 */
	    	JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
	    	ServiceClient sc = new ServiceClient();
	    	
	    	try {				
		
	    		sc.getClientId(rs.getInt("serviceClientId"))
		        	.setName(rs.getString("title"))
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
