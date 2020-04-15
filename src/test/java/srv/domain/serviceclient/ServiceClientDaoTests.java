package srv.domain.serviceclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import srv.domain.serviceclient.JdbcServiceClientDao;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;

public class ServiceClientDaoTests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testListAll_whenUsingJdbc() throws Exception {
		ServiceClientDao dao = new JdbcServiceClientDao();

		
		List<ServiceClient> clients = dao.listAll();
		
		assertEquals(2, clients.size());
		
		ServiceClient sc1 = clients.get(0);
	
		
		assertEquals(1, sc1.getScid());

		
		assertEquals("Work pl0x", sc1.getTitle());

		
	}

	
	
	@Test
	void testGetById_whenUsingJdbc() throws Exception {
		ServiceClientDao dao = new JdbcServiceClientDao();

		
		ServiceClient sc1 = dao.fetchClientId(1);
		
		assertEquals(1, sc1.getScid());
		
		assertEquals("Work pl0x", sc1.getTitle());
		
	}


}
