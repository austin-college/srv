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
}
