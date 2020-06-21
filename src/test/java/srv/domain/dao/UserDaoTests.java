package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class UserDaoTests {

	@Autowired
	UserDao dao;

	@Autowired
	ServantUserDao srvUserDao;
	
	/*
	 * Testing fetchUserById(int i) should return the user info for the user with id
	 * i.
	 */
	@Test
	void testFetchById() throws Exception {

		// test that u1 can be fetched
		int id1 = 1;
		User u1 = dao.fetchUserById(id1);

		assertEquals(id1, u1.getUid());

		assertEquals("apritchard", u1.getUsername());

		// test that u2 can be fetched
		int id2 = 2;
		User u2 = dao.fetchUserById(id2);

		assertEquals(id2, u2.getUid());

		assertEquals("hCouturier", u2.getUsername());

		// test that u3 can be fetched
		int id3 = 3;
		User u3 = dao.fetchUserById(id3);

		assertEquals(id3, u3.getUid());

		assertEquals("eDriscoll", u3.getUsername());

	}

	@Test
	void testListAll() throws Exception {

		List<User> users = dao.listAll();

		assertEquals(1, users.get(0).getUid());

		assertEquals("apritchard", users.get(0).getUsername());

		assertEquals(2, users.get(1).getUid());

		assertEquals("hCouturier", users.get(1).getUsername());

		assertEquals(3, users.get(2).getUid());

		assertEquals("eDriscoll", users.get(2).getUsername());

	}

	@Test
	void testCreate() throws Exception {

		User u1 = dao.create("lHouse", 4);
		User u3 = dao.create("mHiggs", 5);

		User u2 = dao.fetchUserById(u1.getUid());

		assertEquals(u1.getUsername(), u2.getUsername());

		User u4 = dao.fetchUserById(u3.getUid());

		assertEquals(u3.getUsername(), u4.getUsername());

	}

	@Test
	void testDelete() throws Exception {

		// checks to see that user with id 1 exists then
		User u1 = dao.fetchUserById(1);

		assertEquals(1, u1.getUid());

		assertEquals("apritchard", u1.getUsername());

		assertEquals(4, dao.listAll().size());

		// deletes user with id 1
		dao.delete(1);

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(1));
		assertEquals(3, dao.listAll().size());

		// checks user with id 2 exists
		u1 = dao.fetchUserById(2);

		assertEquals(2, u1.getUid());

		assertEquals("hCouturier", u1.getUsername());

		assertEquals(3, dao.listAll().size());

		// deletes user with id 2
		dao.delete(2);

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(2));
		assertEquals(2, dao.listAll().size());
	}

	@Test
	void testDeleteNewlyCreated() throws Exception {

		int originalSizeOfUserArray = dao.listAll().size();

		User u = dao.create("NewPerson", 4);

		// checks to see that user with id 1 exists then
		User u1 = dao.fetchUserById(u.getUid());

		assertEquals(u.getUid(), u1.getUid());

		assertEquals(u.getUsername(), u1.getUsername());

		assertEquals(originalSizeOfUserArray + 1, dao.listAll().size());

		// deletes user with id 1
		dao.delete(u.getUid());

		// verifies its been deleted
		assertEquals(null, dao.fetchUserById(u.getUid()));
		assertEquals(originalSizeOfUserArray, dao.listAll().size());
	}

	@Test
	void testUpdate() throws Exception {

		int id = 1;
		User u1 = dao.fetchUserById(id);

		assertEquals(id, u1.getUid());

		assertEquals("apritchard", u1.getUsername());

		int newContact = 5;

		// update each item
		dao.update(id, newContact);

		u1 = dao.fetchUserById(id);

		assertEquals(5, u1.getContactInfo().getContactId());
	}

	@Test
	void testFetchByUserName() throws Exception {

		// test that u1 can be fetched
		String uname1 = "apritchard";
		User u1 = dao.fetchUserByUsername(uname1);

		assertEquals(uname1, u1.getUsername());

		assertEquals("apritchard", u1.getUsername());

		// test that u2 can be fetched
		String uname2 = "hCouturier";
		User u2 = dao.fetchUserByUsername(uname2);

		assertEquals(uname2, u2.getUsername());

		assertEquals("hCouturier", u2.getUsername());

		// test that u3 can be fetched
		String uname3 = "eDriscoll";
		User u3 = dao.fetchUserByUsername(uname3);

		assertEquals(uname3, u3.getUsername());

		assertEquals("eDriscoll", u3.getUsername());
	}
	
	/*
	 * Delete method to verify the ServantUser associated with the
	 * specified User to be deleted is also removed.
	 */
	@Test
	void test_delete() throws Exception {
		
		dao.delete(1);
		
		// Should be null since deleted
		assertNull(srvUserDao.fetchServantUserById(1));
		
		// Size of ServantUsers should be decreased by one
		List<ServantUser> srvUsers = srvUserDao.listAllServantUsers();
		
		assertEquals(1, srvUsers.size());
		
		// with the ServantUserId of 4
		assertEquals(4, srvUsers.get(0).getUid());
		
	}
}
