package srv.domain.serviceclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import srv.domain.ServiceClientDao;
import srv.domain.reason.JdbcTemplateReasonDao;
import srv.domain.reason.Reason;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.serviceclient.ServiceClient;

public class ServiceClientDaoTests {

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void testGetById_whenUsingJdbcTemplate() throws Exception {
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		ServiceClient r1 = dao.fetchClientId(1);
		
		assertEquals(1, r1.getScid());

		assertEquals("Meals on Wheels", r1.getTitle());
		assertEquals("Tom Hanks", r1.getContact());
		assertEquals("Billy Bob", r1.getBoardMember());
		assertEquals("Housing, Community", r1.getCategory());

		
	}



}
