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

//needed this annotation to roll back test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class BoardMemberUserDaoTests {
	
	@Autowired
	BoardMemberUserDao bmUserDao;
	
	/*
	 * Testing listAll(), should return 2 current board member users
	 */
	@Test
	void test_listAllBmUsers() throws Exception {
		
		List<BoardMemberUser> testBmUsers = bmUserDao.listAllBoardMemberUsers();
		
		assertEquals(2, testBmUsers.size());
		
		BoardMemberUser bm1 = testBmUsers.get(0);
		BoardMemberUser bm2 = testBmUsers.get(1);
		
		assertEquals(2, bm1.getUid());
		assertEquals(4, bm2.getUid());
		
		// Verifying the contents of one BoardMemberUser
		assertEquals(true, bm1.getIsCoChair());
		
		// Verifying inherited ServantUser fields
		assertEquals(2023, bm1.getExpectedGradYear());
		assertEquals(3, bm1.getAffiliation().getSgid());
		assertEquals(true, bm1.getHasCar());
		assertEquals(1, bm1.getCarCapacity());
		assertEquals("hCouturier", bm1.getUsername());
		assertEquals(6, bm1.getContactInfo().getContactId());
	}
	
	/*
	 * Testing fetchBoardMemberUserById when the specified id is valid
	 * (in the database). Should return the specified user.
	 */
	@Test
	void test_fetchById_whenIdValid() throws Exception {
		
		BoardMemberUser testBmUser = bmUserDao.fetchBoardMemberUserById(4);
		
		// Verifying BoardMemberUser contents
		assertEquals(false, testBmUser.getIsCoChair());
		assertEquals(4, testBmUser.getUid());
		
		// Verifying inherited ServantUser fields
		assertEquals(2024, testBmUser.getExpectedGradYear());
		assertEquals(2, testBmUser.getAffiliation().getSgid());
		assertEquals(true, testBmUser.getHasCar());
		assertEquals(3, testBmUser.getCarCapacity());
		assertEquals("user", testBmUser.getUsername());
		assertEquals(1, testBmUser.getContactInfo().getContactId());
	}
	
	/*
	 * Testing fetchBoardMemberUserById when the specified id is invalid
	 * (not in the database). Should return null.
	 */
	@Test
	void test_fetchById_whenIdInvalid() throws Exception {
		
		BoardMemberUser testNullUser = bmUserDao.fetchBoardMemberUserById(-1);
		
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
		
		BoardMemberUser newBmUser = bmUserDao.create("apritchard", true);
		
		// Verifying the ServantUser's contents
		assertEquals(1, newBmUser.getUid());
		assertEquals("apritchard", newBmUser.getUsername());
		assertEquals(5, newBmUser.getContactInfo().getContactId());
		assertEquals("AJ", newBmUser.getContactInfo().getFirstName());
		assertEquals(1, newBmUser.getAffiliation().getSgid());
		assertEquals("DummyName01", newBmUser.getAffiliation().getShortName());
		assertEquals(2021, newBmUser.getExpectedGradYear());
		assertEquals(false, newBmUser.getHasCar());
		assertEquals(0, newBmUser.getCarCapacity());
		
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
		
		BoardMemberUser newBmUser = bmUserDao.create("apritchard", null);
		
		// Verifying the ServantUser's contents
		assertEquals(1, newBmUser.getUid());
		assertEquals("apritchard", newBmUser.getUsername());
		assertEquals(5, newBmUser.getContactInfo().getContactId());
		assertEquals("AJ", newBmUser.getContactInfo().getFirstName());
		assertEquals(1, newBmUser.getAffiliation().getSgid());
		assertEquals("DummyName01", newBmUser.getAffiliation().getShortName());
		assertEquals(2021, newBmUser.getExpectedGradYear());
		assertEquals(false, newBmUser.getHasCar());
		assertEquals(0, newBmUser.getCarCapacity());
		
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
}
