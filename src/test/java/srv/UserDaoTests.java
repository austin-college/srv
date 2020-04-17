package srv;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

class UserDaoTests {

	/*
	 * Testing fetchUserById() should return the first
	 * user info in the list.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();
		
		User c1 = dao.fetchUserById(1);
		
		assertEquals(1, c1.getUid());
		
		assertEquals("apritchard", c1.getUserID());
		assertEquals("1234", c1.getPassword());
		assertEquals("0", c1.getTotalHoursServed());
		//assertEquals("", c1.getCid()); // TODO
		
	}

}
