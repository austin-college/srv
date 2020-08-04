package srv.domain.dao;

import static org.junit.Assert.assertTrue;
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
	 * Testing fetchClientById() should return the second service client info in the
	 * list.
	 */
	@Test
	void testGetById() throws Exception {

		ServiceClient sc1 = dao.fetchClientById(2);

		assertEquals(2, sc1.getScid());
		assertEquals("Austin College Service Station", sc1.getName());
		assertEquals("Variety", sc1.getCategory());
		
		// Testing board member info
		assertEquals(1, sc1.getCurrentBoardMember().getUid());
		assertEquals("apritchard", sc1.getCurrentBoardMember().getUsername());
		assertEquals(5, sc1.getCurrentBoardMember().getContactInfo().getContactId());

		// Testing primary Contact info
		assertEquals(1, sc1.getMainContact().getContactId());
		assertEquals("Tom", sc1.getMainContact().getFirstName());
		assertEquals("Hanks", sc1.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc1.getMainContact().getEmail());
		assertEquals("903-420-1212", sc1.getMainContact().getPrimaryPhone());
		assertEquals("400-232-1211", sc1.getMainContact().getSecondaryPhone());
		assertEquals("626 E Main Street", sc1.getMainContact().getStreet());
		assertEquals("Sherman", sc1.getMainContact().getCity());
		assertEquals("TX", sc1.getMainContact().getState());
		assertEquals("75090", sc1.getMainContact().getZipcode());

		// Testing secondary Contact info
		assertEquals(4, sc1.getOtherContact().getContactId());
		assertEquals("Susan", sc1.getOtherContact().getFirstName());
		assertEquals("Atkins", sc1.getOtherContact().getLastName());
		assertEquals("satkins67@gmail.com", sc1.getOtherContact().getEmail());
		assertEquals("803-426-1527", sc1.getOtherContact().getPrimaryPhone());
		assertEquals("800-191-9412", sc1.getOtherContact().getSecondaryPhone());
		assertEquals("23 First Street", sc1.getOtherContact().getStreet());
		assertEquals("Denison", sc1.getOtherContact().getCity());
		assertEquals("TX", sc1.getOtherContact().getState());
		assertEquals("75021", sc1.getOtherContact().getZipcode());
	}

	/*
	 * Testing listAll(), should return the current 2 service client entries that
	 * are in the schema.sql database.
	 */
	@Test
	void testListAll() throws Exception {

		List<ServiceClient> clients = dao.listAll();

		assertEquals(5, clients.size());

		ServiceClient sc2 = clients.get(1);
		ServiceClient sc4 = clients.get(3);

		assertEquals(2, sc2.getScid());
		assertEquals(4, sc4.getScid());

		// Service Client info for client id 1
		assertEquals("Austin College Service Station", sc2.getName());
		assertEquals("Variety", sc2.getCategory());
		
		// Testing board member info for service client 1
		assertEquals(1, sc2.getCurrentBoardMember().getUid());
		assertEquals("apritchard", sc2.getCurrentBoardMember().getUsername());
		assertEquals(5, sc2.getCurrentBoardMember().getContactInfo().getContactId());

		// Testing primary Contact info for service client 1
		assertEquals(1, sc2.getMainContact().getContactId());
		assertEquals("Tom", sc2.getMainContact().getFirstName());
		assertEquals("Hanks", sc2.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc2.getMainContact().getEmail());
		assertEquals("903-420-1212", sc2.getMainContact().getPrimaryPhone());
		assertEquals("400-232-1211", sc2.getMainContact().getSecondaryPhone());
		assertEquals("626 E Main Street", sc2.getMainContact().getStreet());
		assertEquals("Sherman", sc2.getMainContact().getCity());
		assertEquals("TX", sc2.getMainContact().getState());
		assertEquals("75090", sc2.getMainContact().getZipcode());

		// Testing secondary Contact info for service client 1
		assertEquals(4, sc2.getOtherContact().getContactId());
		assertEquals("Susan", sc2.getOtherContact().getFirstName());
		assertEquals("Atkins", sc2.getOtherContact().getLastName());
		assertEquals("satkins67@gmail.com", sc2.getOtherContact().getEmail());
		assertEquals("803-426-1527", sc2.getOtherContact().getPrimaryPhone());
		assertEquals("800-191-9412", sc2.getOtherContact().getSecondaryPhone());
		assertEquals("23 First Street", sc2.getOtherContact().getStreet());
		assertEquals("Denison", sc2.getOtherContact().getCity());
		assertEquals("TX", sc2.getOtherContact().getState());
		assertEquals("75021", sc2.getOtherContact().getZipcode());

		// Service Client info for client id 3
		assertEquals("Crisis Center", sc4.getName());
		assertEquals("Crisis Support", sc4.getCategory());
		
		// Testing board member info for service client id 3
		assertEquals(1, sc4.getCurrentBoardMember().getUid());
		assertEquals("apritchard", sc4.getCurrentBoardMember().getUsername());
		assertEquals(5, sc4.getCurrentBoardMember().getContactInfo().getContactId());
				
		// Testing primary Contact info for service client id 3
		assertEquals(3, sc4.getMainContact().getContactId());
		assertEquals("Joe", sc4.getMainContact().getFirstName());
		assertEquals("Smith", sc4.getMainContact().getLastName());
		assertEquals("jsmith12@gmail.com", sc4.getMainContact().getEmail());
		assertEquals("903-444-4440", sc4.getMainContact().getPrimaryPhone());
		assertEquals("401-322-1201", sc4.getMainContact().getSecondaryPhone());
		assertEquals("25 Frieda Drive", sc4.getMainContact().getStreet());
		assertEquals("Gunter", sc4.getMainContact().getCity());
		assertEquals("TX", sc4.getMainContact().getState());
		assertEquals("75058", sc4.getMainContact().getZipcode());
		
		// Testing secondary Contact info for service client id 3
		assertEquals(4, sc4.getOtherContact().getContactId());
		assertEquals("Susan", sc4.getOtherContact().getFirstName());
		assertEquals("Atkins", sc4.getOtherContact().getLastName());
		assertEquals("satkins67@gmail.com", sc4.getOtherContact().getEmail());
		assertEquals("803-426-1527", sc4.getOtherContact().getPrimaryPhone());
		assertEquals("800-191-9412", sc4.getOtherContact().getSecondaryPhone());
		assertEquals("23 First Street", sc4.getOtherContact().getStreet());
		assertEquals("Denison", sc4.getOtherContact().getCity());
		assertEquals("TX", sc4.getOtherContact().getState());
		assertEquals("75021", sc4.getOtherContact().getZipcode());
	}


	/*
	 * Testing the create(), should create a new Service Client query in the database
	 * 
	 * WARNING:  Smelly code alert;  This test depends on listAll method working.
	 * 
	 */
	@Test
	void testCreate() throws Exception {

		List<ServiceClient> clientsBefore = dao.listAll(); 
		int numBeforeInsert = clientsBefore.size();
		System.err.println("\n\nBefore Insert " + numBeforeInsert);
		for (ServiceClient sc : clientsBefore) {
			System.err.println(sc.getName());
		}
		
		
		ServiceClient nsc = dao.create("Meals on Wheels", 2, 1, 1, "Seniors");
		
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
		ServiceClient sc5 = nsc;

		assertTrue(sc5.getScid()>numBeforeInsert);
		
		assertEquals("Meals on Wheels", sc5.getName());
		assertEquals("Seniors", sc5.getCategory());
		
		// Testing board member info for service client returned from create
		assertEquals(1, sc5.getCurrentBoardMember().getUid());
		assertEquals("apritchard", sc5.getCurrentBoardMember().getUsername());
		assertEquals(5, sc5.getCurrentBoardMember().getContactInfo().getContactId());

		// Testing primary Contact info for service client returned from create
		assertEquals(2, sc5.getMainContact().getContactId());
		assertEquals("Lois", sc5.getMainContact().getFirstName());
		assertEquals("Lane", sc5.getMainContact().getLastName());
		assertEquals("llane86@gmail.com", sc5.getMainContact().getEmail());
		assertEquals("803-423-1257", sc5.getMainContact().getPrimaryPhone());
		assertEquals("800-232-1211", sc5.getMainContact().getSecondaryPhone());
		assertEquals("118 NW Crawford Street", sc5.getMainContact().getStreet());
		assertEquals("Sherman", sc5.getMainContact().getCity());
		assertEquals("TX", sc5.getMainContact().getState());
		assertEquals("75090", sc5.getMainContact().getZipcode());

		// Testing secondary Contact info for service client returned from create
		assertEquals(1, sc5.getOtherContact().getContactId()); 
		assertEquals("Tom", sc5.getOtherContact().getFirstName()); 
		assertEquals("Hanks", sc5.getOtherContact().getLastName()); 
		assertEquals("thanks@gmail.com", sc5.getOtherContact().getEmail()); 
		assertEquals("903-420-1212", sc5.getOtherContact().getPrimaryPhone()); 
		assertEquals("400-232-1211", sc5.getOtherContact().getSecondaryPhone()); 
		assertEquals("626 E Main Street", sc5.getOtherContact().getStreet()); 
		assertEquals("Sherman", sc5.getOtherContact().getCity()); 
		assertEquals("TX", sc5.getOtherContact().getState()); 
		assertEquals("75090", sc5.getOtherContact().getZipcode());

	}

	/*
	 * Testing the delete(), should remove the query with the specified ID (first
	 * one in this case). Should still be one query left in the database.
	 */
	@Test
	void testDelete() throws Exception {

		int cnum = dao.listAll().size();

		dao.delete(3);

		List<ServiceClient> clients = dao.listAll();

		assertEquals(cnum-1, clients.size());
	}

	/*
	 * Testing the update(), should update the query with the specified ID. Switched
	 * the primary contact id to point to the first entry in the contacts table
	 * rather than the second entry.
	 */
	@Test
	void testUpdate() throws Exception {

		dao.update(2, "Meals on Wheels", 1, 2, 4, "Seniors");

		ServiceClient sc2 = dao.fetchClientById(2);

		// Service Client info for client id 2
		assertEquals("Meals on Wheels", sc2.getName());
		assertEquals("Seniors", sc2.getCategory());
		
		// Testing board member info for service client returned from create
		assertEquals(4, sc2.getCurrentBoardMember().getUid());
		assertEquals("user", sc2.getCurrentBoardMember().getUsername());
		assertEquals(1, sc2.getCurrentBoardMember().getContactInfo().getContactId());

		// Testing primary Contact info for service client id 2
		assertEquals(1, sc2.getMainContact().getContactId());
		assertEquals("Tom", sc2.getMainContact().getFirstName());
		assertEquals("Hanks", sc2.getMainContact().getLastName());
		assertEquals("thanks@gmail.com", sc2.getMainContact().getEmail());
		assertEquals("903-420-1212", sc2.getMainContact().getPrimaryPhone());
		assertEquals("400-232-1211", sc2.getMainContact().getSecondaryPhone());
		assertEquals("626 E Main Street", sc2.getMainContact().getStreet());
		assertEquals("Sherman", sc2.getMainContact().getCity());
		assertEquals("TX", sc2.getMainContact().getState());
		assertEquals("75090", sc2.getMainContact().getZipcode());

		// Testing secondary Contact info for service client id 2
		assertEquals(2, sc2.getOtherContact().getContactId());
		assertEquals("Lois", sc2.getOtherContact().getFirstName());
		assertEquals("Lane", sc2.getOtherContact().getLastName());
		assertEquals("llane86@gmail.com", sc2.getOtherContact().getEmail());
		assertEquals("803-423-1257", sc2.getOtherContact().getPrimaryPhone());
		assertEquals("800-232-1211", sc2.getOtherContact().getSecondaryPhone());
		assertEquals("118 NW Crawford Street", sc2.getOtherContact().getStreet());
		assertEquals("Sherman", sc2.getOtherContact().getCity());
		assertEquals("TX", sc2.getOtherContact().getState());
		assertEquals("75090", sc2.getOtherContact().getZipcode());
	}

}
