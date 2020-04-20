package srv;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import srv.domain.contact.Contact;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

class UserDaoTests {

	/*
	 * Testing fetchUserById(int i) should return the user info for the user with id
	 * i.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		// test that u1 can be fetched
		int id1 = 1;
		User u1 = dao.fetchUserById(id1);

		assertEquals(id1, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		// test that u2 can be fetched
		int id2 = 2;
		User u2 = dao.fetchUserById(id2);

		assertEquals(id2, u2.getUid());

		assertEquals("hCouturier", u2.getUserID());
		assertEquals(0.0, u2.getTotalHoursServed());
		assertEquals(5, u2.getCid());

		// test that u3 can be fetched
		int id3 = 3;
		User u3 = dao.fetchUserById(id3);

		assertEquals(id3, u3.getUid());

		assertEquals("eDriscoll", u3.getUserID());
		assertEquals(0.0, u3.getTotalHoursServed());
		assertEquals(6, u3.getCid());
	}

	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		List<User> users = dao.listAll();

		assertEquals(1, users.get(0).getUid());

		assertEquals("apritchard", users.get(0).getUserID());
		assertEquals(0.0, users.get(0).getTotalHoursServed());
		assertEquals(4, users.get(0).getCid());

		assertEquals(2, users.get(1).getUid());

		assertEquals("hCouturier", users.get(1).getUserID());
		assertEquals(0.0, users.get(1).getTotalHoursServed());
		assertEquals(5, users.get(1).getCid());

		assertEquals(3, users.get(2).getUid());

		assertEquals("eDriscoll", users.get(2).getUserID());
		assertEquals(0.0, users.get(2).getTotalHoursServed());
		assertEquals(6, users.get(2).getCid());
	}

	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		// if this isn't here i get a null pointer exception so i have no idea whats up
		// User u = dao.fetchUserById(1);

		// System.err.println("Size of list before create is " + dao.listAll().size());

		User u1 = dao.create("lHouse", 0, 4);
		User u3 = dao.create("mHiggs", 0, 5);

		User u2 = dao.fetchUserById(u1.getUid());

		assertEquals(u1.getUserID(), u2.getUserID());
		assertEquals(u1.getTotalHoursServed(), u2.getTotalHoursServed());
		assertEquals(u1.getCid(), u2.getCid());

		User u4 = dao.fetchUserById(u3.getUid());

		assertEquals(u3.getUserID(), u4.getUserID());
		assertEquals(u3.getTotalHoursServed(), u4.getTotalHoursServed());
		assertEquals(u3.getCid(), u4.getCid());
	}

	@Test
	void testGetUsersContact_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		int id = 1;
		User u1 = dao.fetchUserById(id);

		assertEquals(id, u1.getUid());

		// verifies that user works
		assertEquals("apritchard", u1.getUserID());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		Contact c1 = dao.fetchUserContactById(u1.getUid());

		// System.err.println("C1 WORKS : " + (c1 != null));

		// tests that you can get the contact
		assertEquals(u1.getCid(), c1.getContactId());
	}

	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		// checks to see that user with id 1 exists then
		User u1 = dao.fetchUserById(1);

		assertEquals(1, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		assertEquals(3, dao.listAll().size());

		// deletes user with id 1
		dao.delete(1);

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(1));
		assertEquals(2, dao.listAll().size());

		// checks user with id 2 exists
		u1 = dao.fetchUserById(2);

		assertEquals(2, u1.getUid());

		assertEquals("hCouturier", u1.getUserID());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(5, u1.getCid());

		assertEquals(2, dao.listAll().size());

		// deletes user with id 2
		dao.delete(2);

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(2));
		assertEquals(1, dao.listAll().size());
	}

	@Test
	void testDeleteNewlyCreated_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		int originalSizeOfUserArray = dao.listAll().size();

		User u = dao.create("NewPerson", 0, 4);

		// checks to see that user with id 1 exists then
		User u1 = dao.fetchUserById(u.getUid());

		assertEquals(u.getUid(), u1.getUid());

		assertEquals(u.getUserID(), u1.getUserID());
		assertEquals(u.getTotalHoursServed(), u1.getTotalHoursServed());
		assertEquals(u.getCid(), u1.getCid());

		assertEquals(originalSizeOfUserArray + 1, dao.listAll().size());

		// deletes user with id 1
		dao.delete(u.getUid());

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(u.getUid()));
		assertEquals(originalSizeOfUserArray, dao.listAll().size());
	}

	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {

		JdbcTemplateUserDao dao = new JdbcTemplateUserDao();

		int id = 1;
		User u1 = dao.fetchUserById(id);

		assertEquals(id, u1.getUid());

		assertEquals("apritchard", u1.getUserID());
		assertEquals(0.0, u1.getTotalHoursServed());
		assertEquals(4, u1.getCid());

		String newUsername = "new username";
		double newHours = 1;
		int newContact = 5;

		// update each item
		dao.Update(id, newUsername, newHours, newContact);

		u1 = dao.fetchUserById(id);

		assertEquals(newUsername, u1.getUserID());
		assertEquals(newHours, u1.getTotalHoursServed());
		assertEquals(newContact, u1.getCid());
	}
}
