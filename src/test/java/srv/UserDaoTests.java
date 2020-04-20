package srv;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

class UserDaoTests {

	/*
	 * Testing fetchUserById() should return the first user info in the list.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		User u1 = dao.fetchUserById(1);

		assertEquals(1, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals("1234", u1.getPassword());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		User u2 = dao.fetchUserById(2);

		assertEquals(2, u2.getUid());

		assertEquals("hCouturier", u2.getUserID());
		assertEquals("5678", u2.getPassword());
		assertEquals(0.0, u2.getTotalHoursServed());
		assertEquals(5, u2.getCid());

		User u3 = dao.fetchUserById(3);

		assertEquals(3, u3.getUid());

		assertEquals("eDriscoll", u3.getUserID());
		assertEquals("1234", u3.getPassword());
		assertEquals(0.0, u3.getTotalHoursServed());
		assertEquals(6, u3.getCid());

		// TODO Get contact info

	}

	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		List<User> users = dao.listAll();

		assertEquals(1, users.get(0).getUid());

		assertEquals("apritchard", users.get(0).getUserID());
		assertEquals("1234", users.get(0).getPassword());
		assertEquals(0.0, users.get(0).getTotalHoursServed());
		assertEquals(4, users.get(0).getCid());

		assertEquals(2, users.get(1).getUid());

		assertEquals("hCouturier", users.get(1).getUserID());
		assertEquals("5678", users.get(1).getPassword());
		assertEquals(0.0, users.get(1).getTotalHoursServed());
		assertEquals(5, users.get(1).getCid());

		assertEquals(3, users.get(2).getUid());

		assertEquals("eDriscoll", users.get(2).getUserID());
		assertEquals("1234", users.get(2).getPassword());
		assertEquals(0.0, users.get(2).getTotalHoursServed());
		assertEquals(6, users.get(2).getCid());
	}

	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		// if this isn't here i get a null pointer exception so i have no idea whats up
		User u = dao.fetchUserById(1);

		// System.err.println("Size of list before create is " + dao.listAll().size());

		User u1 = dao.create("lHouse", "5678", 0, 4);
		User u3 = dao.create("mHiggs", "1234", 0, 5);

		User u2 = dao.fetchUserById(4);

		assertEquals("lHouse", u2.getUserID());
		assertEquals("5678", u2.getPassword());
		assertEquals(0.0, u2.getTotalHoursServed());
		assertEquals(4, u2.getCid());

		User u4 = dao.fetchUserById(5);

		assertEquals("mHiggs", u4.getUserID());
		assertEquals("1234", u4.getPassword());
		assertEquals(0.0, u4.getTotalHoursServed());
		assertEquals(5, u4.getCid());
	}

	@Test
	void testGetUsersContact_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		User u1 = dao.fetchUserById(1);

		assertEquals(1, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals("1234", u1.getPassword());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		Contact c1 = dao.fetchUserContactById(u1.getUid());

		System.err.println("C1 WORKS : " + (c1 != null));

		assertEquals(u1.getCid(), c1.getContactId());
	}

	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		User u1 = dao.fetchUserById(1);

		assertEquals(1, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals("1234", u1.getPassword());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		assertEquals(3, dao.listAll().size());

		dao.delete(1);

		assertEquals(null, dao.fetchUserById(1));
		assertEquals(2, dao.listAll().size());

	}
}
