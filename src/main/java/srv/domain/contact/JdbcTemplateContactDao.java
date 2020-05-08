package srv.domain.contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import srv.domain.JdbcTemplateAbstractDao;

/**
 * The JDBC Template that implements the Contact DAO (data access object) interface.
 * An instance of this class is responsible to get data from the CONTACTS table in the schema.sql
 * database. The methods this class implements are creating a new contact query, updating an
 * existing contact query, deleting a contact query, and fetching a contact query by its unique
 * primary id (contactId). 
 * 
 * @author Lydia House
 *
 */
public class JdbcTemplateContactDao extends JdbcTemplateAbstractDao implements ContactDao  {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateContactDao.class);
	
	public JdbcTemplateContactDao() {
		super();
	}

	/*
	 * Lists all the current contacts that are in the schema.sql database.
	 */
	@Override
	public List<Contact> listAll() throws Exception {
		
		List<Contact> results = getJdbcTemplate().query("SELECT contactId, firstName, lastName, email, workPhone, mobilePhone, "
				+ "str, city, st, zip FROM contacts", new ContactRowMapper());
		 
		return results;
	}
	
	/*
	 * Creates a new Contact in the schema.sql database. An exception is thrown
	 * if the new contact is a duplicate. 
	 */
	@Override
	public Contact create(String fn, String ln, String email, String work, String mobile, String str, String city, String st, String zip) throws Exception {
		
		  final String sql = "INSERT INTO contacts (firstName, lastName, email, workPhone, mobilePhone, str, city, st, zip) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		  final KeyHolder keyHolder = new GeneratedKeyHolder();

		  /* 
		   * in the following code we are using java8's closure (lambda expression) feature .  It's like an anonymous inline 
		   * class definition of a listener.  JdbcTemplate update allows us to pass a snippet of code, given an 
		   * untyped parameter (connection).   Inside our code snippet, we can refer to the parameter by name. 
		   * Our snippet returns a prepared statement (which is what the JdbcTemplate.update method requires as 
		   * the first parameter.   The second parameter of the update method is a keyholder object that we can ask 
		   * for the database assigned auto number key value (a number).
		   * 
		   * Note: The preparedStatement's string array names the columns that are auto-number keys.   For the
		   * contact table, the auto number key is contactId.
		   * 
		   */
	      getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"contactId"});
	                  ps.setString(1, fn);
	                  ps.setString(2, ln);
	                  ps.setString(3, email);
	                  ps.setString(4, work);
	                  ps.setString(5, mobile);
	                  ps.setString(6, str);
	                  ps.setString(7, city);
	                  ps.setString(8, st);
	                  ps.setString(9, zip);
	                  return ps;
	              }, keyHolder);
		
	     Number num = keyHolder.getKey();
	     
		if (num == null ) {
			String msg = String.format("Unable to insert new contact [%s]", fn);
			log.warn(msg);
			throw new Exception("Unable to insert new unique contact.");
		}
	   
	   log.debug("generated id is {}", num);
		
	   return this.fetchContactById((int)num);
	   
	}

	/*
	 * Removes the desired Contact (by id) from the schema.sql database. An
	 * exception is thrown if the contact is unable to be removed (does not exist).
	 * 
	 * NOTE: in schema.sql we are setting references that have contact as a foreign key, to be null when a 
	 * contact is removed.
	 */
	@Override
	public void delete(int cid) throws Exception {
		
		int rc = getJdbcTemplate().update("DELETE FROM contacts WHERE contactId= ?", new Object[] { cid });
		
		if (rc != 1) {
			String msg = String.format("Unable to delete contact [%s]",cid);
			log.warn(msg);
			throw new Exception(msg);
		}
		
	}

	/* 
	 * Updates the desired Contact (by id) in the schema.sql database with the new 
	 * specified content. An exception is thrown if the contact is unable to be updated (does not exist).
	 */
	@Override
	public void update(int cid,  String newFn, String newLn, String newEmail, String newWork, String newMobile, String newStr, String newCity, String newSt, String newZip) throws Exception {
		
		int rc = getJdbcTemplate().update("UPDATE contacts SET firstName = ?, lastName = ?, email = ?, workPhone = ?,"
				+ "mobilePhone = ?, str = ?, city = ?, st = ?, zip = ? WHERE contactId = ?", 
				new Object[] {newFn, newLn, newEmail, newWork, newMobile, newStr, newCity, newSt, newZip, cid});

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
		
		String sqlStr = String.format("SELECT contactId, firstName, lastName, email, workPhone, mobilePhone, "
				+ "str, city, st, zip FROM contacts WHERE contactId = %d", cid);
		log.debug(sqlStr);
		
		List<Contact> results = getJdbcTemplate().query(sqlStr, new ContactRowMapper());
		
		if (results.size() != 1) {
			log.error("Unable to fetch contact [{}]", cid);
		}
		
		return results.get(0);
	}
	
	/**
	 * This class maps a Contact database record to the Contact model object by using
	 * a RowMapper interface to fetch the records for a Contact from the database.
	 */
	private class ContactRowMapper implements RowMapper <Contact> {
	    @Override
	    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {

	        Contact con = new Contact()
	        		.setContactId(rs.getInt("contactId"))
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
