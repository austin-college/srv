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
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;

// needed this annotation to roll back test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class ServantUserDaoTests {

	@Autowired
	ServantUserDao srvUserDao;
	
	@Autowired
	BoardMemberUserDao bmUserDao;
		
	/*
	 * Testing listAll(), should return 3 current servant users
	 */
	@Test
	void test_listAllServantUsers() throws Exception {
		
		List<ServantUser> testSrvUsers = srvUserDao.listAllServantUsers();
		
		assertEquals(3, testSrvUsers.size());
		
		ServantUser su1 = testSrvUsers.get(0);
		ServantUser su2 = testSrvUsers.get(1);
		ServantUser su3 = testSrvUsers.get(2);
		
		assertEquals(1, su1.getUid());
		assertEquals(2, su2.getUid());
		assertEquals(4, su3.getUid());
		
		// Verifying the contents of one ServantUser
		assertEquals(2024, su3.getExpectedGradYear());
		assertEquals(true, su3.getHasCar());
		assertEquals(3, su3.getCarCapacity());
		
		// Verifying the ServiceGroup
		assertEquals(2, su3.getAffiliation().getSgid());
		assertEquals("DummyName02", su3.getAffiliation().getShortName());
		assertEquals("DummyTitle02", su3.getAffiliation().getTitle());
		assertEquals(2, su3.getAffiliation().getContactInfo().getContactId());
		
		// Verifying User contents
		assertEquals("user", su3.getUsername());
		assertEquals(1, su3.getContactInfo().getContactId());
		assertEquals("Tom", su3.getContactInfo().getFirstName());
		assertEquals("USER", su3.getRoll());	
		
	}
	
	/*
	 * Testing fetchServantUserById when the specified id is valid (in the database).
	 * Should return the specified user
	 */
	@Test
	void test_fetchById_whenIdValid() throws Exception {
		
		ServantUser testSrvUser = srvUserDao.fetchServantUserById(1);
		
		// Verifying User contents
		assertEquals(1, testSrvUser.getUid());
		assertEquals("apritchard", testSrvUser.getUsername());
		assertEquals("USER", testSrvUser.getRoll());
		
		// Verifying some Contact info 
		assertEquals(5, testSrvUser.getContactInfo().getContactId());
		assertEquals("apritchard18@austincollege.edu", testSrvUser.getContactInfo().getEmail());
		
		// Verifying ServantUser contents
		assertEquals(2021, testSrvUser.getExpectedGradYear());
		assertEquals(1, testSrvUser.getAffiliation().getSgid());
		assertEquals(false, testSrvUser.getHasCar());
		assertEquals(0, testSrvUser.getCarCapacity());
		
		// Verifying some ServiceGroup contents
		assertEquals("DummyName01", testSrvUser.getAffiliation().getShortName());
	}
	
	/*
	 * Testing fetchServantUserById when the specified id is invalid (not in the database).
	 * Should return null.
	 */
	@Test
	void test_fetchById_whenIdInvalid() throws Exception {
		
		ServantUser testNullUser = srvUserDao.fetchServantUserById(-1);
		
		assertNull(testNullUser);
	
	}
	
	/*
	 * Testing create() when the specified username does not exist yet in the
	 * users datatable, requiring a new user to be made. None of the other values
	 * (affilation, grad year, etc) are null. Should create a new user, servant user
	 * and contact in the data store. 
	 */
	@Test
	void test_create_whenNewUserNoNullValues() throws Exception {
		
		ServantUser newUser = srvUserDao.create("rbuckle19", 1, 2021, true, 2);
		
		// Verifying the User contents
		assertEquals(5, newUser.getUid());
		assertEquals("rbuckle19", newUser.getUsername());
		assertEquals(8, newUser.getContactInfo().getContactId());
		
		// Verifying the User's contact info
		assertEquals("rbuckle19", newUser.getContactInfo().getFirstName());
		assertNull(newUser.getContactInfo().getLastName());
		assertEquals("rbuckle19@austincollege.edu", newUser.getContactInfo().getEmail());
		assertNull(newUser.getContactInfo().getPrimaryPhone());
		assertNull(newUser.getContactInfo().getSecondaryPhone());
		assertNull(newUser.getContactInfo().getCity());
		assertNull(newUser.getContactInfo().getState());
		assertNull(newUser.getContactInfo().getZipcode());
		assertNull(newUser.getContactInfo().getStreet());
		
		// Verifying the ServantUser contents
		assertEquals(2021, newUser.getExpectedGradYear());
		assertEquals(true, newUser.getHasCar());
		assertEquals(2, newUser.getCarCapacity());

		// Verifying ServiceGroup Contents
		assertEquals(1, newUser.getAffiliation().getSgid());
		assertEquals("DummyName01", newUser.getAffiliation().getShortName());
		
	}
	
	/*
	 * Testing create() when the specified username does not exist yet in the users
	 * datatable, requiring a new user to be made. The other values (affiliation, 
	 * grad year, etc) are null. Should create a new user, servant user, and contact 
	 * in the data store and handle the null values just fine.
	 * 
	 */
	@Test
	void test_create_whenNewUserWithNullValues() throws Exception {
		
		ServantUser newUser = srvUserDao.create("bruckle21", null, null, null, null);
		
		// Verifying the User contents
		assertEquals(5, newUser.getUid());
		assertEquals("bruckle21", newUser.getUsername());
		assertEquals(8, newUser.getContactInfo().getContactId());
		
		// Verifying the User's contact info
		assertEquals("bruckle21", newUser.getContactInfo().getFirstName());
		assertNull(newUser.getContactInfo().getLastName());
		assertEquals("bruckle21@austincollege.edu", newUser.getContactInfo().getEmail());
		assertNull(newUser.getContactInfo().getPrimaryPhone());
		assertNull(newUser.getContactInfo().getSecondaryPhone());
		assertNull(newUser.getContactInfo().getCity());
		assertNull(newUser.getContactInfo().getState());
		assertNull(newUser.getContactInfo().getZipcode());
		assertNull(newUser.getContactInfo().getStreet());
		
		// Verifying the ServantUser contents
		assertNull(newUser.getExpectedGradYear());
		assertNull(newUser.getAffiliation());
		assertNull(newUser.getHasCar());
		assertNull(newUser.getCarCapacity());
	}
	
	/*
	 * Testing create when the specified username exists already in the users datatable.
	 * Should create a new servant user without having to create a new user. None of the
	 * other values (affiliation, grad year) are null.
	 */
	@Test
	void test_create_whenExistingUser() throws Exception {
		
		ServantUser newUser = srvUserDao.create("eDriscoll", 3, 2022, false, 0);
		
		// Verifying the User contents
		assertEquals(3, newUser.getUid());
		assertEquals(7, newUser.getContactInfo().getContactId());
		
		// Verifying some of the User's contact info
		assertEquals("Emma", newUser.getContactInfo().getFirstName());
		assertEquals("eDriscoll@gmail.com", newUser.getContactInfo().getEmail());
		
		// Verifying the Servant contents
		assertEquals(2022, newUser.getExpectedGradYear());
		assertEquals(false, newUser.getHasCar());
		assertEquals(0, newUser.getCarCapacity());
		assertEquals(3, newUser.getAffiliation().getSgid());
		
		// Verifying some of the ServiceGroup contents
		assertEquals("DummyName03", newUser.getAffiliation().getShortName());
	}
	
	/*
	 * Testing create() when the username is null. Should throw an exception
	 * whose error message states that the username is now allowed to be null.
	 */
	@Test
	void test_create_whenUsernameNull_throwsException() throws Exception {
			
		Exception exception = assertThrows(Exception.class, () -> {
			srvUserDao.create(null, null, 2021, false, 0);
		});
	 
	    String expectedMessage = "Thy username shall not be null.";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));		
	}
	
	/*
	 * Testing update() when the userId is valid (exists in the data table)
	 * and none of the updated values are null. Should update the ServantUser
	 * with the new values and change the User's contact info.
	 */
	@Test
	void test_update_whenIdValidNoNull() throws Exception {
		
		srvUserDao.update(4, 3, 2025, false, 0, 4);
		
		// Assuming the fetchById is okay even though smelly code
		ServantUser testSrvUser = srvUserDao.fetchServantUserById(4);
		
		// Verifying updated values
		assertEquals(3, testSrvUser.getAffiliation().getSgid());
		assertEquals(2025, testSrvUser.getExpectedGradYear());
		assertEquals(false, testSrvUser.getHasCar());
		assertEquals(0, testSrvUser.getCarCapacity());
		
		// Verifying the new service group short name
		assertEquals("DummyName03", testSrvUser.getAffiliation().getShortName());
		
		// Verifying User contents
		assertEquals(4, testSrvUser.getUid());
		assertEquals("user", testSrvUser.getUsername());
		
		// Verifying some Contact contents
		assertEquals(4, testSrvUser.getContactInfo().getContactId());
		assertEquals("Susan", testSrvUser.getContactInfo().getFirstName());
		assertEquals("Atkins", testSrvUser.getContactInfo().getLastName());
		
	}
	
	/*
	 * Testing update() when the userId is valid (exists in the data table)
	 * and some of the updated values are null. Should update the ServantUser
	 * with the new values.
	 */
	@Test
	void test_update_whenIdValidNull() throws Exception {
		
		srvUserDao.update(1, null, 2020, true, 1, null);
		
		// Assuming the fetchById is okay even though smelly code
		ServantUser testSrvUser = srvUserDao.fetchServantUserById(1);
		
		// Verifying updated values
		assertEquals(2020, testSrvUser.getExpectedGradYear());
		assertEquals(true, testSrvUser.getHasCar());
		assertEquals(1, testSrvUser.getCarCapacity());
		assertNull(testSrvUser.getAffiliation());	
		assertNull(testSrvUser.getContactInfo());
	
	}
	
	/*
	 * Testing update() when the userId is invalid (does not exist in the data table).
	 * Should throw an exception stating that the specified ServantUser was not
	 * able to update.
	 */
	@Test
	void test_update_whenIdInvalid() throws Exception {
		
		Exception exception = assertThrows(Exception.class, () -> {
			srvUserDao.update(-1, 2, 2024, false, 0, 1);
		});
	 
	    String expectedMessage = "Unable to update servant user -1";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));		
		
	}
	
	/*
	 * Testing delete when the userId is valid (exists in the data table).
	 * Should delete the ServantUser.
	 */
	@Test
	void test_delete_whenIdValid() throws Exception {
		
		srvUserDao.delete(4);
		
		// Deleted values should now be null
		assertNull(srvUserDao.fetchServantUserById(4));
		
		List<ServantUser> allSrvUsers = srvUserDao.listAllServantUsers(); 
		
		// Should only have 2 ServantUser in the servantUsers table
		assertEquals(2, allSrvUsers.size());
		
		// who has user id is 1
		assertEquals(1, allSrvUsers.get(0).getUid());
		assertEquals(2, allSrvUsers.get(1).getUid());
	}
	
	/*
	 * Testing delete() when the userId is invalid (does not exist
	 * in the data table). Should throw an exception stating that
	 * the specified servant user was not able to be deleted.
	 */
	@Test
	 void test_delete_whenIdInvalid() throws Exception {
		
		Exception exception = assertThrows(Exception.class, () -> {
			srvUserDao.delete(3);
		});
	 
	    String expectedMessage = "Unable to delete servant user [3]";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));		
	}
	
	/*
	 * Delete method to verify the BoardMember associated with the
	 * specified ServantUser to be deleted is also removed.
	 */
	@Test
	void test_delete_boardMemberUser() throws Exception {
		
		srvUserDao.delete(2);
		
		//  Should be null since deleted
		assertNull(bmUserDao.fetchBoardMemberUserById(2));
		
		// Size of BoardMemberUsers should be decreased by one
		List<BoardMemberUser> bmUsers = bmUserDao.listAllBoardMemberUsers();
		
		assertEquals(1, bmUsers.size());
		
		// with the following ids
		assertEquals(4, bmUsers.get(0).getUid());
	}
}
