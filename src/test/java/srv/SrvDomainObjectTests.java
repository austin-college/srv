package srv;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.Test;

import srv.domain.User;

class SrvDomainObjectTests {

	@Test
	void UserConstructionTest() {
		User testUser = new User("hunter", "generic passowrd 1");

		assertTrue(testUser != null);
		assertEquals("hunter", testUser.getUserID());
		assertEquals("generic passowrd 1", testUser.getPassword());
	}

	@Test
	void UserListAllOneItemTest() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		arrayListHolder.add(testUser);
		User[] tested = User.listAll(arrayListHolder);
		assertEquals(null, tested[1]);
		assertEquals(testUser, tested[0]);
	}

	@Test
	void UserListAllTwoItemTest() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		User testUser2 = new User("Larry", "THELarry");
		arrayListHolder.add(testUser);
		arrayListHolder.add(testUser2);
		User[] tested = User.listAll(arrayListHolder);
		assertEquals(null, tested[2]);
		assertTrue(tested[1] == testUser2);
	}

	@Test
	void UserListAllThreeItemTest() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		User testUser2 = new User("Larry", "THELarry");
		User testUser3 = new User("Leroy", "Jenkins");
		arrayListHolder.add(testUser);
		arrayListHolder.add(testUser2);
		arrayListHolder.add(testUser3);
		User[] tested = User.listAll(arrayListHolder);
		assertFalse(null == tested[2]);
		assertTrue(tested[1] == testUser2);
	}

	void UserListAllLotsOfItemsTest() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		User testUser2 = new User("Larry", "THELarry");
		User testUser3 = new User("Leroy", "Jenkins");
		User testUser4 = new User("Vic", "gc1");
		User testUser5 = new User("Lowd", "lzdw");
		User testUser6 = new User("mcFunk", "gawd");
		arrayListHolder.add(testUser);
		arrayListHolder.add(testUser2);
		arrayListHolder.add(testUser3);
		arrayListHolder.add(testUser4);
		arrayListHolder.add(testUser5);
		arrayListHolder.add(testUser6);
		User[] tested = User.listAll(arrayListHolder);
		assertFalse(null == tested[2]);
		assertTrue((tested[0] == testUser) || (tested[0] == testUser2) || (tested[0] == testUser3) || (tested[0] == testUser4) || (tested[0] == testUser5) || (tested[0] == testUser6));
	}

	@Test
	void UserFindUserTestWithOne() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		arrayListHolder.add(testUser);

		
		User examinedUser = User.findUser("hunter", arrayListHolder);
		assertEquals(testUser, examinedUser);
		

	}
	
	@Test
	void UserFindUserTestWithLots() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		User testUser = new User("hunter", "generic passowrd 1");
		User testUser2 = new User("Larry", "THELarry");
		User testUser3 = new User("Leroy", "Jenkins");
		User testUser4 = new User("Vic", "gc1");
		User testUser5 = new User("Lowd", "lzdw");
		User testUser6 = new User("mcFunk", "gawd");
		arrayListHolder.add(testUser);
		arrayListHolder.add(testUser2);
		arrayListHolder.add(testUser3);
		arrayListHolder.add(testUser4);
		arrayListHolder.add(testUser5);
		arrayListHolder.add(testUser6);
		User examinedUser = User.findUser("mcFunk", arrayListHolder);
		assertEquals(testUser6, examinedUser);

	}
	
	@Test
	void UserFindUserTestWithNone() {
		ArrayList<User> arrayListHolder = new ArrayList<User>();
		
		User examinedUser = User.findUser("hunter", arrayListHolder);
		assertEquals(null, examinedUser);
	}
}
