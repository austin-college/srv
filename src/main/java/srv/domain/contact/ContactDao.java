package srv.domain.contact;

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
	
	public void update(int cid, String newFn, String newLn, String newEmail, String newWork, String newMobile, String newStr, String newCity, String newSt, String newZip) throws Exception;

	public Contact fetchContactById(int cid) throws Exception;

}
