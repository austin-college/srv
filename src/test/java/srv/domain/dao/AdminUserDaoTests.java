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

import srv.domain.user.AdminUser;
import srv.domain.user.AdminUserDao;
import srv.domain.user.ServantUser;

//needed this annotation to roll back test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class AdminUserDaoTests {

	@Autowired
	AdminUserDao adminUserDao;
	
	/*
	 * Testing listAll(), should return 1 current admin user
	 */
	@Test
	void test_listAll() throws Exception {
		
		List<AdminUser> testAdminUsers = adminUserDao.listAllAdminUsers();
		
		assertEquals(1, testAdminUsers.size());
		
		AdminUser au1 = testAdminUsers.get(0);
		
		// Verifying the contents of the AdminUser
		assertEquals(3, au1.getUid());
		assertEquals("eDriscoll", au1.getUsername());
		assertEquals(7, au1.getContactInfo().getContactId());
		assertEquals("Emma", au1.getContactInfo().getFirstName());
	}

	/*
	 * Testing fetchAdminUserById() when the specified is id valid
	 * (in the database). Should return the specified AdminUser.
	 */
	@Test
	void test_fetchById_whenIdValid() throws Exception {
		
		AdminUser testAdminUser = adminUserDao.fetchAdminUserById(3);
		
		// Verifying the contents of the AdminUser
		assertEquals(3, testAdminUser.getUid());
		assertEquals("eDriscoll", testAdminUser.getUsername());
		assertEquals(7, testAdminUser.getContactInfo().getContactId());
		assertEquals("Emma", testAdminUser.getContactInfo().getFirstName());
	}
	
	/*
	 * Testing fetchAdminUserById() when the specified is is invalid
	 * (not in the database). Should return null.
	 */
	@Test
	void test_fetchById_whenIdInvalid() throws Exception {
		
		AdminUser testAdminUser = adminUserDao.fetchAdminUserById(-1);
		
		assertNull(testAdminUser);
	}
	
	/*
	 * Testing create() when the specified username does not exist yet
	 * in the users datatable, requiring a new user to be made. Should
	 * create a new user, admin user, and contact in the data store.
	 */
	@Test
	void test_create_whenNewUser() throws Exception {
		
		AdminUser newAdminUser = adminUserDao.create("rbuckle19");
		
		// Verifying the contents of the AdminUser
		assertEquals(5, newAdminUser.getUid());
		assertEquals("rbuckle19", newAdminUser.getUsername());
		assertEquals(8, newAdminUser.getContactInfo().getContactId());

		// Verifying the User's contact info
		assertEquals("rbuckle19", newAdminUser.getContactInfo().getFirstName());
		assertNull(newAdminUser.getContactInfo().getLastName());
		assertEquals("rbuckle19@austincollege.edu", newAdminUser.getContactInfo().getEmail());
		assertNull(newAdminUser.getContactInfo().getPhoneNumWork());
		assertNull(newAdminUser.getContactInfo().getPhoneNumMobile());
		assertNull(newAdminUser.getContactInfo().getCity());
		assertNull(newAdminUser.getContactInfo().getState());
		assertNull(newAdminUser.getContactInfo().getZipcode());
		assertNull(newAdminUser.getContactInfo().getStreet());
	}
	
	/*
	 * Testing create when the specified username exists already in the users datatable.
	 * Should create a new admin user without having to create a new user.
	 */
	@Test
	void test_create_whenExistingUser() throws Exception {
		
		AdminUser newAdminUser = adminUserDao.create("apritchard");
		
		// Verifying the User contents
		assertEquals(1, newAdminUser.getUid());
		assertEquals(5, newAdminUser.getContactInfo().getContactId());
		
		// Verifying some of the User's contact info
		assertEquals("AJ", newAdminUser.getContactInfo().getFirstName());
		assertEquals("apritchard18@austincollege.edu", newAdminUser.getContactInfo().getEmail());
	}
	
	/*
	 * Testing create() when the username is null. Should throw an exception
	 * whose error message states that the username is now allowed to be null.
	 */
	@Test
	void test_create_whenUsernameNull_throwsException() throws Exception {
			
		Exception exception = assertThrows(Exception.class, () -> {
			adminUserDao.create(null);
		});
	 
	    String expectedMessage = "Username shall not be null.";
	    String actualMessage = exception.getMessage();
	 
	    assertTrue(actualMessage.contains(expectedMessage));		
	}
}
