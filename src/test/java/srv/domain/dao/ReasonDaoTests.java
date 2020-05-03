package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
public class ReasonDaoTests {

	@Autowired
	ReasonDao dao;
	
	@Test
	public void testGetById_whenUsingJdbcTemplate() throws Exception {
			
		Reason r1 = dao.getReasonById(1);
		
		assertEquals(1, r1.getRid());
		
		assertEquals("Assembly Drawing", r1.getReason());
		
	}

	
	
}
