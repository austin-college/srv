package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import srv.config.WebSecurityConfig;
import srv.controllers.EventController;
import srv.controllers.ServiceClientController;
import srv.domain.contact.Contact;
import srv.domain.contact.ContactDao;
import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
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
	

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest() throws Exception {

		

		
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
				.setDate("today")
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setContact(new Contact()
						.setContactId(1)
						.setEmail("rbuckle@helpful.org")
						.setCity("Sherman")
						);
		
		Event e2 = new Event()
				.setEid(1)
				.setTitle("fws 2020")
				.setDate("today")
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setContact(new Contact()
						.setContactId(1)
						.setEmail("jsmith@austincollege.edu")
						.setCity("Sherman")
						.setState("TX")
						.setFirstName("Jane")
						.setLastName("Smith")
						);
		
		when(userUtil.userIsAdmin()).thenReturn(true);
		
		List<Event> myEvents = new ArrayList<Event>();
		myEvents.add(e1);
		
		List<EventType> types = new ArrayList<EventType>();
		types.add(et1);
		

		Mockito.when(mockService.allEvents()).thenReturn(myEvents);
		Mockito.when(mockService.allEventTypes()).thenReturn(types);
		

		// now perform the test ... should contain rows <tr id="eid-${ev.eid?c}">

		// http://hamcrest.org/JavaHamcrest/javadoc/2.1/org/hamcrest/Matchers.html
		
		mvc.perform(get("/events")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// there's a row in our table for the ifrst event
			.andExpect(content()
					.string(containsString("<tr id=\"eid-1\">")))
			.andExpect(content()
					.string(containsString("<td class='ev_id'>1</td>")))
			.andExpect(content()
					.string(containsString("<td class='ev_title'>gds 2020</td>")))
			;

	}
	
	
	
	
//	//credit to Professor Higgs here for this test
//	@Test
//    @WithMockUser(username = "admin", password = "admin")
//    public void ajaxAddSerciceClientTest() throws Exception {
//
//        
//         when(userUtil.userIsAdmin()).thenReturn(true);
//
//        
//         /*
//         * prepare dummy client obj
//         */
//         String clientName = "Habitat for Humanity";
//         int cid1 = 1; 
//         String bmName = "Billy Bob";
//         String category = "Community";
//        
//         ServiceClient sc1 = new ServiceClient()
//                  .setClientId(cid1)
//                  .setName(clientName)
//                  .setBoardMember(bmName)
//                  .setCategory(category);
//
//        
//         // when the controller asks the dao to create a client in the database, we 
//         // fake it and use our dummy client above (sc1)
//         Mockito.when(dao.create(clientName, cid1, -1, bmName, category) ).thenReturn(sc1);
//
//         // now perform the test...pretend that jquery sends in the parameters for a new
//         // client...  Our mock dao is trained to return a dummy service client (above)
//         // we should see an HTML table row return.
//        
//         mvc.perform(post("/ajax/addSc")
//                  .param("clientName", clientName)
//                  .param("mcID", String.valueOf(cid1))
//                  .param("ocID", String.valueOf("-1"))
//                  .param("bmName", bmName)
//                  .param("cat",category)
//                 
//                  .contentType(MediaType.TEXT_HTML))
//        
//                  .andExpect(status().isOk())
//                 
//                  // it should be a table row tagged with right id.
//                  .andExpect(content().string(containsString("<tr id=\"scid-1\">")))
//                 
//                  // it should have the client's name
//                  .andExpect(content().string(containsString(clientName)))
//                 
//                  // other expectations here...
//                  ;
//        
//
//    }
	

	
	
	

}
