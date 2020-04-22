package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import srv.domain.contact.Contact;
import srv.domain.contact.ContactDao;
import srv.domain.contact.JdbcTemplateContactDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
public class ContactDaoTests {
	
	private static Logger log = LoggerFactory.getLogger(ContactDaoTests.class);
	
	@Autowired
	ContactDao dao;
	
	
	/*
	 * Testing fetchContactById() should return the first
	 * contact info in the list.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {
		
		log.warn("\n\n\n");
		
		
		
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
	
		
	/*
	 * Testing listAll(), should return the current 2 contact entries that are in the 
	 * data.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception { 
		
		
		List<Contact> contacts = dao.listAll();
		
		assertEquals(7, contacts.size());
		
		Contact c1 = contacts.get(0);
		Contact c2 = contacts.get(1);
		Contact c3 = contacts.get(2);
		Contact c4 = contacts.get(3);
		
		assertEquals(1, c1.getContactId());
		assertEquals(2, c2.getContactId());
		assertEquals(3, c3.getContactId());
		assertEquals(4, c4.getContactId());
		
		// Verifying contents of first Contact
		assertEquals("Tom", c1.getFirstName());
		assertEquals("Hanks", c1.getLastName());
		assertEquals("thanks@gmail.com", c1.getEmail());
		assertEquals("903-420-1212", c1.getPhoneNumWork());
		assertEquals("400-232-1211", c1.getPhoneNumMobile());
		assertEquals("626 E Main Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
		
		// Verifying contents of second Contact
		assertEquals("Lois", c2.getFirstName());
		assertEquals("Lane", c2.getLastName());
		assertEquals("llane86@gmail.com", c2.getEmail());
		assertEquals("803-423-1257", c2.getPhoneNumWork());
		assertEquals("800-232-1211", c2.getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", c2.getStreet());
		assertEquals("Sherman", c2.getCity());
		assertEquals("TX", c2.getState());
		assertEquals("75090", c2.getZipcode());
		
		// Verifying contents of third Contact
		assertEquals(3, c3.getContactId());
		assertEquals("Joe", c3.getFirstName());
		assertEquals("Smith", c3.getLastName());
		assertEquals("jsmith12@gmail.com", c3.getEmail());
		assertEquals("903-444-4440", c3.getPhoneNumWork());
		assertEquals("401-322-1201", c3.getPhoneNumMobile());
		assertEquals("25 Frieda Drive", c3.getStreet());
		assertEquals("Gunter", c3.getCity());
		assertEquals("TX", c3.getState());
		assertEquals("75058", c3.getZipcode());	
		
		// Verifying contents of fourth contact
		assertEquals(4, c4.getContactId());
		assertEquals("Susan", c4.getFirstName());
		assertEquals("Atkins", c4.getLastName());
		assertEquals("satkins67@gmail.com", c4.getEmail());
		assertEquals("803-426-1527", c4.getPhoneNumWork());
		assertEquals("800-191-9412", c4.getPhoneNumMobile());
		assertEquals("23 First Street", c4.getStreet());
		assertEquals("Denison", c4.getCity());
		assertEquals("TX", c4.getState());
		assertEquals("75021", c4.getZipcode());	
	}
	
	/* Testing the create(), should create a new Contact query in the 
	 * data.sql database.
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {
		
		log.warn("\n\n\n");
		
		Contact c = dao.create("Morgan", "Freeman", "mfreeman@msn.com", "902-412-6121", "392-121-5252", "626 Hayes Rd", "Sherman", "TX", "75090");

		Contact c8 = dao.fetchContactById(8);
		
		// Verifying that the Contact was stored in the database
		assertEquals(8, c8.getContactId());
		assertEquals("Morgan", c8.getFirstName());
		assertEquals("Freeman", c8.getLastName());
		assertEquals("mfreeman@msn.com", c8.getEmail());
		assertEquals("902-412-6121", c8.getPhoneNumWork());
		assertEquals("392-121-5252", c8.getPhoneNumMobile());
		assertEquals("626 Hayes Rd", c8.getStreet());
		assertEquals("Sherman", c8.getCity());
		assertEquals("TX", c8.getState());
		assertEquals("75090", c8.getZipcode());
		
		// Testing Contact returned from create
		assertEquals(8, c.getContactId());
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
		
		log.warn("\n\n\n");
			
		dao.delete(1);
		
		List<Contact> contacts = dao.listAll();
		
		assertEquals(6, contacts.size());
		
		Contact c1 = contacts.get(0);
		Contact c2 = contacts.get(1);
		Contact c3 = contacts.get(2);
		
		assertEquals(2, c1.getContactId());
		assertEquals(3, c2.getContactId());
		assertEquals(4, c3.getContactId());
		
		// Verifying contents of first Contact
		assertEquals("Lois", c1.getFirstName());
		assertEquals("Lane", c1.getLastName());
		assertEquals("llane86@gmail.com", c1.getEmail());
		assertEquals("803-423-1257", c1.getPhoneNumWork());
		assertEquals("800-232-1211", c1.getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
		
		// Verifying contents of second Contact
		assertEquals(3, c2.getContactId());
		assertEquals("Joe", c2.getFirstName());
		assertEquals("Smith", c2.getLastName());
		assertEquals("jsmith12@gmail.com", c2.getEmail());
		assertEquals("903-444-4440", c2.getPhoneNumWork());
		assertEquals("401-322-1201", c2.getPhoneNumMobile());
		assertEquals("25 Frieda Drive", c2.getStreet());
		assertEquals("Gunter", c2.getCity());
		assertEquals("TX", c2.getState());
		assertEquals("75058", c2.getZipcode());	

		// Verifying contents of third contact
		assertEquals(4, c3.getContactId());
		assertEquals("Susan", c3.getFirstName());
		assertEquals("Atkins", c3.getLastName());
		assertEquals("satkins67@gmail.com", c3.getEmail());
		assertEquals("803-426-1527", c3.getPhoneNumWork());
		assertEquals("800-191-9412", c3.getPhoneNumMobile());
		assertEquals("23 First Street", c3.getStreet());
		assertEquals("Denison", c3.getCity());
		assertEquals("TX", c3.getState());
		assertEquals("75021", c3.getZipcode());	
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {
		
		log.warn("\n\n\n");
		
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
