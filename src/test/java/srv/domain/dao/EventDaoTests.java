package srv.domain.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.Event;
import srv.domain.event.EventDao;

/**
 * NOTE: these tests have been made without the implementation of the EventType dao. 
 * There is a dummy variable (typeString) holding the place of the eventType column. 
 * DAO, JdbcTemplate, and tests need to be updated when EventTypeDao is implemented. 
 * 
 * @author fancynine9
 *
 */
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class EventDaoTests {

	@Autowired
	EventDao dao;

	/*
	 * Testing fetchEventById(int i) should return the Event info for the Event with
	 * id i.
	 */
	@Test
	void testFetchById() throws Exception {

		// test that e1 can be fetched
		int id1 = 1;
		Event e1 = dao.fetchEventById(id1);

		assertEquals(id1, e1.getEid());

		assertEquals("GDS2020", e1.getTitle());

	}

	/**
	 * Tests the listAll method in the JdbcTemplateEventDao and makes sure 
	 * the contents of each event are correctly gotten. 
	 * 
	 * @throws Exception
	 */
	@Test
	void testListAll() throws Exception {

		List<Event> events = dao.listAll();

		// making sure events are not null
		Event e1 = events.get(0);
		assertNotNull(e1);
		
		Event e2 = events.get(1);
		assertNotNull(e2);
		
		Event e3 = events.get(2);
		assertNotNull(e3); 
			
		/*
		 * ('GDS2020', 'distributed', 1, '01/01/2020', 1, false, 5, 1, 5.0, 3.0, 'free text field');
		 */
		//testing contents of e1
		assertEquals(1, e1.getEid());
		assertEquals("GDS2020", e1.getTitle());
		assertEquals("distributed", e1.getAddress());
		assertEquals(1, e1.getContact().getContactId());
		assertEquals("2020-01-01 00:00:00.0", e1.getDate().toString());
		assertEquals(1, e1.getType().getEtid()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e1.isContinuous());
		assertEquals(5, e1.getVolunteersNeeded());
		assertEquals(1, e1.getServiceClient().getScid());
		assertEquals(5.0, e1.getNeededVolunteerHours());
		assertEquals(3.0, e1.getRsvpVolunteerHours());
		assertEquals("free text field", e1.getNote());
	
		// testing contents of e2
		assertEquals(2, e2.getEid());
		assertEquals("Dummy Event 2", e2.getTitle());
		assertEquals("Dummy Address 2", e2.getAddress());
		assertEquals(2, e2.getContact().getContactId());
		assertEquals("2020-05-05 00:00:00.0", e2.getDate().toString());
		assertEquals(2, e2.getType().getEtid()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e2.isContinuous());
		assertEquals(10, e2.getVolunteersNeeded());
		assertEquals(2, e2.getServiceClient().getScid());
		assertEquals(3.0, e2.getNeededVolunteerHours());
		assertEquals(1.5, e2.getRsvpVolunteerHours());
		assertEquals("free text field", e2.getNote());

		// testing contents of e3
		assertEquals(3, e3.getEid());
		assertEquals("Dummy Event 3", e3.getTitle());
		assertEquals("Dummy Address 3", e3.getAddress());
		assertEquals(3, e3.getContact().getContactId());
		assertEquals("2020-03-03 00:00:00.0", e3.getDate().toString());
		assertEquals(3, e3.getType().getEtid()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e3.isContinuous());
		assertEquals(15, e3.getVolunteersNeeded());
		assertEquals(1, e3.getServiceClient().getScid());
		assertEquals(4.0, e3.getNeededVolunteerHours());
		assertEquals(2.0, e3.getRsvpVolunteerHours());
		assertEquals("free text field", e3.getNote());

	}

	/**
	 * Tests the create method in Dao by checking size, and getting the contents 
	 * of the newly created event.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreate_whenNullsPresent() throws Exception {

		/*
		 * checks the event list size and contents before we create a new one
		 */
		List<Event> eventsBefore = dao.listAll();
		int numBeforeCreate = eventsBefore.size();
		System.err.println("\n\nBefore Insert " + numBeforeCreate);
		
		for(Event e : eventsBefore) {
			
			System.err.println(e.getEid());
		}
		
		// Creating new Date object
		String sDate = "2020-03-12 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
		
		/*
		 * Creating new Event with one null for each type
		 * 			//(title, address, contactId, dateOf, eventTypeId, continuous, volunteersNeeded, serviceClientId, neededVolunteerHours, rsvpVolunteerHours, note)
		 */		
		Event ne = dao.create("EARTH DAY", null, null, date, 2, false, null, 2, null, 2.5, null);
		
		assertNotNull(ne); // checking if null
		
		/*
		 * testing size and contents of list after insert
		 */
		List<Event> eventsAfter = dao.listAll();
		int numAfterCreate = eventsAfter.size();
		
		System.err.println("\n\nAfter Insert " + numAfterCreate);
		
		for(Event e : eventsAfter) {
			System.err.println(e.getEid());
		}
		
		/*
		 * tests that next assigned id when successful should be numBeforeCreate+1.
		 * 
		 */
		assertTrue(ne.getEid() > 3);
		
		/*
		 * Checking contents of ne from eventsAfter to check contents inserted in table
		 */
		Event e4 = ne;
		
		assertEquals(4, e4.getEid());
		assertEquals("EARTH DAY", e4.getTitle());
		assertNull(e4.getAddress());
		assertNull(e4.getContact());
		
		assertEquals("2020-03-12 00:00:00.0", e4.getDate().toString());
		assertEquals(2, e4.getType().getEtid()); // needs to be replaced with getType().getEventTypeId();
		assertEquals(false, e4.isContinuous());
		assertEquals(0, e4.getVolunteersNeeded());
		assertEquals(2, e4.getServiceClient().getScid());
		assertEquals(0.0, e4.getNeededVolunteerHours());
		assertEquals(2.5, e4.getRsvpVolunteerHours());
		
		assertNull( e4.getNote());

	}
	
	
	/**
	 * Tests the create method in Dao by checking size, and getting the contents 
	 * of the newly created event.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreate() throws Exception {

		/*
		 * checks the event list size and contents before we create a new one
		 */
		List<Event> eventsBefore = dao.listAll();
		int numBeforeCreate = eventsBefore.size();
		System.err.println("\n\nBefore Insert " + numBeforeCreate);
		
		for(Event e : eventsBefore) {
			System.err.println(String.format("%d %s", e.getEid(),e.getTitle()));
		}
		
		// Creating new Date object
		String sDate = "2020-03-12 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
		
		/*
		 * Creating new Event 
		 */
		Event ne = dao.create("EARTH DAY", "Dummy Address 4", 3, date, 2, false, 10, 2, 12.0, 2.5, "save the earth!!!");
		
		assertNotNull(ne); // checking if null
		
		/*
		 * testing size and contents of list after insert
		 */
		List<Event> eventsAfter = dao.listAll();
		int numAfterCreate = eventsAfter.size();
		
		System.err.println("\n\nAfter Insert " + numAfterCreate);
		
		for(Event e : eventsAfter) {
			System.err.println(String.format("%d %s", e.getEid(),e.getTitle()));
		}
		
		/*
		 * Checking contents of ne from eventsAfter to check contents inserted in table
		 */
		assertTrue(eventsAfter.contains(ne));
		
		Event e4 = ne;

		assertTrue(e4.getEid()>4);   // assigned it must be after the first 4 created in our sql
		
		assertEquals("EARTH DAY", e4.getTitle());
		assertEquals("Dummy Address 4", e4.getAddress());
		assertEquals(3, e4.getContact().getContactId());
		assertEquals("2020-03-12 00:00:00.0", e4.getDate().toString());
		assertEquals(2, e4.getType().getEtid()); // needs to be replaced with getType().getEventTypeId();
		assertEquals(false, e4.isContinuous());
		assertEquals(10, e4.getVolunteersNeeded());
		assertEquals(2, e4.getServiceClient().getScid());
		assertEquals(12.0, e4.getNeededVolunteerHours());
		assertEquals(2.5, e4.getRsvpVolunteerHours());
		assertEquals("save the earth!!!", e4.getNote());

	}

	@Test
	void testDelete() throws Exception {

		// checks to see that Event with id 1 exists then
		Event e1 = dao.fetchEventById(1);

		int size = dao.listAll().size();

		assertEquals(1, e1.getEid());

		assertEquals("GDS2020", e1.getTitle());

		assertEquals(size, dao.listAll().size());

		// deletes Event with id 1
		dao.delete(1);

		// verifies its been deleted
		assertEquals(null, dao.fetchEventById(1));
		assertEquals(2, dao.listAll().size());

	}

	@Test
	void testUpdate() throws Exception {
		
		// Creating new Date object
		String sDate = "2020-06-16 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
				
		/*
		 * Use update dao method
		 */
		dao.update(1, 
				"COOLER EVENT", 
				"1345 Murder Hornet Dr.", 
				Integer.valueOf(1), 
				date, 
				Integer.valueOf(2), 
				Boolean.TRUE, 
				Integer.valueOf(100), 
				Integer.valueOf(1), 
				Double.valueOf(30), 
				Double.valueOf(15), 
				"they're coming");
		
		
		Event ue = dao.fetchEventById(1);
		
		/*
		 * checking contents of updated client id 1 to match update
		 */
		assertEquals(1, ue.getEid());
		assertEquals("COOLER EVENT", ue.getTitle());
		assertEquals("1345 Murder Hornet Dr.", ue.getAddress());
		assertEquals(1, ue.getContact().getContactId());
		assertEquals("2020-06-16 00:00:00.0", ue.getDate().toString());
		assertEquals(2, ue.getType().getEtid());
		assertEquals(true, ue.isContinuous());
		assertEquals(100, ue.getVolunteersNeeded());
		assertEquals(30.0, ue.getNeededVolunteerHours());
		assertEquals(15.0, ue.getRsvpVolunteerHours());
		assertEquals("they're coming", ue.getNote());
	

	}

//	/*
//	 * Tests getParticipants()
//	 */
//	@Test
//	void testGetParticipants() throws Exception {
//
//		// test that e1 can be fetched
//		int id1 = 1;
//		Event e1 = dao.fetchEventById(id1);
//
//		assertEquals(id1, e1.getEid());
//
//		assertEquals("Dummy Event 1", e1.getTitle());
//
//		assertFalse(e1.getParticipantsList() == null);
////		System.err.println("User List Size: " + usersOfEvent1.size());
//		// assertEquals("apritchard", usersOfEvent1.get(0).getUsername());
//	}
}