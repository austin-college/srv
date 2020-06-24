package srv.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import srv.controllers.HoursController;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.User;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(HoursController.class)
@Import(WebSecurityConfig.class)
public class FilterHoursControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	ServiceHoursService hrSvc;
	
	@MockBean
	ServiceHoursDao dao;
		
	@MockBean
	UserUtil mockUserUtil;
	
	@MockBean
	private EventService eventSvc;
	
	// handy objects for these tests
	
	private List<Event> testEvents = new ArrayList<Event>();		
	private List<EventType> testTypes = new ArrayList<EventType>();
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();
	private List<ServiceHours> testHours = new ArrayList<ServiceHours>();

	private ServiceHours sh1;
	private ServiceHours sh2;
	
	
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
				.setDate(date)
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
				.setDate(date)
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
	 * Make sure the base page shows a table of 2 hours with
	 * user ids of 1
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void byUserId_whenIdValid() throws Exception {
		
		testHours.add(sh1);
		testHours.add(sh2);
		
		Mockito.when(eventSvc.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(false);
		Mockito.when(mockUserUtil.currentUser()).thenReturn(new User().setUid(1));
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.eq(1), Mockito.refEq(null), Mockito.refEq(null), Mockito.anyString(), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.averageHoursPerMonth(testHours)).thenReturn(0.0);
		
		mvc.perform(get("/hours")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing hours
			.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())
			
			// and there's a row in our table that has a event name td inside whose text better be 'gds 2020' 
			.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='gds 2020']")).exists())
			
			// and there's a row in our table that has a event name td inside whose text better be 'fws 2020' 
			.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 1 hour with
	 * service client/sponsors ids of 1
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void byServiceClientId_whenIdValid() throws Exception {
		
		testHours.add(sh1);

		Mockito.when(eventSvc.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(1), Mockito.refEq(null), Mockito.anyString(), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.averageHoursPerMonth(testHours)).thenReturn(0.0);
		
		mvc.perform(get("/hours?sc=1")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing hours
			.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())
			
			// and there's a row in our table that has a service client/sponsor name td inside whose text better be 'Habitat for Humanity' 
			.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_sponsor' and text()='Habitat for Humanity']")).exists())
			
			// and there's not a row in our table that has a service client/sponsor name td inside whose text better be 'Meals on Wheels' 
			.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_sponsor' and text()='Meals on Wheels']")).doesNotExist())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 2 hours with
	 * month 'June'
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void byMonthName_whenNameValid() throws Exception {
		
		testHours.add(sh1);
		testHours.add(sh2);

		Mockito.when(eventSvc.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(null), Mockito.eq("June"), Mockito.anyString(), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.averageHoursPerMonth(testHours)).thenReturn(0.0);
		
		mvc.perform(get("/hours?month=June")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing hours
			.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())
			
			// and there's a row in our table that has a date td inside whose text better be 'Jun 12, 2020 12:00 AM' aka have the month of 'June'
			.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_date' and text()='Jun 12, 2020 12:00 AM']")).exists())
			
			// and there's not a row in our table that has a date td inside whose text better be 'Jun 12, 2020 12:00 AM' aka have the month of 'June'
			.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_date' and text()='Jun 12, 2020 12:00 AM']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 2 hours with
	 * year 2020
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void byYear_whenYearValid() throws Exception {
		
		testHours.add(sh1);
		testHours.add(sh2);

		Mockito.when(eventSvc.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(null), Mockito.refEq(null), Mockito.anyString(), Mockito.eq("2020"))).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.averageHoursPerMonth(testHours)).thenReturn(0.0);
		
		mvc.perform(get("/hours?year=2020")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing hours
			.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())
			
			// and there's a row in our table that has a date td inside whose text better be 'Jun 12, 2020 12:00 AM' aka have the year '2020'
			.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_date' and text()='Jun 12, 2020 12:00 AM']")).exists())
			
			// and there's not a row in our table that has a date td inside whose text better be 'Jun 12, 2020 12:00 AM' aka have the year '2020'
			.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_date' and text()='Jun 12, 2020 12:00 AM']")).exists())
			;
	}
	
	/**
	 * Make sure the base page shows a table of 1 hour with
	 * status 'Approved'
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void byStatus_whenStatusValid() throws Exception {
		
		testHours.add(sh2);

		Mockito.when(eventSvc.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(null), Mockito.refEq(null), Mockito.eq("Approved"), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.averageHoursPerMonth(testHours)).thenReturn(0.0);
		
		mvc.perform(get("/hours?status=Approved")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing hours
			.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())
					
			// and there's not a row in our table that has an hours status td inside whose text better be 'Approved'
			.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_status' and text()='Approved']")).exists())
			;
	}
}
