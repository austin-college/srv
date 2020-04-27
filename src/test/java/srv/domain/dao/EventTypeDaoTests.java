package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.EventType;
import srv.domain.event.EventTypeDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")

class EventTypeDaoTests {
	
	@Autowired
	EventTypeDao dao;
	
	/*
	 * Testing fetchEventTypeById
	 * should return the EventType info for the Event with id i.
	 */
	@Test
	void testFetchEventTypeById_whenUsingJdbcTemplate() throws Exception {

		// test that et1 can be fetched
		int id1 = 1;
		EventType et01 = dao.fetchEventTypeById(id1);

		assertEquals(id1, et01.getEtid());
		assertEquals("Dummy Event Type 1", et01.getName());

		// test that et2 can be fetched
		int id2 = 2;
		EventType et02 = dao.fetchEventTypeById(id2);

		assertEquals(id2, et02.getEtid());
		assertEquals("Dummy Event Type 2", et02.getName());

		// test that et3 can be fetched
		int id3 = 3;
		EventType et03 = dao.fetchEventTypeById(id3);

		assertEquals(id3, et03.getEtid());
		assertEquals("Dummy Event Type 3", et03.getName());

	}
	
	/*
	 * Testing listAll(), should return the current 3 Event Types that
	 * are in the data.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		List<EventType> events = dao.listAll();

		assertEquals(1, events.get(0).getEtid());
		assertEquals("Dummy Event Type 1", events.get(0).getName());

		assertEquals(2, events.get(1).getEtid());
		assertEquals("Dummy Event Type 2", events.get(1).getName());

		assertEquals(3, events.get(2).getEtid());
		assertEquals("Dummy Event Type 3", events.get(2).getName());

	}
	
	
	/* Testing the create(), should create a new Event Type query 
	 * in the data.sql database.
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception{
		EventType et1 = dao.create("Dummy Event 4", "Dummy Event 4 Description");
		EventType et2 = dao.create("Dummy Event 5", "Dummy Event 5 Description");
		
		EventType et3 = dao.fetchEventTypeById(et1.getEtid());
		assertEquals(et1.getName(), et2.getName());
		
		EventType et4 = dao.fetchEventTypeById(et2.getEtid());
		assertEquals(et3.getName(), et4.getName());
	}
	
	/*
	 *  Testing the delete(), should remove the query with the specified ID (first one in this case). 
	 *  Should still be one query left in the database.
	 */
	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception{
		
		// checks to see that event type with id 1 exists 
		EventType et1 = dao.fetchEventTypeById(1);
		
		int size = dao.listAll().size();
		
		assertEquals(1, et1.getEtid());
		assertEquals("Dummy Event 1", et1.getName());
		assertEquals(size,dao.listAll().size());
		
		// deletes event type with id 1
		dao.delete(1);
		
		// check if event type is deleted
		assertEquals(null,dao.fetchEventTypeById(1));
		assertEquals(2,dao.listAll().size());
		
		// checks event type with id 2 exists
		et1 = dao.fetchEventTypeById(2);
		
		assertEquals(2, et1.getEtid());
		assertEquals("Dummy Event 2", et1.getName());
		assertEquals(size - 1, dao.listAll().size());
		
		// delete event type with id 2
		dao.delete(2);
		
		// check if event type is deleted
		assertEquals(null, dao.fetchEventTypeById(2));
		assertEquals(size - 2, dao.listAll().size());
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception{
		
		int id = 1;
		EventType et1 = dao.fetchEventTypeById(id);
		
		assertEquals(id, et1.getEtid());
		assertEquals("Dummy Event 1", et1.getName());
		
		String newEventType = "New EventType";
		String newContent = "New Content";
		
		dao.update(id, newEventType, newContent);
		
		et1 = dao.fetchEventTypeById(id);
		
		assertEquals(newEventType, et1.getName());
	}
	
	
}
