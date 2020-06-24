package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import srv.controllers.HoursController;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.JdbcTemplateEventTypeDao;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(HoursController.class)
@Import(WebSecurityConfig.class)
public class HoursControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ServiceHoursService hrSvc;
	
	@MockBean
	ServiceHoursDao dao;
	
	@MockBean
	UserUtil mockUserUtil;

	@MockBean
	private EventService mockService;
	
	// handy objects for these tests
	private List<Event> testEvents = new ArrayList<Event>();		
	private List<EventType> testTypes = new ArrayList<EventType>();
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();
	private List<ServiceHours> testHours = new ArrayList<ServiceHours>();
	
	private ServiceHours sh1;
	private ServiceHours sh2;
	private ServiceHours sh3;

	
	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * events, a couple of typical event types and service clients.  
	 * @throws ParseException 
	 * 
	 */
	@Before
	public void setupTestFixture() throws ParseException {
		
		// Creating new Date object to avoid errors in the future
		String sDate = "2020-06-12 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
				
		EventType et1 = new EventType()
				.setEtid(1)
				.setName("gds")
				.setDescription("great day of service for test")
				.setDefHours(0.0)
				.setDefClient(null)
				.setPinHours(false);
				
		EventType et2 = new EventType()
				.setEtid(2)
				.setName("fws")
				.setDescription("first we serve for test")
				.setDefHours(0.0)
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
		
		Event e1 = new Event()
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
		
		 Event e2 = new Event()
				.setEid(2)
				.setTitle("fws 2020")
				.setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave")
				.setType(et1)
				.setServiceClient(sc2)
				.setContact(null);
			
		 sh1 = new ServiceHours()
				 .setShid(1)
				 .setStatus("Rejected")
				 .setEvent(e1)
				 .setServant(new User().setUid(1))
				 .setServedPet(sc1)
				 .setDate(date)
				 ;
		 
		 sh2 = new ServiceHours()
				 .setShid(2)
				 .setStatus("Approved")
				 .setEvent(e2)
				 .setServant(new User().setUid(2))
				 .setServedPet(sc2)
				 .setDate(date)
				 ;
		 
		 sh3 = new ServiceHours()
				 .setShid(3)
				 .setStatus("Pending")
				 .setEvent(e2)
				 .setServant(new User().setUid(2))
				 .setServedPet(sc2)
				 .setDate(date)
				 ;
		 
		testEvents.add(e1);
		testEvents.add(e2);
		testTypes.add(et1);
		testTypes.add(et2);
		testClients.add(sc1);
		testClients.add(sc2);
		testHours.add(sh1);
		testHours.add(sh2);
		testHours.add(sh3);
		
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

	@Test
	public void basicHtmlPageTest() throws Exception {
		
		// train the dao to ask for these when asked to listAll reasons.
		ServiceHours h1 = new ServiceHours().setShid(1).setEvent(new Event().setTitle("Spending Time with Toys for Tots")).setHours((double) 6).setStatus("Approved");
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Teaching Part Time")).setHours((double) 2).setStatus("Pending");

		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(h1);
		dummyList.add(h2);
		
		Mockito.when(hrSvc.listHours()).thenReturn(dummyList);

		// now perform the test
		mvc.perform(get("/test/hours").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("<li  id=\"row_1\"> Num: 1, 6 hours, Event Title: Spending Time with Toys for Tots</li>")))
				.andExpect(content().string(
						containsString("<li  id=\"row_2\"> Num: 2, 2 hours, Event Title: Teaching Part Time</li>")));

	}
	
	/**
	 * Testing hours list for hours manager, specifically the action buttons for different hours' status
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void hoursPageTest() throws Exception {
		

		Mockito.when(mockService.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(null), Mockito.refEq(null), Mockito.anyString(), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.filteredHours(1, null, null, null, null)).thenReturn(testHours);

		
		// now perform the test
		mvc.perform(get("/hours")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		// our page displays a table somewhere inside for showing hours
		.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())

		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'gds 2020' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='gds 2020']")).exists())
		
		// and that same row should not have a td with a button inside for editing (since hour is approved)
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]")).exists())

	
		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'fws 2020' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())

		// and that same row better have a td with a button inside for editing (since hour is pending)
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]")).doesNotExist())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]")).doesNotExist())
		
		
		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'fws 2020' 
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())

		// and that same row better have a td with a button inside for editing (since hour is rejected)
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]")).exists())

		;
	}
}
