package srv.domain;

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

public class JdbcTemplateContactDao implements ContactDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateContactDao.class);
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
                .addScript("contact.sql")
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

	public JdbcTemplateContactDao() {
		super();
	}

	/*
	 * Lists all the current contacts that are in the Contact.sql database.
	 */
	@Override
	public List<Contact> listAll() throws Exception {
		
		List<Contact> results = getJdbcTemplate().query("SELECT cid, firstName, lastName, email, workPhone, mobilePhone, "
				+ "str, city, st, zip FROM contacts", new ContactRowMapper());
		 
		return results;
	}
	
	/*
	 * Creates a new Contact in the Contact.sql database. An exception is thrown
	 * if the new contact is a duplicate. 
	 */
	@Override
	public Contact create(String fn, String ln, String email, String work, String mobile, String str, String city, String st, String zip) throws Exception {
		
		int rc = jdbcTemplate.update("INSERT INTO contacts (firstName, lastName, email,	workPhone,"
				+ " mobilePhone, str, city, st, zip) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {fn, ln, email, work, mobile, str, city, st, zip});

		if (rc != 1) {
			String msg = String.format("Unable to insert new contact [%s]", fn, ln, email, work, mobile, str, city, st, zip);
			log.warn(msg);
			throw new Exception("Unable to insert new unique contact.");
		}
	
		//TODO check this
	   Contact results = getJdbcTemplate().queryForObject(String.format("SELECT cid, firstName, lastName, email, workPhone, "
	   		+ "mobilePhone, str, city, st, zip FROM contacts WHERE firstName = '%s'", fn), new ContactRowMapper());
	   
	   return results;
	}

	/*
	 * Removes the desired Contact (by id) from the Contact.sql database. An
	 * exception is thrown if the contact is unable to be removed (does not exist).
	 */
	@Override
	public void delete(int cid) throws Exception {
		
		int rc = getJdbcTemplate().update("DELETE FROM contacts WHERE cid= ?", new Object[] { cid });
		
		if (rc != 1) {
			String msg = String.format("Unable to delete contact [%s]",cid);
			log.warn(msg);
			throw new Exception(msg);
		}
		
	}

	/*
	 * Updates the desired Contact (by id) in the Contact.sql database with the new 
	 * specified content. An exception is thrown if the contact is unable to be updates (does not exist).
	 */
	@Override
	public void update(int cid, String newVal) throws Exception {
		//TODO come check this
		int rc = getJdbcTemplate().update("UPDATE contacts SET firstName = ? WHERE cid = ?", new Object[] { newVal, cid });

		if (rc < 1) {
			log.error("Unable to update contact [{}]",cid);
		}
		
	}

	/*
	 * Finds the corresponding Contact given the specified id. An exception is thrown
	 * if the contact is unable to be fetched (does not exist).
	 */
	@Override
	public Contact fetchContactById(int cid) throws Exception {
		
		String sqlStr = String.format("SELECT cid, firstName, lastName, email, workPhone, mobilePhone, "
				+ "str, city, st, zip FROM contacts WHERE cid = %d", cid);
		log.debug(sqlStr);
		
		List<Contact> results = getJdbcTemplate().query(sqlStr, new ContactRowMapper());
		
		if (results.size() != 1) {
			log.error("Unable to fetch contact [{}]", cid);
		}
		
		return results.get(0);
	}
	
	private class ContactRowMapper implements RowMapper <Contact> {
	    @Override
	    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {

	        Contact con = new Contact()
	        		.setContactId(rs.getInt("cid"))
	        		.setFirstName(rs.getString("firstName"))
	        		.setLastName(rs.getString("lastName"))
	        		.setEmail(rs.getString("email"))
	        		.setPhoneNumWork(rs.getString("workPhone"))
	        		.setPhoneNumMobile(rs.getString("mobilePhone"))
	        		.setStreet(rs.getString("str"))
	        		.setCity(rs.getString("city"))
	        		.setState(rs.getString("st"))
	        		.setZipcode(rs.getString("zip"));
	        
	        return con;
	    }
	}

}
