package srv.services;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;


@RunWith(MockitoJUnitRunner.class)
public class EventServiceTests {

	@Mock
	private EventDao eventDao;
	
	
	@Mock
	private EventTypeDao eventTypeDao;


	@InjectMocks
	private EventService srv;
	
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	private EventType et1;
	private EventType et2;
	private Event e1;
	private Event e2;
	
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		et1 = new EventType()
				.setEtid(1)
				.setName("gds")
				.setDescription("great day of service for test")
				.setDefHours(0)
				.setDefClient(null)
				.setPinHours(false);
				
		et2 = new EventType()
				.setEtid(2)
				.setName("fws")
				.setDescription("first we serve for test")
				.setDefHours(0)
				.setDefClient(null)
				.setPinHours(false);
		
		e1 = new Event()
				.setEid(1)
				.setTitle("gds 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setContact(new Contact()
						.setContactId(1)
						.setEmail("rbuckle@helpful.org")
						.setCity("Sherman")
						);
		
		e2 = new Event()
				.setEid(2)
				.setTitle("fws 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setContact(null);
		
	}

	
	/**
	 * Make sure the service asks the dao for the right event 
	 * when the event id is a valid one.  
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_eventById_whenIdIsValid() throws Exception {
		
		
		Mockito.when(eventDao.fetchEventById(1)).thenReturn(e1);
		
		Event e = srv.eventById(1);
		assertEquals(e,e1);
		
		Mockito.verify(eventDao).fetchEventById(1);
		
		
	}

	/**
	 * Test to make sure service checks for valid id and throws exception
	 * when not valid.
	 * 
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void test_eventById_whenIdIsNotValid() throws Exception {

		Event e = srv.eventById(-1);
		
		
	}
	
	
	/**
	 * Test the a default dummy event is created given its type.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_createEventOfType_whenTypeIdIsValid() throws Exception {
		
		// train the mock doa just return e1 for this test  regardless of params
		Mockito.when(eventDao.create(
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.refEq(null),  // no contact id yet
				Mockito.any(java.util.Date.class),
				Mockito.anyInt(), 
				Mockito.anyBoolean(), 
				Mockito.refEq(null), 
				Mockito.refEq(null),
				Mockito.refEq(null), 
				Mockito.refEq(null), 
				Mockito.any(String.class)
				)).thenReturn(e1);
	
		
		// ready to test. here we go...
		Event e = srv.createEventOfType(1);
		
		assertEquals(e,e1);
		
		// make sure the dao was told to create with expected parameters
		Mockito.verify(eventDao).create(
				Mockito.eq("new event"), 
				Mockito.eq("location"), 
				Mockito.refEq(null),  // no contact id yet
				Mockito.any(java.util.Date.class),
				Mockito.eq(1), 
				Mockito.eq(false), 
				Mockito.refEq(null), 
				Mockito.refEq(null),
				Mockito.refEq(null), 
				Mockito.refEq(null), 
				Mockito.eq(""));
		
	}
	
	/**
	 * Test to make sure service checks for valid eventy type id and throws exception
	 * when not valid.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_createEventOfType_whenIdIsNotValid() throws Exception {

		// we expect an exception that complains about the event type id
	    exceptionRule.expect(Exception.class);
	    exceptionRule.expectMessage("Invalid event type");
	    
	    // ok...let's test
	    Event e = srv.createEventOfType(-1);
	}
	
	
	/**
	 * Test to make sure service tells dao to delete same 
	 * event id as pass  
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_delete_whenIdIsValid() throws Exception {
		
		
		Mockito.doNothing().when(eventDao).delete(Mockito.anyInt());
		
		srv.deleteEvent(1);
		
		Mockito.verify(eventDao).delete(1);
		
		
	}
	
	
	/**
	 * Test to make sure service checks for valid id and throws exception
	 * when not valid.
	 * 
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void test_delete_whenIdIsNotValid() throws Exception {

		srv.deleteEvent(-1);
		
		
	}
	
	
	/**
	 * Initial test for allEvents.  Should delegate to the dao and 
	 * return whatever the dao provided.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_allEvents() throws Exception {
		
		List<Event> list = new ArrayList<Event>();
		list.add(e1); list.add(e2);
		
		Mockito.when(eventDao.listAll()).thenReturn(list);
		
		List<Event> newList = srv.allEvents();
		
		Mockito.verify(eventDao).listAll();		
		
	}
	
	
	@Test
	public void test_allEventsTypes() throws Exception {
		
		List<EventType> list = new ArrayList<EventType>();
		list.add(et1); list.add(et2);
		
		Mockito.when(eventTypeDao.listAll()).thenReturn(list);
		
		List<EventType> newList = srv.allEventTypes();   
		
		Mockito.verify(eventTypeDao).listAll();		
		
	}
	
	@Test
	public void test_updateEvent() throws Exception {
		

		// finish configuring e1 for this test
		
		e1.setContinous(Boolean.valueOf(true));
		e1.setNeededVolunteerHours(2.5);
		e1.setRsvpVolunteerHours(1.5);
		e1.setVolunteersNeeded(3);
		e1.setNote("foo");
		
		
		// tell service to update
		srv.updateEvent(e1);   
		
		// verify that the dao got involved.
		
		Mockito.verify(eventDao).update(
				Mockito.eq(1),
				Mockito.eq(e1.getTitle()),
				Mockito.eq(e1.getAddress()),
				Mockito.eq(e1.getContact().getContactId()),
				Mockito.eq(e1.getDate()),
				Mockito.eq(e1.getType().getEtid()), 
				Mockito.anyBoolean(), 
				Mockito.eq(e1.getVolunteersNeeded()),
				Mockito.refEq(null),
				Mockito.anyDouble(),
				Mockito.anyDouble(),
				Mockito.any(String.class)
				);
		
	}

	
}
