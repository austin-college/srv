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

}
