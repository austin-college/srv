package srv.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import srv.domain.contact.Contact;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;

@RunWith(MockitoJUnitRunner.class)
public class BoardMemberServiceTests {
	
	@Mock 
	private ServiceHoursDao mockSrvHrsDao;
	
	@Mock
	private BoardMemberUserDao mockBmUserDao;
	
	@Mock
	private ServantUserDao mockSrvUserDao;
	
	@InjectMocks
	private BoardMemberService bmSrv;
	
	@Rule 
	public ExpectedException exceptionRule = ExpectedException.none();
	
	// handy objects for these tests
	private BoardMemberUser bm1;
	private BoardMemberUser bm2;
	
	private ServantUser srvUser1;
	private ServantUser srvUser2;
	private ServantUser srvUser3;
	
	private Contact contact1;
	private Contact contact2;
	private Contact contact3;
	
	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);

		contact1 = new Contact()
				.setContactId(1)
				.setFirstName("Rusty")
				.setLastName("Buckle")
				.setPrimaryPhone("903-338-3302")
				.setSecondaryPhone("111-333-2422")
				.setEmail("rBuckle19@austincollege.edu")
				;
		
		contact2 = new Contact()
				.setContactId(2)
				.setFirstName("Lizzy")
				.setLastName("Roo")
				.setPrimaryPhone("123-555-2002")
				.setSecondaryPhone(null)
				.setEmail("lRoo18@austincollege.edu")
				;
		
		contact3 = new Contact()
				.setContactId(3)
				.setFirstName("Jackie")
				.setLastName("Chan")
				.setEmail("jChan20@austincollege.edu")
				;
		
		srvUser1 = new ServantUser(1, "rbuckle19", contact1, 2023, null, false, 0);
		srvUser2 = new ServantUser(2, "lRoo18", contact2, 2022, null, false, 0);
		srvUser3 = new ServantUser(3, "jChan20", contact3, 2024, null, true, 3);
		
		bm1 = new BoardMemberUser(1, "rbuckle19", contact1, 2023, null, false, 0).setIsCoChair(false);
		bm2 = new BoardMemberUser(2, "lRoo18", contact2, 2022, null, false, 0).setIsCoChair(true);
		
	}
	
	
	/**
	 * Make sure the service asks the dao for the list of all current board member users.
	 * Should return a list that has 2 board members in it.
	 */
	@Test
	public void test_listAllBoardMemberUsers() throws Exception {
		
		List<BoardMemberUser> dummyList = new ArrayList<BoardMemberUser>();
		dummyList.add(bm1); dummyList.add(bm2);
		
		Mockito.when(mockBmUserDao.listAllBoardMemberUsers()).thenReturn(dummyList);
		
		List<BoardMemberUser> newDummyList = bmSrv.listAllBoardMemberUsers();
		assertEquals(2, newDummyList.size());
		
		Mockito.verify(mockBmUserDao).listAllBoardMemberUsers();
	}
	
	/** 
	 * Make sure the service asks the dao for the list of all current servant users that are not
	 * board members. Should return a list that has 1 servant user in it.
	 */
	@Test
	public void test_nonBmUsers() throws Exception {
		
		List<ServantUser> dummyList = new ArrayList<ServantUser>();
		dummyList.add(srvUser3);
		
		Mockito.when(mockSrvUserDao.nonBmUsers()).thenReturn(dummyList);
		
		List<ServantUser> newDummyList = bmSrv.nonBmUsers();
		assertEquals(1, newDummyList.size());
		
		Mockito.verify(mockSrvUserDao).nonBmUsers();
	}
	
	/**
	 * Test to make sure service tells dao to delete/demote the board member
	 */
	@Test
	public void test_delete_whenIdIsValid() throws Exception {
		
		Mockito.doNothing().when(mockBmUserDao).delete(Mockito.anyInt());
		
		bmSrv.delete(1);
		
		Mockito.verify(mockBmUserDao).delete(1);
		
	}
	
	/**
	 * Test to make sure service checks for valid id and throws exception
	 * when not valid
	 */
	@Test(expected=Exception.class)
	public void test_delete_whenIdIsInvalid() throws Exception {
	
		bmSrv.delete(-1);
	}
	
	/**
	 * Test to make sure service creates/promotes a dummy servant user
	 */
	@Test
	public void test_createBm_whenUsernameIsValid() throws Exception {
		
		// train the mock dao just return bm1 for this test regardless of params
		Mockito.when(mockBmUserDao.create(Mockito.any(String.class), Mockito.anyBoolean())).thenReturn(bm1);
		
		// ready to test...
		BoardMemberUser bm = bmSrv.create("jChan19", false);
		
		// make sure the dao was told to create weth expected parameters
		Mockito.verify(mockBmUserDao).create(Mockito.eq("jChan19"), Mockito.eq(false));
	}
	
}
