package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class ServiceClientDaoTests {
	
	@Autowired
	ServiceClientDao dao; 
	
	/*
	 * Testing fetchClientById() should return the first service client info in the
	 * list.
	 */
	@Test
	void testGetById_whenUsingJdbcTemplate() throws Exception {

		

		ServiceClient sc1 = dao.fetchClientById(1);

		assertEquals(1, sc1.getScid());
		assertEquals("Habitat for Humanity", sc1.getName());
		assertEquals("Billy Bob", sc1.getBoardMember());
		assertEquals("Community", sc1.getCategory());

		// Testing primary Contact info
		assertEquals(1, sc1.getMainContact().getContactId());
		assertEquals("Tom", sc1.getMainContact().getFirstName());
		assertEquals("Hanks", sc1.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc1.getMainContact().getEmail());
		assertEquals("903-420-1212", sc1.getMainContact().getPhoneNumWork());
		assertEquals("400-232-1211", sc1.getMainContact().getPhoneNumMobile());
		assertEquals("626 E Main Street", sc1.getMainContact().getStreet());
		assertEquals("Sherman", sc1.getMainContact().getCity());
		assertEquals("TX", sc1.getMainContact().getState());
		assertEquals("75090", sc1.getMainContact().getZipcode());

		// Testing secondary Contact info
		assertEquals(4, sc1.getOtherContact().getContactId());
		assertEquals("Susan", sc1.getOtherContact().getFirstName());
		assertEquals("Atkins", sc1.getOtherContact().getLastName());
		assertEquals("satkins67@gmail.com", sc1.getOtherContact().getEmail());
		assertEquals("803-426-1527", sc1.getOtherContact().getPhoneNumWork());
		assertEquals("800-191-9412", sc1.getOtherContact().getPhoneNumMobile());
		assertEquals("23 First Street", sc1.getOtherContact().getStreet());
		assertEquals("Denison", sc1.getOtherContact().getCity());
		assertEquals("TX", sc1.getOtherContact().getState());
		assertEquals("75021", sc1.getOtherContact().getZipcode());
	}

	/*
	 * Testing listAll(), should return the current 2 service client entries that
	 * are in the data.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {


		List<ServiceClient> clients = dao.listAll();

		assertEquals(3, clients.size());

		ServiceClient sc1 = clients.get(0);
		ServiceClient sc2 = clients.get(1);

		assertEquals(1, sc1.getScid());
		assertEquals(2, sc2.getScid());

		// Service Client info for client id 1
		assertEquals("Habitat for Humanity", sc1.getName());
		assertEquals("Billy Bob", sc1.getBoardMember());
		assertEquals("Community", sc1.getCategory());

		// Testing primary Contact info for service client 1
		assertEquals(1, sc1.getMainContact().getContactId());
		assertEquals("Tom", sc1.getMainContact().getFirstName());
		assertEquals("Hanks", sc1.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc1.getMainContact().getEmail());
		assertEquals("903-420-1212", sc1.getMainContact().getPhoneNumWork());
		assertEquals("400-232-1211", sc1.getMainContact().getPhoneNumMobile());
		assertEquals("626 E Main Street", sc1.getMainContact().getStreet());
		assertEquals("Sherman", sc1.getMainContact().getCity());
		assertEquals("TX", sc1.getMainContact().getState());
		assertEquals("75090", sc1.getMainContact().getZipcode());

		// Testing secondary Contact info for service client 1
		assertEquals(4, sc1.getOtherContact().getContactId());
		assertEquals("Susan", sc1.getOtherContact().getFirstName());
		assertEquals("Atkins", sc1.getOtherContact().getLastName());
		assertEquals("satkins67@gmail.com", sc1.getOtherContact().getEmail());
		assertEquals("803-426-1527", sc1.getOtherContact().getPhoneNumWork());
		assertEquals("800-191-9412", sc1.getOtherContact().getPhoneNumMobile());
		assertEquals("23 First Street", sc1.getOtherContact().getStreet());
		assertEquals("Denison", sc1.getOtherContact().getCity());
		assertEquals("TX", sc1.getOtherContact().getState());
		assertEquals("75021", sc1.getOtherContact().getZipcode());

		// Service Client info for client id 2
		assertEquals("Crisis Center", sc2.getName());
		assertEquals("Rick Astley", sc2.getBoardMember());
		assertEquals("Crisis Support", sc2.getCategory());

		// Testing Contact info for service client id 2
		assertEquals(2, sc2.getMainContact().getContactId());
		assertEquals("Lois", sc2.getMainContact().getFirstName());
		assertEquals("Lane", sc2.getMainContact().getLastName());
		assertEquals("llane86@gmail.com", sc2.getMainContact().getEmail());
		assertEquals("803-423-1257", sc2.getMainContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc2.getMainContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc2.getMainContact().getStreet());
		assertEquals("Sherman", sc2.getMainContact().getCity());
		assertEquals("TX", sc2.getMainContact().getState());
		assertEquals("75090", sc2.getMainContact().getZipcode());

		// Testing secondary Contact info for service client 1
		assertEquals(3, sc2.getOtherContact().getContactId());
		assertEquals("Joe", sc2.getOtherContact().getFirstName());
		assertEquals("Smith", sc2.getOtherContact().getLastName());
		assertEquals("jsmith12@gmail.com", sc2.getOtherContact().getEmail());
		assertEquals("903-444-4440", sc2.getOtherContact().getPhoneNumWork());
		assertEquals("401-322-1201", sc2.getOtherContact().getPhoneNumMobile());
		assertEquals("25 Frieda Drive", sc2.getOtherContact().getStreet());
		assertEquals("Gunter", sc2.getOtherContact().getCity());
		assertEquals("TX", sc2.getOtherContact().getState());
		assertEquals("75058", sc2.getOtherContact().getZipcode());
	}


	/*
	 * Testing the create(), should create a new Service Client query in the
	 * 
	 * WARNING:  Smelly code alert;  This test depends on listAll method working.
	 * 
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {

		List<ServiceClient> clientsBefore = dao.listAll(); 
		int numBeforeInsert = clientsBefore.size();
		System.err.println("\n\nBefore Insert " + numBeforeInsert);
		for (ServiceClient sc : clientsBefore) {
			System.err.println(sc.getName());
		}
		
		
		
		ServiceClient nsc = dao.create("Meals on Wheels", 2, 1, "Donald Duck", "Seniors");
		
		assertNotNull(nsc);
		
		List<ServiceClient> clientsAfter = dao.listAll(); 
		int numAfterInsert = clientsAfter.size();
		
		System.err.println("\n\nAfter Insert " + numAfterInsert);
		for (ServiceClient sc : clientsBefore) {
			System.err.println(sc.getName());
		}

		
		
		/*
		 * The next assigned id on successful insert should be numBeforeInsert + 1.
		 */
		assertEquals(numBeforeInsert+1, nsc.getScid());

		
		/*
		 * Now we will examine the newly inserted record.
		 */
		ServiceClient sc4 = nsc;

		assertEquals(4, sc4.getScid());
		assertEquals("Meals on Wheels", sc4.getName());
		assertEquals("Donald Duck", sc4.getBoardMember());
		assertEquals("Seniors", sc4.getCategory());




		// Testing primary Contact info for service client returned from create
		assertEquals(2, sc4.getMainContact().getContactId());
		assertEquals("Lois", sc4.getMainContact().getFirstName());
		assertEquals("Lane", sc4.getMainContact().getLastName());
		assertEquals("llane86@gmail.com", sc4.getMainContact().getEmail());
		assertEquals("803-423-1257", sc4.getMainContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc4.getMainContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc4.getMainContact().getStreet());
		assertEquals("Sherman", sc4.getMainContact().getCity());
		assertEquals("TX", sc4.getMainContact().getState());
		assertEquals("75090", sc4.getMainContact().getZipcode());

		// Testing secondary Contact info for service client returned from create
		assertEquals(1, sc4.getOtherContact().getContactId()); 
		assertEquals("Tom", sc4.getOtherContact().getFirstName()); 
		assertEquals("Hanks", sc4.getOtherContact().getLastName()); 
		assertEquals("thanks@gmail.com", sc4.getOtherContact().getEmail()); 
		assertEquals("903-420-1212", sc4.getOtherContact().getPhoneNumWork()); 
		assertEquals("400-232-1211", sc4.getOtherContact().getPhoneNumMobile()); 
		assertEquals("626 E Main Street", sc4.getOtherContact().getStreet()); 
		assertEquals("Sherman", sc4.getOtherContact().getCity()); 
		assertEquals("TX", sc4.getOtherContact().getState()); 
		assertEquals("75090", sc4.getOtherContact().getZipcode());

	}

	/*
	 * Testing the delete(), should remove the query with the specified ID (first
	 * one in this case). Should still be one query left in the database.
	 */
	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {


		dao.delete(3);

		List<ServiceClient> clients = dao.listAll();

		assertEquals(2, clients.size());
		
		
		
	}

	/*
	 * Testing the update(), should update the query with the specified ID. Switched
	 * the primary contact id to point to the first entry in the contacts table
	 * rather than the second entry.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {

		dao.update(2, "Meals on Wheels", 1, 2, "Rick Astley", "Seniors");

		ServiceClient sc2 = dao.fetchClientById(2);

		// Service Client info for client id 2
		assertEquals("Meals on Wheels", sc2.getName());
		assertEquals("Rick Astley", sc2.getBoardMember());
		assertEquals("Seniors", sc2.getCategory());

		// Testing primary Contact info for service client 1
		assertEquals(1, sc2.getMainContact().getContactId());
		assertEquals("Tom", sc2.getMainContact().getFirstName());
		assertEquals("Hanks", sc2.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc2.getMainContact().getEmail());
		assertEquals("903-420-1212", sc2.getMainContact().getPhoneNumWork());
		assertEquals("400-232-1211", sc2.getMainContact().getPhoneNumMobile());
		assertEquals("626 E Main Street", sc2.getMainContact().getStreet());
		assertEquals("Sherman", sc2.getMainContact().getCity());
		assertEquals("TX", sc2.getMainContact().getState());
		assertEquals("75090", sc2.getMainContact().getZipcode());

		// Testing secondary Contact info for service client id 2
		assertEquals(2, sc2.getOtherContact().getContactId());
		assertEquals("Lois", sc2.getOtherContact().getFirstName());
		assertEquals("Lane", sc2.getOtherContact().getLastName());
		assertEquals("llane86@gmail.com", sc2.getOtherContact().getEmail());
		assertEquals("803-423-1257", sc2.getOtherContact().getPhoneNumWork());
		assertEquals("800-232-1211", sc2.getOtherContact().getPhoneNumMobile());
		assertEquals("118 NW Crawford Street", sc2.getOtherContact().getStreet());
		assertEquals("Sherman", sc2.getOtherContact().getCity());
		assertEquals("TX", sc2.getOtherContact().getState());
		assertEquals("75090", sc2.getOtherContact().getZipcode());
	}

}
