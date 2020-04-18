package srv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;

import org.springframework.jdbc.core.JdbcTemplate;


public class ContactDaoTests {
	
	/*
	 * Testing fetchContactById() should return the first
	 * contact info in the list.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		Contact c1 = dao.fetchContactById(1);
		
		assertEquals(1, c1.getContactId());
		assertEquals("Tom", c1.getFirstName());
		assertEquals("Hanks", c1.getLastName());
		assertEquals("thanks@gmail.com", c1.getEmail());
		assertEquals("903-420-1212", c1.getPhoneNumWork());
		assertEquals("400-232-1211", c1.getPhoneNumMobile());
		assertEquals("626 E Main Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
	}
	
	/* TODO how does one tests exceptions - think need to properly set up exception thingy in template
	 * 
	 * Testing fetchContactById() when the specified contact isn't in the list,
	 * should throw an exception.
	 */
	@Test
	void testFetchByIdExceptionThrown_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
	//	Contact c1 = dao.fetchContactById(3);
		
	//	assertEquals(1, c1.getContactId());
	}
	
	/*
	 * Testing listAll(), should return the current 2 contact entries that are in the 
	 * Contact.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception { 
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		List<Contact> contacts = dao.listAll();
		
		assertEquals(2, contacts.size());
		
		Contact c1 = contacts.get(0);
		Contact c2 = contacts.get(1);
		
		assertEquals(1, c1.getContactId());
		assertEquals(2, c2.getContactId());
		
		assertEquals("Tom", c1.getFirstName());
		assertEquals("Hanks", c1.getLastName());
		assertEquals("thanks@gmail.com", c1.getEmail());
		assertEquals("903-420-1212", c1.getPhoneNumWork());
		assertEquals("400-232-1211", c1.getPhoneNumMobile());
		assertEquals("626 E Main Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
		
		assertEquals("Lois", c2.getFirstName());
		assertEquals("Lane", c2.getLastName());
		assertEquals("llane86@gmail.com", c2.getEmail());
		assertEquals("803-423-1257", c2.getPhoneNumWork());
		assertEquals("800-232-1211", c2.getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", c2.getStreet());
		assertEquals("Sherman", c2.getCity());
		assertEquals("TX", c2.getState());
		assertEquals("75090", c2.getZipcode());
	}
	
	/* Testing the create(), should create a new query in the 
	 * Contact.sql database.
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		// if this isn't here i get a null pointer exception so i have no idea whats up
		Contact c1 = dao.fetchContactById(1);
		
		Contact c = dao.create("Morgan", "Freeman", "mfreeman@msn.com", "902-412-6121", "392-121-5252", "626 Hayes Rd", "Sherman", "TX", "75090");

		Contact c3 = dao.fetchContactById(3);
		
		assertEquals(3, c3.getContactId());
		assertEquals("Morgan", c3.getFirstName());
		assertEquals("Freeman", c3.getLastName());
		assertEquals("mfreeman@msn.com", c3.getEmail());
		assertEquals("902-412-6121", c3.getPhoneNumWork());
		assertEquals("392-121-5252", c3.getPhoneNumMobile());
		assertEquals("626 Hayes Rd", c3.getStreet());
		assertEquals("Sherman", c3.getCity());
		assertEquals("TX", c3.getState());
		assertEquals("75090", c3.getZipcode());
		
		assertEquals(3, c.getContactId());
		assertEquals("Morgan", c.getFirstName());
		assertEquals("Freeman", c.getLastName());
		assertEquals("mfreeman@msn.com", c.getEmail());
		assertEquals("902-412-6121", c.getPhoneNumWork());
		assertEquals("392-121-5252", c.getPhoneNumMobile());
		assertEquals("626 Hayes Rd", c.getStreet());
		assertEquals("Sherman", c.getCity());
		assertEquals("TX", c.getState());
		assertEquals("75090", c.getZipcode());
	}
	
	/*
	 *  Testing the delete(), should remove the query with the specified ID (first one in this case). Should
	 *  still be one query left in the database.
	 */
	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
			
		dao.delete(1);
		
		List<Contact> contacts = dao.listAll();
		
		assertEquals(1, contacts.size());
		
		Contact c1 = contacts.get(0);
		
		assertEquals(2, c1.getContactId());
		
		assertEquals("Lois", c1.getFirstName());
		assertEquals("Lane", c1.getLastName());
		assertEquals("llane86@gmail.com", c1.getEmail());
		assertEquals("803-423-1257", c1.getPhoneNumWork());
		assertEquals("800-232-1211", c1.getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
		
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		dao.update(1, "Tom", "Cruise", "tcruise@msn.com", "901-121-1211", "800-522-5291", "626 E Main Street", "Sherman", "TX", "75090");
		
		Contact c1 = dao.fetchContactById(1);
		
		assertEquals(1, c1.getContactId());
		assertEquals("Tom", c1.getFirstName());
		assertEquals("Cruise", c1.getLastName());
		assertEquals("tcruise@msn.com", c1.getEmail());
		assertEquals("901-121-1211", c1.getPhoneNumWork());
		assertEquals("800-522-5291", c1.getPhoneNumMobile());
		assertEquals("626 E Main Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
	}
	
	
}
