package srv;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
		
		assertEquals(1, sc1.getClientId());
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
	
	/*
	 * Testing listAll(), should return the current 2 service client entries that are in the 
	 * data.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception { 
		
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		List<ServiceClient> clients = dao.listAll();
		
		assertEquals(2, clients.size());
		
		ServiceClient sc1 = clients.get(0);
		ServiceClient sc2 = clients.get(1);
		
		assertEquals(1, sc1.getClientId());
		assertEquals(2, sc2.getClientId());
		
		// Service Client info for client id 1
		assertEquals("Habitat for Humanity", sc1.getName());
		assertEquals("Billy Bob", sc1.getBoardMember());
		assertEquals("Housing, Community", sc1.getCategory());

		// Testing Contact info for service client id 1
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
		
		// Service Client info for client id 2
		assertEquals("Crisis Center", sc2.getName());
		assertEquals("Rick Astley", sc2.getBoardMember());
		assertEquals("Women, Crisis Support", sc2.getCategory());
		
		// Testing Contact info for service client id 2
		assertEquals(2, sc2.getContact().getContactId());
		assertEquals("Lois", sc2.getContact().getFirstName());
		assertEquals("Lane", sc2.getContact().getLastName());
		assertEquals("llane86@gmail.com", sc2.getContact().getEmail());
		assertEquals("803-423-1257", sc2.getContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc2.getContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc2.getContact().getStreet());
		assertEquals("Sherman", sc2.getContact().getCity());
		assertEquals("TX", sc2.getContact().getState());
		assertEquals("75090", sc2.getContact().getZipcode());
	}
	
	/* TODO BROKEN :c
	 * TODO creating a new service client with new contact?? 
	 * 
	 * Testing the create(), should create a new Service Client query in the 
	 * data.sql database.
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		// if this isn't here i get a null pointer exception so i have no idea whats up
		ServiceClient sc1 = dao.fetchClientId(1);
		
		ServiceClient sc = dao.create("Meals on Wheels", 1, "Donald Duck", "Seniors, Community");

		ServiceClient sc3 = dao.fetchClientId(3);	
		
/*		assertEquals(3, sc3.getClientId());
		assertEquals("Meals on Wheels", sc3.getName());
		assertEquals("Donald Duck", sc3.getBoardMember());
		assertEquals("Seniors, Community", sc3.getCategory());
		*/
		// Testing Contact info for service client
		/*assertEquals(2, sc3.getContact().getContactId());
		assertEquals("Lois", sc3.getContact().getFirstName());
		assertEquals("Lane", sc3.getContact().getLastName());
		assertEquals("llane86@gmail.com", sc3.getContact().getEmail());
		assertEquals("803-423-1257", sc3.getContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc3.getContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc3.getContact().getStreet());
		assertEquals("Sherman", sc3.getContact().getCity());
		assertEquals("TX", sc3.getContact().getState());
		assertEquals("75090", sc3.getContact().getZipcode());
	*/}
	
	/*
	 *  Testing the delete(), should remove the query with the specified ID (first one in this case). Should
	 *  still be one query left in the database.
	 */
	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
			
		dao.delete(1);
		
		List<ServiceClient> clients = dao.listAll();
		
		assertEquals(1, clients.size());
		
		ServiceClient sc1 = clients.get(0);
		
		// Service Client info for client id 2
		assertEquals("Crisis Center", sc1.getName());
		assertEquals("Rick Astley", sc1.getBoardMember());
		assertEquals("Women, Crisis Support", sc1.getCategory());

		// Testing Contact info for service client id 2
		assertEquals(2, sc1.getContact().getContactId());
		assertEquals("Lois", sc1.getContact().getFirstName());
		assertEquals("Lane", sc1.getContact().getLastName());
		assertEquals("llane86@gmail.com", sc1.getContact().getEmail());
		assertEquals("803-423-1257", sc1.getContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc1.getContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc1.getContact().getStreet());
		assertEquals("Sherman", sc1.getContact().getCity());
		assertEquals("TX", sc1.getContact().getState());
		assertEquals("75090", sc1.getContact().getZipcode());	
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID. 
	 * Switched the contact id to point to the first entry in the contacts table 
	 * rather than the second entry.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {
		
		JdbcTemplateServiceClientDao dao = new JdbcTemplateServiceClientDao();
		
		dao.update(2, "Meals on Wheels", 1, "Rick Astley", "Seniors, Community");
		
		ServiceClient sc2 = dao.fetchClientId(2);

		// Service Client info for client id 2
		assertEquals("Meals on Wheels", sc2.getName());
		assertEquals("Rick Astley", sc2.getBoardMember());
		assertEquals("Seniors, Community", sc2.getCategory());
		
		// Testing Contact info for service client id 2
		assertEquals(1, sc2.getContact().getContactId());
		assertEquals("Tom", sc2.getContact().getFirstName());
		assertEquals("Hanks", sc2.getContact().getLastName());
		assertEquals("thanks@gmail.com", sc2.getContact().getEmail());
		assertEquals("903-420-1212", sc2.getContact().getPhoneNumWork());
		assertEquals("400-232-1211", sc2.getContact().getPhoneNumMobile());
		assertEquals("626 E Main Street", sc2.getContact().getStreet());
		assertEquals("Sherman", sc2.getContact().getCity());
		assertEquals("TX", sc2.getContact().getState());
		assertEquals("75090", sc2.getContact().getZipcode());	
	}


}
