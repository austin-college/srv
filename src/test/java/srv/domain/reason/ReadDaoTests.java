package srv.domain.reason;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadDaoTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListAll_whenUsingJdbc() throws Exception {
		ReasonDao dao = new JdbcReasonDao();

		
		List<Reason> reasons = dao.listAll();
		
		assertEquals(2, reasons.size());
		
		Reason r1 = reasons.get(0);
		Reason r2 = reasons.get(1);
		
		assertEquals(1, r1.getRid());
		assertEquals(2, r2.getRid());
		
		assertEquals("Assembly Drawing", r1.getReason());
		assertEquals("Piece Part Drawing", r2.getReason());
		
	}

	
	
	
	@Test
	void testGetById_whenUsingJdbc() throws Exception {
		ReasonDao dao = new JdbcReasonDao();
		
		Reason r1 = dao.getReasonById(1);
		
		assertEquals(1, r1.getRid());
		
		assertEquals("Assembly Drawing", r1.getReason());
		
	}

	
	@Test
	void testGetById_whenUsingJdbcTemplate() throws Exception {
		JdbcTemplateReasonDao dao = new JdbcTemplateReasonDao();
		
		Reason r1 = dao.getReasonById(1);
		
		assertEquals(1, r1.getRid());
		
		assertEquals("Assembly Drawing", r1.getReason());
		
	}

	
	
}
