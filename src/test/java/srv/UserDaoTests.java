package srv;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;

class UserDaoTests {

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

}
