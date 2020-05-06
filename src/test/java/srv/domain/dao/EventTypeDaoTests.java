package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
		
		// Check existing event type in sql 1~3
		EventType et01 = dao.fetchEventTypeById(1);

		assertEquals(1, et01.getEtid());
		assertEquals("gds", et01.getName());
		assertEquals("Great Day of Service", et01.getDescription());
		assertEquals(2, et01.getDefHours());
		assertEquals(true, et01.isPinHours());
		assertEquals(1, et01.getDefClient().getScid());
		assertEquals(1, et01.getDefOrg().getSgid());

		EventType et02 = dao.fetchEventTypeById(2);

		assertEquals(2, et02.getEtid());
		assertEquals("fws", et02.getName());
		assertEquals("First We Serve", et02.getDescription());
		assertEquals(2, et02.getDefHours());
		assertEquals(true, et02.isPinHours());
		assertEquals(2, et02.getDefClient().getScid());
		assertEquals(2, et02.getDefOrg().getSgid());
		
		EventType et03 = dao.fetchEventTypeById(3);

		assertEquals(3, et03.getEtid());
		assertEquals("rbd", et03.getName());
		assertEquals("Roo Bound", et03.getDescription());
		assertEquals(3, et03.getDefHours());
		assertEquals(true, et03.isPinHours());
		assertEquals(1, et03.getDefClient().getScid());
		assertEquals(3, et03.getDefOrg().getSgid());

	}
	
	/*
	 * Testing listAll(), should return the current 3 Event Types that
	 * are in the data.sql database.
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {

		List<EventType> events = dao.listAll();

		assertEquals(1, events.get(0).getEtid());
		assertEquals("gds", events.get(0).getName());

		assertEquals(2, events.get(1).getEtid());
		assertEquals("fws", events.get(1).getName());

		assertEquals(3, events.get(2).getEtid());
		assertEquals("rbd", events.get(2).getName());

	}
	
	
	/* Testing the create(), should create a new Event Type query 
	 * in the data.sql database.
	 */
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception{
		
		List<EventType> preCreate = dao.listAll();
		int numBeforeInsert = preCreate.size();
		System.err.println("\n\nBefore Insert " + numBeforeInsert);
		for(EventType et : preCreate) {
			System.err.println(et.getName());
		}
		
		EventType newET = dao.create("et04", "Event Type 4 Description", 2, true, 2, 1);
		
		assertNotNull(newET);
		
		List<EventType> postCreate = dao.listAll();
		int numAfterInsert = postCreate.size();
		
		System.err.println("\n\nAfter Insert " + numAfterInsert);
		for(EventType et : postCreate) {
			System.err.println(et.getName());
		}
		
		// The next assigned id on successful insert should be numBeforeInsert + 1.
		assertEquals(numBeforeInsert+1, newET.getEtid());
		
		// Checking the newly inserted record.
		EventType et4 = newET;
		
		assertEquals(4, et4.getEtid());
		assertEquals("et04", et4.getName());
		assertEquals("Event Type 4 Description", et4.getDescription());
		assertEquals(2, et4.getDefHours());
		assertEquals(true, et4.isPinHours());
		assertEquals(2, et4.getDefClient().getScid());
		assertEquals(1, et4.getDefOrg().getSgid());
	}
	
	/*
	 *  Testing the delete(), should remove the query with the specified ID (first one in this case). 
	 *  Should still be one query left in the database.
	 */
	@Test
	void testDelete_whenUsingJdbcTemplate() throws Exception{
		
		dao.delete(1);
		
		List<EventType> ets = dao.listAll();
		
		// Original Size, 3, minus 1 since we deleted = 2
		assertEquals(2, ets.size());
		
		EventType et1 = ets.get(0);
		
		// SQL input for Event Type id 2
		assertEquals("fws", et1.getName());
		assertEquals("First We Serve", et1.getDescription());
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID.
	 */
	@Test
	void testUpdate_whenUsingJdbcTemplate() throws Exception{
		
		dao.update(1, "GreatDayOfService", "Austin College Hosted Event", 4, false, 2, 2);
		
		EventType et1 = dao.fetchEventTypeById(1);
		
		assertEquals("GreatDayOfService", et1.getName());
		assertEquals("Austin College Hosted Event", et1.getDescription());
		assertEquals(4, et1.getDefHours());
		assertEquals(false, et1.isPinHours());
		assertEquals(2, et1.getDefClient().getScid());
		assertEquals(2, et1.getDefOrg().getSgid());
	}
	
	
}
