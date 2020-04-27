package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.eventParticipant.EventParticipant;
import srv.domain.event.eventParticipant.EventParticipantDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class EventParticipantsDaoTests {

	@Autowired
	EventParticipantDao dao;

	/*
	 * Testing fetchEventById(int i) should return the EventParticipant info for the
	 * EventParticipant with id i.
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {

		// test that e1 can be fetched
		int id1 = 1;
		EventParticipant e1 = dao.fetchEventParticipantById(id1);

		assertEquals(id1, e1.getEventParticipantId());

		// test that e2 can be fetched
		int id2 = 2;
		EventParticipant e2 = dao.fetchEventParticipantById(id2);

		assertEquals(id2, e2.getEventParticipantId());

		// test that e3 can be fetched
		int id3 = 3;
		EventParticipant e3 = dao.fetchEventParticipantById(id3);

		assertEquals(id3, e3.getEventParticipantId());
	}

	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		List<EventParticipant> events = dao.listAll();

		assertEquals(1, events.get(0).getEventParticipantId());

		assertEquals(2, events.get(1).getEventParticipantId());

		assertEquals(3, events.get(2).getEventParticipantId());
	}

	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {

		// System.err.println("Size of list before create is " + dao.listAll().size());

		EventParticipant e1 = dao.create(2, 3);
		EventParticipant e3 = dao.create(2, 2);
		EventParticipant e2 = dao.fetchEventParticipantById(e1.getEventParticipantId());

		assertEquals(e1.getUser().getUid(), e2.getUser().getUid());

		EventParticipant e4 = dao.fetchEventParticipantById(e3.getEventParticipantId());

		assertEquals(e3.getUser().getUid(), e4.getUser().getUid());

	}

	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception {

		// checks to see that Event with id 1 exists then
		EventParticipant e1 = dao.fetchEventParticipantById(1);

		int size = dao.listAll().size();

		assertEquals(1, e1.getEventParticipantId());

		assertEquals(size, dao.listAll().size());

		// deletes Event with id 1
		dao.delete(1);

		// verifies its been deleted
		assertEquals(null, dao.fetchEventParticipantById(1));
		assertEquals(size - 1, dao.listAll().size());

		// checks Event with id 2 exists
		e1 = dao.fetchEventParticipantById(2);

		assertEquals(2, e1.getEventParticipantId());

		// deletes Event with id 2
		dao.delete(2);

		// verifies its been deleted
		assertEquals(null, dao.fetchEventParticipantById(2));
		assertEquals(size - 2, dao.listAll().size());
	}

	@Test
	void testFetchByEventID_whenUsingJdbcTemplate() throws Exception {

		List<EventParticipant> events = dao.fetchAllEventParticipantsByEventId(1);

		assertEquals(1, events.get(0).getEvent().getEid());
		assertEquals(2, events.size());
	}
}
