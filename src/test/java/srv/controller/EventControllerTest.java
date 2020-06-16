package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.config.WebSecurityConfig;
import srv.controllers.EventController;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceclient.ServiceClient;
import srv.services.EventService;
import srv.utils.UserUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
@Import(WebSecurityConfig.class)

public class EventControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EventService mockService;

	@MockBean
	private UserUtil userUtil;

	
	// handy objects for these tests
	
	private List<Event> testEvents = new ArrayList<Event>();		
	private List<EventType> testTypes = new ArrayList<EventType>();
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();

	private Event e1;

	private Event e2;

	
	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * events, a couple of typical event types and service clients.  
	 * 
	 */
	@Before
	public void setupTestFixture() {
		
		EventType et1 = new EventType()
				.setEtid(1)
				.setName("gds")
				.setDescription("great day of service for test")
				.setDefHours(0)
				.setDefClient(null)
				.setPinHours(false);
				
		EventType et2 = new EventType()
				.setEtid(2)
				.setName("fws")
				.setDescription("first we serve for test")
				.setDefHours(0)
				.setDefClient(null)
				.setPinHours(false);
		
		ServiceClient sc1 = new ServiceClient()
				.setClientId(1)
				.setName("Habitat for Humanity")
				.setCategory("Community");
		
		ServiceClient sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Meals on Wheels")
				.setCategory("Seniors");		
		
		e1 = new Event()
				.setEid(1)
				.setTitle("gds 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setServiceClient(sc1)
				.setContact(new Contact()
						.setFirstName("Rusty")
						.setLastName("Buckle")
						.setContactId(1)
						.setEmail("rbuckle@helpful.org")
						.setPhoneNumMobile("903-813-5555")
						.setCity("Sherman")
						);
		
		e2 = new Event()
				.setEid(2)
				.setTitle("fws 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setServiceClient(sc2)
				.setContact(null);
		
		
		

		testEvents.add(e1);
		testEvents.add(e2);
		testTypes.add(et1);
		testTypes.add(et2);
		testClients.add(sc1);
		testClients.add(sc2);
		
	}

	private String dquote(String anyStr) {
		if (anyStr == null) return null;
		return anyStr.replaceAll("[']", "\"");
	}
	
	@Test
	public void test_replace_single_quotes_function() {
		String str = "tr/td[@id='xyz']";
		System.err.println(dquote(str));
		assertEquals("tr/td[@id=\"xyz\"]",dquote(str));
	}
	
	
	/**
	 * Make sure the base page shows a table of 2 events with buttons
	 * to allow the user to create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_queriesAllNull() throws Exception {
		
		Mockito.when(mockService.filteredEvents(null, null, null, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())
			
			
			
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
		
		

	}
	
	
	
	
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxDeleteEventTest_whenEventExists() throws Exception {
        
		 // for this test, our service will pretend to delete

		Mockito.doNothing().when(mockService).deleteEvent(1);
		
		 
         mvc.perform(post("/events/ajax/del/1")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should have the client's name
                  .andExpect(content().string(containsString("1")))
                 
                  // other expectations here...
                  ;
        
         Mockito.verify(mockService).deleteEvent(1);
       
    }
	
	/**
	 * Test how our controller responds when an exception is thrown.
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxDeleteEventTest_whenEventMissing() throws Exception {
        
		// for this test, our service will throw an exception like we might
		// see if the database could not delete
		Mockito.doThrow(Exception.class) .when(mockService).deleteEvent(1);
				
		 
		// let's test....
         mvc.perform(post("/events/ajax/del/1")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().is4xxClientError())
                  ;

                  
         // did the mock object get tickled appropriately
         Mockito.verify(mockService).deleteEvent(1);
       
    }
	
	
	
	/**
	 * Test that the controller returns HTML to present the events contact
	 * info.
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void testAjaxEventContact_whenContactExists() throws Exception {
        

		Mockito.when(mockService.eventById(Mockito.anyInt())).thenReturn(e1);
		
		 // ready to test....
         mvc.perform(get("/events/ajax/event/1/contact")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should have the client's name
                  .andExpect(
                 
                  xpath(dquote("//div[@id='evContactDiv']")).exists()
                  
                  )
                  
                  .andExpect(content().string(containsString("Rusty Buckle")))
                  .andExpect(content().string(containsString("work: unknown")))
                  .andExpect(content().string(containsString("mobile: 903-813-5555")))
         		  .andExpect(content().string(containsString("rbuckle@helpful.org")));
         
                  // other expectations here...
                  
        
         Mockito.verify(mockService).eventById(1);
       
    }
	
	
	/**
	 * Also test that the controller handles the case when the event's contact
	 * information is missing.
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void testAjaxEventContact_whenContactMissing() throws Exception {
        
		 
		e1.setContact(null);
		
		Mockito.when(mockService.eventById(Mockito.anyInt())).thenReturn(e1);
		
		 // ready to test....
         mvc.perform(get("/events/ajax/event/1/contact")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should have the client's name
                  .andExpect(
                 
                  xpath(dquote("//div[@id='evContactDiv']")).exists()
                  
                  )
                  .andExpect(content().string(containsString("Missing Contact")));
         
                  // other expectations here...
                  
        
         Mockito.verify(mockService).eventById(1);
       
    }
	
	
	/**
	 * Make sure the controller is creating a new event of a specified type when
	 * asked.
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void testAjaxNewEvent() throws Exception {
        

		 Mockito.when(mockService.createEventOfType(Mockito.anyInt())).thenReturn(e1);
		
		 // ready to test....
         mvc.perform(post("/events/ajax/new/1")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should have the client's name
                  .andExpect(content().string(containsString("1")));

         
                  // other expectations here...
                  
        
         Mockito.verify(mockService).createEventOfType(1);
       
    }
	
	
	/**
	 * Make sure the controller is handling the case where the event type
	 * is invalid (the service throws an exception). 
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void testAjaxNewEvent_whenExceptionThrown() throws Exception {
        

		// for this test, our service will throw an exception like we might
		// see if the database could not delete
		Mockito.doThrow(Exception.class) .when(mockService).createEventOfType(Mockito.anyInt());
		
		 // ready to test....
         mvc.perform(post("/events/ajax/new/1")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
         		  .andExpect(status().is4xxClientError());
        
         Mockito.verify(mockService).createEventOfType(1);
       
    }
	
	/**
	 * Make sure the base page shows a table of 2 events that are before
	 * the date 2020-09-09 00:00:00 with buttons to allow the user to
	 *  create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_pastEvents() throws Exception {
		
		Mockito.when(mockService.filteredEvents("now", null, null, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(mockService.currentDate()).thenReturn(Timestamp.valueOf("2020-09-09 00:00:00"));
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?before=now")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())
			
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 2 events that are after
	 * the date 2020-04-09 00:00:00 with buttons to allow the user to
	 *  create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_futureEvents() throws Exception {
		
		Mockito.when(mockService.filteredEvents(null, "now", null, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(mockService.currentDate()).thenReturn(Timestamp.valueOf("2020-04-09 00:00:00"));
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?after=now")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())
			
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 2 events that were in the last month
	 * from the date 2020-07-09 00:00:00 with buttons to allow the user to
	 * create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_lastMonthEvents() throws Exception {
		
		Mockito.when(mockService.filteredEvents("now-1M", null, null, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(mockService.currentDate()).thenReturn(Timestamp.valueOf("2020-07-09 00:00:00"));
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?before=now-1M")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())
			
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	/**
	 * Make sure the base page shows a table of 2 events that were in the next month
	 * from the date 2020-05-09 00:00:00 with buttons to allow the user to
	 * create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_nextMonthEvents() throws Exception {
		
		Mockito.when(mockService.filteredEvents(null, "now+1M", null, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(mockService.currentDate()).thenReturn(Timestamp.valueOf("2020-05-19 00:00:00"));
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?after=now+1M")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())
			
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 1 events with event type ids of 1
	 * with buttons to allow the user to create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_byEventType_whenIdValid() throws Exception {
		
		Mockito.when(mockService.filteredEvents(null, null, 1, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?eType=1")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 1 events with service client ids of 2
	 * with buttons to allow the user to create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_byServiceClient_whenIdValid() throws Exception {
		
		Mockito.when(mockService.filteredEvents(null, null, null, 2, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?sc=2")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
						
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title evView' and text()='fws 2020']")).exists())

			// and that second event better handle null contact just fine... ignoring extra white space around.
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_contact' and normalize-space(text())='None']")).exists())

			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())
			
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 1 event that is before
	 * the date 2020-09-09 00:00:00 with event type id of 1 and with
	 * buttons to allow the user to create a new event.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest_query_pastEventsWithEventType() throws Exception {
		
		Mockito.when(mockService.filteredEvents("now", null, 1, null, null)).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);
		Mockito.when(mockService.allServiceClients()).thenReturn(testClients);
		Mockito.when(mockService.currentDate()).thenReturn(Timestamp.valueOf("2020-09-09 00:00:00"));
		Mockito.when(userUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(userUtil.userIsBoardMember()).thenReturn(true);
		
		mvc.perform(get("/events?before=now&eType=1")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title evView' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

	
						
			// and our page better have a delete dialog defined/hidden
			.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED EVENT']")).exists())
			
			// and a view dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgView' and @title='EVENT DETAILS']")).exists())
			
			// and a dialog for showing contact details
			.andExpect(xpath(dquote("//div[@id='dlgViewContact' and @title='CONTACT DETAILS']")).exists())
			
			// and a dialog for for create a new event
			.andExpect(xpath(dquote("//div[@id='dlgNewEvent' and @title='CREATE NEW EVENT']")).exists())
			;
	}
}
