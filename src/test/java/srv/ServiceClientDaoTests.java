package srv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.ServiceClient;


public class ServiceClientDaoTests {

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	void testGetById_whenUsingJdbcTemplate() throws Exception {
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		ServiceClient r1 = dao.fetchClientId(1);
		
		assertEquals(1, r1.getScid());

		assertEquals("Habitat for Humanity", r1.getName());
		assertEquals("Tom", r1.getContact().getFirstName());
		assertEquals("Billy Bob", r1.getBoardMember());
		assertEquals("Housing, Community", r1.getCategory());

		
	}



}
