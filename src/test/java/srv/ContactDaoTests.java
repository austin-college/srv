package srv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import srv.domain.Contact;
import srv.domain.JdbcTemplateContactDao;



public class ContactDaoTests {
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {
		JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
		
		Contact c1 = dao.fetchContactById(1);
		
		assertEquals(1, c1.getContactId());
		
		assertEquals("Tom", c1.getFirstName());
		assertEquals("Hanks", c1.getLastName());
		assertEquals("thanks@gmail.com", c1.getEmail());
		assertEquals("903-420-121", c1.getPhoneNumWork());
		assertEquals("400-232-121", c1.getPhoneNumMobile());
		assertEquals("626 E Main Street", c1.getStreet());
		assertEquals("Sherman", c1.getCity());
		assertEquals("TX", c1.getState());
		assertEquals("75090", c1.getZipcode());
		
	}
}
