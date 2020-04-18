package srv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.ServiceClient;


public class ServiceClientDaoTests {
	
	/*
	 * Testing fetchClientById() should return the first
	 * service client info in the list.
	 */
	@Test
	void testGetById_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		ServiceClient sc1 = dao.fetchClientId(1);
		
		assertEquals(1, sc1.getScid());
		assertEquals("Habitat for Humanity", sc1.getName());
		assertEquals("Billy Bob", sc1.getBoardMember());
		assertEquals("Housing, Community", sc1.getCategory());

		// Testing Contact info
		assertEquals(1, sc1.getContact().getContactId());
		assertEquals("Tom", sc1.getContact().getFirstName());
		assertEquals("Hanks", sc1.getContact().getLastName());
		assertEquals("thanks@gmail.com", sc1.getContact().getEmail());
		assertEquals("903-420-1212", sc1.getContact().getPhoneNumWork());
		assertEquals("400-232-1211", sc1.getContact().getPhoneNumMobile());
		assertEquals("626 E Main Street", sc1.getContact().getStreet());
		assertEquals("Sherman", sc1.getContact().getCity());
		assertEquals("TX", sc1.getContact().getState());
		assertEquals("75090", sc1.getContact().getZipcode());	
	}



}
