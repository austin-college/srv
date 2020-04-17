package srv.domain;

import java.util.List;

/**
 *  Data Access Object Interface for Contact.java that defines the standard operations
 *  (CRUD) to be performed on the Contact model object. 
 * @author Lydia House
 *
 */

public interface ContactDao {
	
	public List<Contact> listAll() throws Exception;
	
	public Contact create(String fn, String ln, String email, String work, String mobile, String str, String city, String st, String zip) throws Exception; 

	public void delete(int cid) throws Exception;
	
	public void update(int cid, String newVal) throws Exception;

	public Contact fetchContactById(int cid) throws Exception;

}
