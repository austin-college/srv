package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
import srv.domain.user.UserDao;

//needed this annotation to roll back test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class BoardMemberUserDaoTests {
	
	@Autowired
	BoardMemberUserDao bmUserDao;
	
	@Autowired
	UserDao uDao;
	
	/*
	 * Testing listAll(), should return 2 current board member users
	 */
	@Test
	void test_listAllBmUsers() throws Exception {
		
		List<BoardMemberUser> testBmUsers = bmUserDao.listAllBoardMemberUsers();
		
		assertEquals(2, testBmUsers.size());
		
		BoardMemberUser bm1 = testBmUsers.get(0);
		BoardMemberUser bm2 = testBmUsers.get(1);
		
		assertEquals(1, bm1.getUid());
		assertEquals(2, bm2.getUid());
		
		// Verifying the contents of one BoardMemberUser
		assertEquals(true, bm1.getIsCoChair());
		
		// Verifying inherited ServantUser fields
		assertEquals(2021, bm1.getExpectedGradYear());
		assertEquals(1, bm1.getAffiliation().getSgid());
		assertEquals(false, bm1.getHasCar());
		assertEquals(0, bm1.getCarCapacity());
		assertEquals("apritchard", bm1.getUsername());
		assertEquals(5, bm1.getContactInfo().getContactId());
	}
	
	/*
	 * Testing fetchBoardMemberUserById when the specified id is valid
	 * (in the database). Should return the specified user.
	 */
	@Test
	void test_fetchById_whenIdValid() throws Exception {
		
		BoardMemberUser testBmUser = bmUserDao.fetchBoardMemberUserById(1);
		
		// Verifying BoardMemberUser contents
		assertEquals(true, testBmUser.getIsCoChair());
		assertEquals(1, testBmUser.getUid());
		
		// Verifying inherited ServantUser fields
		assertEquals(2021, testBmUser.getExpectedGradYear());
		assertEquals(1, testBmUser.getAffiliation().getSgid());
		assertEquals(false, testBmUser.getHasCar());
		assertEquals(0, testBmUser.getCarCapacity());
		assertEquals("apritchard", testBmUser.getUsername());
		assertEquals(5, testBmUser.getContactInfo().getContactId());
	}
	
	/*
	 * Testing fetchBoardMemberUserById when the specified id is invalid
	 * (not in the database). Should return null.
	 */
	@Test
	void test_fetchById_whenIdInvalid() throws Exception {
		
		BoardMemberUser testNullUser = bmUserDao.fetchBoardMemberUserById(0);
		
		assertNull(testNullUser);
	}
	
	/*
	 * Testing create() when the specified username does not exist in neither the
	 * users nor servantUsers table, requiring a new user to be made. None of the other
	 * values (coChair) are null. Should create a new user, servant user, and board member user
	 * in the data store.
	 */
	@Test
	void test_create_whenNewUserNoNullValues() throws Exception {
		
		BoardMemberUser newBmUser = bmUserDao.create("rbuckle19", true);
		
		// Verifying the ServantUser's contents
		assertEquals(5, newBmUser.getUid());
		assertEquals("rbuckle19", newBmUser.getUsername());
		assertEquals(8, newBmUser.getContactInfo().getContactId());
		assertEquals("rbuckle19", newBmUser.getContactInfo().getFirstName());
		assertNull(newBmUser.getAffiliation());
		assertNull(newBmUser.getExpectedGradYear());
		assertNull(newBmUser.getHasCar());
		assertNull(newBmUser.getCarCapacity());
		
		// Verifying BoardMemberUser's contents
		assertEquals(true, newBmUser.getIsCoChair());
	}
	
	/*
	 * Testing create() method when the specified username exists in the users table 
	 * but not the servantUsers table, requiring a new servant user to be made. None
	 * of the other values (coChair) are null. Should create a new servant user and
	 * board member user in the data store.
	 */
	@Test
	void test_create_whenNewServantUserNoNullValues() throws Exception {
		
		BoardMemberUser newBmUser = bmUserDao.create("eDriscoll", false);
		
		// Verifying the ServantUser's contents
		assertEquals(3, newBmUser.getUid());
		assertEquals("eDriscoll", newBmUser.getUsername());
		assertEquals(7, newBmUser.getContactInfo().getContactId());
		assertEquals("Emma", newBmUser.getContactInfo().getFirstName());
		assertNull(newBmUser.getAffiliation());
		assertNull(newBmUser.getExpectedGradYear());
		assertNull(newBmUser.getHasCar());
		assertNull(newBmUser.getCarCapacity());
		
		// Verifying the BoardMemberUser's contents
		assertEquals(false, newBmUser.getIsCoChair());
	}
	
	/*
	 * Testing create() method when the specified username exists in the users table
	 * and the servantUsers table, requiring updating an existing servantUser to a
	 * board member. None of the other values (coChair) are null.  
	 */
	@Test
	void test_create_whenExistingUser() throws Exception {
		
		BoardMemberUser newBmUser = bmUserDao.create("user", true);
		
		// Verifying the ServantUser's contents
		assertEquals(4, newBmUser.getUid());
		assertEquals("user", newBmUser.getUsername());
		assertEquals(1, newBmUser.getContactInfo().getContactId());
		assertEquals("Tom", newBmUser.getContactInfo().getFirstName());
		assertEquals(2, newBmUser.getAffiliation().getSgid());
		assertEquals("DummyName02", newBmUser.getAffiliation().getShortName());
		assertEquals(2024, newBmUser.getExpectedGradYear());
		assertEquals(true, newBmUser.getHasCar());
		assertEquals(3, newBmUser.getCarCapacity());
		
		// Verifying the BoardMemberUser's contents
		assertEquals(true, newBmUser.getIsCoChair());
	}
	
	/*
	 * Testing create() method when the specified username exists in the
	 * users table and the servantUsers table, requiring updating an existing
	 * servantUser to a board member. The other values (coChair) are null. 
	 */
	@Test
	void test_create_whenExistingUser_nullValues() throws Exception {
		
		BoardMemberUser newBmUser = bmUserDao.create("user", null);
		
		// Verifying the ServantUser's contents
		assertEquals(4, newBmUser.getUid());
		assertEquals("user", newBmUser.getUsername());
		assertEquals(1, newBmUser.getContactInfo().getContactId());
		assertEquals("Tom", newBmUser.getContactInfo().getFirstName());
		assertEquals(2, newBmUser.getAffiliation().getSgid());
		assertEquals("DummyName02", newBmUser.getAffiliation().getShortName());
		assertEquals(2024, newBmUser.getExpectedGradYear());
		assertEquals(true, newBmUser.getHasCar());
		assertEquals(3, newBmUser.getCarCapacity());
		
		// Verifying the BoardMemberUser's contents
		assertNull(newBmUser.getIsCoChair());
	}
	
	/*
	 * Testing create() when the username is null. Should throw an exception
	 * whose error message states that the username is not allowed to be null.
	 */
	@Test
	void test_create_whenUsernameNull_throwsException() throws Exception {
			
		Exception exception = assertThrows(Exception.class, () -> {
			bmUserDao.create(null, false);
		});
	 
	    String expectedMessage = "Username shall not be null.";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));		
	}
	
	/*
	 * Testing update() when the userId is valid (exists in the data table)
	 * and none of the updated values are null. Should update the BoardMemberUser
	 * with the new values as well as the ServantUser and User's values.
	 */
	@Test
	void test_update_whenIdValid_noNullValues() throws Exception {
		
		bmUserDao.update(1, false, 2, 2025, true, 0, 3);
		
		// Assuming the fetchById is okay even though smelly code
		BoardMemberUser testBmUser = bmUserDao.fetchBoardMemberUserById(1);
		
		// Verifying updated values for board member
		assertEquals(false, testBmUser.getIsCoChair());
		
		// and for ServantUser
		assertEquals(2, testBmUser.getAffiliation().getSgid());
		assertEquals("DummyName02", testBmUser.getAffiliation().getShortName());
		assertEquals(2025, testBmUser.getExpectedGradYear());
		assertEquals(true, testBmUser.getHasCar());
		assertEquals(0, testBmUser.getCarCapacity());
		
		// and for User's contact info
		assertEquals(3, testBmUser.getContactInfo().getContactId());
		assertEquals("Joe", testBmUser.getContactInfo().getFirstName());	
	}
	
	/* 
	 * Testing update() when the userId is valid (exists in the data table)
	 * and some of the updated values are null. Should update the BoardMemberUser
	 * with the new values.
	 */
	@Test
	void test_update_whenIdValid_nullValues() throws Exception {
		
		bmUserDao.update(2, null, null, 2024, false, 0, null);
		
		// Assuming the fetchById is okay even though smelly code
		BoardMemberUser testBmUser = bmUserDao.fetchBoardMemberUserById(2);
		
		// Verifying updated values for board member
		assertNull(testBmUser.getIsCoChair());
		
		// and for ServantUser
		assertNull(testBmUser.getAffiliation());
		assertEquals(2024, testBmUser.getExpectedGradYear());
		assertEquals(false, testBmUser.getHasCar());
		assertEquals(0, testBmUser.getCarCapacity());
		
		// and for Users contact info
		assertNull(testBmUser.getContactInfo());
	}
	
	/*
	 * Testing update() when the userId is invalid (does not exist in the data table).
	 * Should throw an exception stating that the specified BoardMemberUser was not
	 * able to be updated.
	 * 
	 * NOTE: needs to be a value that is in the ServantUser tables but not BoardMember table
	 * othewise the update() for the ServantUserDao will throw the exception.
	 */
	@Test
	void test_update_whenIdInvalid() throws Exception {
	
		Exception exception = assertThrows(Exception.class, () -> {
			bmUserDao.update(4, false, 2, 2021, false, 0, null);
		});
	 
	    String expectedMessage = "Unable to update board member user 4";
	    String actualMessage = exception.getMessage();
	    System.err.println(actualMessage);

	    assertTrue(actualMessage.contains(expectedMessage));		
	}
	
	/*
	 * Testing delete when the userId is valid (exists in the data table).
	 * Should delete the BoardMemberUser.
	 */
	@Test
	void test_delete_whenIdValid() throws Exception {
		
		// before the delete verify the user's role is a board member
		assertEquals("BOARDMEMBER", bmUserDao.fetchBoardMemberUserById(2).getRoll());
		
		bmUserDao.delete(2);
		
		// Deleted values should now be null
		assertNull(bmUserDao.fetchBoardMemberUserById(2));
		
		List<BoardMemberUser> allBmUsers = bmUserDao.listAllBoardMemberUsers();
		
		// Should only have 1 BoardMember user in the boardMemberUsers table
		assertEquals(1, allBmUsers.size());
		
		// who has a userId of 4
		assertEquals(1, allBmUsers.get(0).getUid());
		
		// after delete verify the board member that was deleted now has a role of 'user'
		// need to use the user dao...
		assertEquals("USER", uDao.fetchUserById(2).getRoll());
	}
	
	/*
	 * Testing delete() when the userId is invalid (does not exist in
	 * the data table). Should throw an exception stating that the
	 * specified board member user was not able to be deleted.
	 */
	@Test
	void test_delete_whenIdInvalid() throws Exception {
		
		Exception exception = assertThrows(Exception.class, () -> {
			bmUserDao.delete(-1);
		});
	 
	    String expectedMessage = "Unable to delete board member user [-1]";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));	
	}
	
}
