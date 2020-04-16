package srv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import srv.domain.Contact;
import srv.domain.JdbcTemplateContactDao;



public class ContactDaoTests {
	
	/*
	 * Testing fetchContactById() should return the first
	 * contact infoin the list.
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
	
	/* TODO how does one tests exceptions
	 * Testing fetchContactById() when the specified contact isn't in the list,
	 * should throw an exception.
	 */
	@Test
	void testFetchByIdExceptionThrown_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		//Contact c1 = dao.fetchContactById(3);
		
		//assertEquals(1, c1.getContactId());
	}
	
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
	
	
}
