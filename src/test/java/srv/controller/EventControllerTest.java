package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

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

	
	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * events and a couple of typical event types.  
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
		
		Event e1 = new Event()
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
		
		Event e2 = new Event()
				.setEid(2)
				.setTitle("fws 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setContact(null);
		
		
		

		testEvents.add(e1);
		testEvents.add(e2);
		testTypes.add(et1);
		testTypes.add(et2);
		
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
	public void basicBasePageTest() throws Exception {
		
		Mockito.when(mockService.allEvents()).thenReturn(testEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(testTypes);

		mvc.perform(get("/events")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing events
			.andExpect(xpath(dquote("//table[@id='tblEvents']")).exists())
			
			// and there's a row in our table that has a ev_title td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='ev_title' and text()='gds 2020']")).exists())
			
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='eid-1']/td[@class='evActions']/button[contains(@class, 'btnEvDel')]")).exists())

			
			// and there's a row in our table that has a ev_title td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='eid-2']/td[@class='ev_title' and text()='fws 2020']")).exists())

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
	

	
	
	

}
