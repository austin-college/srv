package srv.domain.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventParticipant.EventParticipant;

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
	void testFetchById_whenUsingJdbcTemplate() throws Exception {

		// test that e1 can be fetched
		int id1 = 1;
		Event e1 = dao.fetchEventById(id1);

		assertEquals(id1, e1.getEid());

		assertEquals("Dummy Event 1", e1.getTitle());

		// test that e2 can be fetched
		int id2 = 2;
		Event e2 = dao.fetchEventById(id2);

		assertEquals(id2, e2.getEid());

		assertEquals("Dummy Event 2", e2.getTitle());

		// test that e3 can be fetched
		int id3 = 3;
		Event e3 = dao.fetchEventById(id3);

		assertEquals(id3, e3.getEid());

		assertEquals("Dummy Event 3", e3.getTitle());

	}

	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		List<Event> events = dao.listAll();

		// making sure events are not null
		Event e1 = events.get(0);
		assertNotNull(e1);
		
		Event e2 = events.get(1);
		assertNotNull(e2);
		
		Event e3 = events.get(2);
		assertNotNull(e3); 
		
		//testing contents of e1
		assertEquals(1, e1.getEid());
		assertEquals("Dummy Event 1", e1.getTitle());
		assertEquals("Dummy Address 1", e1.getAddress());
		assertEquals(1, e1.getContact().getContactId());
		assertEquals("01/01/2020", e1.getDate());
		assertEquals("JANSERVE", e1.getTypeString()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e1.isContinuous());
		assertEquals(5, e1.getVolunteersNeeded());
		assertEquals(1, e1.getServiceClient().getScid());
		assertEquals(5.0, e1.getNeededVolunteerHours());
		assertEquals(3.0, e1.getRsvpVolunteerHours());
		assertEquals("free text field", e1.getFreeTextField());
	
		// testing contents of e2
		assertEquals(2, e2.getEid());
		assertEquals("Dummy Event 2", e2.getTitle());
		assertEquals("Dummy Address 2", e2.getAddress());
		assertEquals(2, e2.getContact().getContactId());
		assertEquals("05/05/2020", e2.getDate());
		assertEquals("GREENSERVE", e2.getTypeString()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e2.isContinuous());
		assertEquals(10, e2.getVolunteersNeeded());
		assertEquals(2, e2.getServiceClient().getScid());
		assertEquals(3.0, e2.getNeededVolunteerHours());
		assertEquals(1.5, e2.getRsvpVolunteerHours());
		assertEquals("free text field", e2.getFreeTextField());

		// testing contents of e3
		assertEquals(3, e3.getEid());
		assertEquals("Dummy Event 3", e3.getTitle());
		assertEquals("Dummy Address 3", e3.getAddress());
		assertEquals(3, e3.getContact().getContactId());
		assertEquals("03/03/2020", e3.getDate());
		assertEquals("HELPFUL EVENT", e3.getTypeString()); // testing hardcoded example in schema - eventTypeDao not created yet
		assertFalse(e3.isContinuous());
		assertEquals(15, e3.getVolunteersNeeded());
		assertEquals(1, e3.getServiceClient().getScid());
		assertEquals(4.0, e3.getNeededVolunteerHours());
		assertEquals(2.0, e3.getRsvpVolunteerHours());
		assertEquals("free text field", e3.getFreeTextField());

	}

	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {

		// System.err.println("Size of list before create is " + dao.listAll().size());

		Event e1 = dao.create("Dummy Event 4", "Dummy Address 4", 1, "EVENT TYPE", "/0/0/0000", false, 5, 1, 0, 0, "");
		Event e3 = dao.create("Dummy Event 5", "Dummy Address 5", 1, "EVENT TYPE", "/0/0/0000", false, 5, 2, 0, 0, "");

		Event e2 = dao.fetchEventById(e1.getEid());

		assertEquals(e1.getTitle(), e2.getTitle());

		Event e4 = dao.fetchEventById(e3.getEid());

		assertEquals(e3.getTitle(), e4.getTitle());

	}

	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {

		// checks to see that Event with id 1 exists then
		Event e1 = dao.fetchEventById(1);

		int size = dao.listAll().size();

		assertEquals(1, e1.getEid());

		assertEquals("Dummy Event 1", e1.getTitle());

		assertEquals(size, dao.listAll().size());

		// deletes Event with id 1
		dao.delete(1);

		// verifies its been deleted
		assertEquals(null, dao.fetchEventById(1));
		assertEquals(2, dao.listAll().size());

		// checks Event with id 2 exists
		e1 = dao.fetchEventById(2);

		assertEquals(2, e1.getEid());

		assertEquals("Dummy Event 2", e1.getTitle());

		assertEquals(size - 1, dao.listAll().size());

		// deletes Event with id 2
		dao.delete(2);

		// verifies its been deleted
		assertEquals(null, dao.fetchEventById(2));
		assertEquals(size - 2, dao.listAll().size());
	}

	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception {

		int id = 1;
		Event e1 = dao.fetchEventById(id);

		assertEquals(id, e1.getEid());

		assertEquals("Dummy Event 1", e1.getTitle());

		String newEventname = "new Eventname";
		int newContact = 5;

		// update each item

		dao.update(id, newEventname, e1.getAddress(), newContact, e1.getDate(), "", e1.isContinuous(),
				e1.getVolunteersNeeded(), (int) e1.getServiceClient().getScid(), 0.0, 0.0, " ");

		e1 = dao.fetchEventById(id);

		assertEquals(newEventname, e1.getTitle());

	}

//	/*
//	 * Tests getParticipants()
//	 */
//	@Test
//	void testGetParticipants_whenUsingJdbcTemplate() throws Exception {
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