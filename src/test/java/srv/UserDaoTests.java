package srv;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

class UserDaoTests {

	/*
	 * Testing fetchUserById() should return the first user info in the list.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		User c1 = dao.fetchUserById(1);

		assertEquals(1, c1.getUid());

		assertEquals("apritchard", c1.getUserID());
		assertEquals("1234", c1.getPassword());
		assertEquals(0.0, c1.getTotalHoursServed());
		assertEquals(4, c1.getCid());

		User c2 = dao.fetchUserById(2);

		assertEquals(2, c2.getUid());

		assertEquals("hCouturier", c2.getUserID());
		assertEquals("5678", c2.getPassword());
		assertEquals(0.0, c2.getTotalHoursServed());
		assertEquals(5, c2.getCid());

		User c3 = dao.fetchUserById(3);

		assertEquals(3, c3.getUid());

		assertEquals("eDriscoll", c3.getUserID());
		assertEquals("1234", c3.getPassword());
		assertEquals(0.0, c3.getTotalHoursServed());
		assertEquals(6, c3.getCid());
		
		// TODO Get contact info

	}

}
