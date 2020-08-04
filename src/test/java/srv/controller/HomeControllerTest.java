package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.config.WebSecurityConfig;
import srv.controllers.HomeController;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.hours.ServiceHours;
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;
import srv.domain.user.User;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;


/**
 * Tests the /home area of our site. 
 * 
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@Import(WebSecurityConfig.class)

public class HomeControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ServiceHoursService hrSvc;
	
	@MockBean
	private EventService evSvc;

	
	@MockBean
	private UserUtil userUtil;
	
	@MockBean
	private ServantUserDao mockSrvUserDao;
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
	 * This test ensures that when the user is just a servant user, the /home page
	 * redirects to the servant's home page.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void servantHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);

		mvc.perform(get("/home")
				
				.contentType(MediaType.TEXT_HTML))
				
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/servant?userid=user"))
				;

	}
	
	/**
	 * Test to verify the controller prepares and loads the servant user's home page.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void servantHome() throws Exception {
		
		// Create dependencies
		Event e1 = new Event()
				.setEid(1)
				.setTitle("gds 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(null)
				.setNeededVolunteerHours(25.0)
				.setRsvpVolunteerHours(2.5)
				.setServiceClient(null)
				.setContact(null);
		
		User user = new User()
				.setUid(1)
				.setContactInfo(new Contact()
						.setFirstName("Rusty")
						.setLastName("Buckle")
						.setContactId(1)
						.setEmail("rbuckle@helpful.org")
						.setPrimaryPhone("903-813-5555")
						.setCity("Sherman"));
		
		ServiceHours dummyHr = new ServiceHours()
				.setStatus("Pending");
		
		ServantUser srvUser = new ServantUser(1, "user", user.getContactInfo(), 2020, null, true, 2);
		
		List<Event> upcomingEventDummy = new ArrayList<Event>();
		List<ServiceHours> dummyHrList = new ArrayList<ServiceHours>();
		upcomingEventDummy.add(e1);
		dummyHrList.add(dummyHr);
		
		// Mock dependencies
		when(evSvc.filteredEvents(null, "now+1M", null, null, null)).thenReturn(upcomingEventDummy);
		when(userUtil.currentUser()).thenReturn(user);
		when(mockSrvUserDao.fetchServantUserById(1)).thenReturn(srvUser);
		when(hrSvc.userHours(1)).thenReturn(dummyHrList);
		when(hrSvc.totalSemesterHours(null)).thenReturn(0.0);

		mvc.perform(get("/home/servant/")
				.contentType(MediaType.TEXT_HTML))
		
				// there should be a div for announcements
        		.andExpect(xpath(dquote("//div[@id='announceList']")).exists())
        
        		// should contain the current user's information
        		.andExpect(content().string(containsString("user")))				
        		.andExpect(content().string(containsString("Rusty Buckle")))
        		.andExpect(content().string(containsString("rbuckle@helpful.org")))
        		.andExpect(content().string(containsString("903-813-5555")))	
        		.andExpect(content().string(containsString("2020")))
        		.andExpect(content().string(containsString("Yes")))
        		.andExpect(content().string(containsString("2")))
        		
                // and there's a button inside for editing profile
                .andExpect(xpath(dquote("//button[contains(@id, 'editProfileBtn')]")).exists())

                // and signing up for the event
                .andExpect(xpath(dquote("//button[contains(@id, 'rsvpBtn')]")).exists())
                ;
        		
		
	}
	
	@Test
	@WithMockUser(username = "boardmember", password = "boardmember")
	public void bmHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);
		when(userUtil.userIsBoardMember()).thenReturn(true);

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/boardMember?userid=boardmember"));

	}

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void adminHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);
		when(userUtil.userIsBoardMember()).thenReturn(true);
		when(userUtil.userIsAdmin()).thenReturn(true);

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/admin?userid=admin"));

	}

	@Test
	@WithMockUser(username = "boardmember", password = "boardmember")
	public void boardMemberHomeTest() throws Exception {

		mvc.perform(get("/home/boardMember").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome, Board Member")))
				// testing for the Approve Hours Button - Via the text it has
				.andExpect(content().string(containsString("<p>View current pets</p>")))
				// testing a broad statement to make sure we actually have buttons on the page
				.andExpect(content().string(containsString("type=\"button\" class=\"btn btn-White\"")));

	}

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void adminHomeTest() throws Exception {

		mvc.perform(get("/home/admin").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome, Admin")))
				// testing for the Manage Service Client Button - Via the text it has
			.andExpect(content().string(containsString("Manage Sponsors")))
				// testing a broad statement to make sure we actually have buttons on the page
			.andExpect(content().string(containsString("href=\"/srv/sc\" class=\"btn btn-light\"")));

	}

}
