package srv.controller;

import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import okhttp3.Response;
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
	
	private Event e1;
	private Event e2;
	
	private ServiceClient sc1;
	private ServiceClient sc2;

	/**
	 * Called before each an every test in order to have sufficient data for this
	 * series of tests. We make a couple of typical events, a couple of typical
	 * event types and service clients.
	 * 
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

		EventType et1 = new EventType().setEtid(1).setName("gds").setDescription("great day of service for test")
				.setDefHours(0.0).setDefClient(null).setPinHours(false);

		EventType et2 = new EventType().setEtid(2).setName("fws").setDescription("first we serve for test")
				.setDefHours(0.0).setDefClient(null).setPinHours(false);

		sc1 = new ServiceClient().setClientId(1).setName("Habitat for Humanity").setCategory("Community");

		sc2 = new ServiceClient().setClientId(2).setName("Meals on Wheels").setCategory("Seniors");

		e1 = new Event().setEid(1).setTitle("gds 2020").setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave").setType(et1).setServiceClient(sc1)
				.setContact(new Contact().setFirstName("Rusty").setLastName("Buckle").setContactId(1)
						.setEmail("rbuckle@helpful.org").setPhoneNumMobile("903-813-5555").setCity("Sherman"));

		e2 = new Event().setEid(2).setTitle("fws 2020").setDate(new java.util.Date())
				.setAddress("900 N. Grand Ave").setType(et1).setServiceClient(sc2).setContact(null);

		sh1 = new ServiceHours().setShid(1).setStatus("Rejected").setEvent(e1).setServant(new User().setUid(1))
				.setServedPet(sc1).setDate(date);

		sh2 = new ServiceHours().setShid(2).setStatus("Approved").setEvent(e2).setServant(new User().setUid(2))
				.setServedPet(sc2).setDate(date).setFeedback("Good job");

		sh3 = new ServiceHours().setShid(3).setStatus("Pending").setEvent(e2).setServant(new User().setUid(2))
				.setServedPet(sc2).setDate(date);

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
		if (anyStr == null)
			return null;
		return anyStr.replaceAll("[']", "\"");
	}

	@Test
	public void test_replace_single_quotes_function() {
		String str = "tr/td[@id='xyz']";
		System.err.println(dquote(str));
		assertEquals("tr/td[@id=\"xyz\"]", dquote(str));
	}

	@Test
	public void basicHtmlPageTest() throws Exception {

		// train the dao to ask for these when asked to listAll reasons.
		ServiceHours h1 = new ServiceHours().setShid(1)
				.setEvent(new Event().setTitle("Spending Time with Toys for Tots")).setHours((double) 6)
				.setStatus("Approved");
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Teaching Part Time"))
				.setHours((double) 2).setStatus("Pending");

		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(h1);
		dummyList.add(h2);

		Mockito.when(hrSvc.listHours()).thenReturn(dummyList);

		// now perform the test
		mvc.perform(get("/test/hours").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString(
						"<li  id=\"row_1\"> Num: 1, 6 hours, Event Title: Spending Time with Toys for Tots</li>")))
				.andExpect(content().string(
						containsString("<li  id=\"row_2\"> Num: 2, 2 hours, Event Title: Teaching Part Time</li>")));

	}

	/**
	 * Testing hours list for hours manager, specifically the action buttons for
	 * different hours' status
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void hoursPageTest() throws Exception {

		Mockito.when(mockService.allEvents()).thenReturn(testEvents);
		Mockito.when(hrSvc.listCurrentSponsors()).thenReturn(testClients);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);
		Mockito.when(hrSvc.listHours()).thenReturn(testHours);
		Mockito.when(hrSvc.filteredHours(Mockito.refEq(null), Mockito.refEq(null), Mockito.refEq(null),
				Mockito.anyString(), Mockito.anyString())).thenReturn(testHours);
		Mockito.when(hrSvc.totalSemesterHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalAcademicYearHours(testHours)).thenReturn(0.0);
		Mockito.when(hrSvc.totalSponsorsCount(testHours)).thenReturn(0);
		Mockito.when(hrSvc.filteredHours(1, null, null, null, null)).thenReturn(testHours);

		// now perform the test
		mvc.perform(get("/hours").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())

				// our page displays a table somewhere inside for showing hours
				.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())

				// and there's a row in our table that has a hrs_eventName td inside whose text
				// better be 'fws 2020'
				.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())

				// and there's a row in our table that has a hrs_eventName td inside whose text
				// better be 'gds 2020'
				.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='gds 2020']")).exists())

				// and that same row should not have a td with a button inside for editing
				// (since hour is approved)
				.andExpect(xpath(dquote(
						"//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]"))
								.exists())

				// and deleting
				.andExpect(xpath(dquote(
						"//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]"))
								.exists())

				// and that same row better have a td with a button inside for editing (since
				// hour is pending)
				.andExpect(xpath(dquote(
						"//tr[@id='row3']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]"))
								.exists())

				// and deleting
				.andExpect(xpath(dquote(
						"//tr[@id='row2']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]"))
								.doesNotExist())

				// and there's a row in our table that has a hrs_eventName td inside whose text
				// better be 'fws 2020'
				.andExpect(xpath(dquote("//tr[@id='row3']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())

				// and that same row better have a td with a button inside for editing (since
				// hour is rejected)
				.andExpect(xpath(dquote(
						"//tr[@id='row3']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]"))
								.exists());

	}
	
/**
 * Make sure the controller is updating an existing hour when asked. 
 * Note all the parameters are valid
 * 
 * @throws Exception
 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void ajaxServiceHourUpdate() throws Exception {
	
		int shid = 1;
		int scid = 2;
		int eid = 2;
		double hrs = 6.5;
		String reflection = "Had a fun time";
		String descr = "Worked with seniors";
	
		// change the values of our current service hour
		sh1.setEvent(e2)
			.setDescription(descr)
			.setHours(hrs)
			.setReflection(reflection)
			.setServedPet(sc2)
			;
		
		// mock dependencies
		Mockito.doNothing().when(hrSvc).updateHour(shid, scid, eid, hrs, reflection, descr);
		Mockito.when(hrSvc.serviceHourById(shid)).thenReturn(sh1);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(false);
		Mockito.when(mockUserUtil.userIsBoardMember()).thenReturn(false);
		
		// now perform the test and pretend that jquery sends in the parameters
		// to update the service hour
		mvc.perform(post("/hours/ajax/editHr")	
				.param("shid", String.valueOf(shid))
				.param("scid", String.valueOf(scid))
				.param("eid", String.valueOf(eid))
				.param("hrSrved", String.valueOf(hrs))
				.param("reflect", reflection)
				.param("descr", descr)
				
				.contentType(MediaType.TEXT_HTML))
		
		.andExpect(status().isOk())
		
		// there's a row in our table that has an event name/title td inside whose text better be 'fws 2020' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='fws 2020']")).exists())

		// and there's a row in our table that has a service client name td inside whose text better be 'Meals on Wheels' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_sponsor' and text()='Meals on Wheels']")).exists())

		// and there's a row in our table that has a hours served td inside whose text better be '6.5' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_hrsServed' and text()='6.5']")).exists())

		;
	
		Mockito.verify(hrSvc).updateHour(shid, scid, eid, hrs, reflection, descr);
		Mockito.verify(hrSvc).serviceHourById(shid);
		
	}

	@Test
	@WithMockUser(username = "user", password = "user")
	public void handleReasonRequest() throws Exception {
		ServiceHours h1 = new ServiceHours().setShid(1)
				.setEvent(new Event().setTitle("GDS 2020")).setHours((double) 3.5 )
				.setStatus("Approved");
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Greenserve 2020"))
				.setHours((double) 8).setStatus("Pending");

		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(h1);
		dummyList.add(h2);

		Mockito.when(hrSvc.listHours()).thenReturn(dummyList);

		// now perform the test
		mvc.perform(get("/test/hours").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString(
						"<li  id=\"row_1\"> Num: 1, 3.5 hours, Event Title: GDS 2020</li>")))
				.andExpect(content().string(
						containsString("<li  id=\"row_2\"> Num: 2, 8 hours, Event Title: Greenserve 2020</li>")));
	}

	/**
	 * tests the fetch by service hour ID function
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void ajaxFetchServiceHour() throws Exception {
		Mockito.when(hrSvc.serviceHourById(Mockito.anyInt())).thenReturn(sh1);

		// ready to test....
		mvc.perform(get("/hours/ajax/hour/1")

				.contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				// checks if id matches
				.andExpect(jsonPath("$.shid", is(1)));

		Mockito.verify(hrSvc).serviceHourById(1);
	}

	/**
	 * Tests the create function - come back and fix - not complete 
	 *
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void ajaxAddServiceHour() throws Exception {

		int scid = 1;
		int eid = 1;
		double hrs = 3.5;
		String reflect = "great day for service";
		String descrip = "love the community";
		
		// create dummy new service hour
		ServiceHours shr = new ServiceHours();
		
		shr.setReflection(reflect)
			.setDescription(descrip)
			.setHours(hrs)
			.setShid(1)
			.setEvent(e1)
			.setServedPet(sc1)
			.setStatus("Pending");
		
		// mock dependencies
		Mockito.when(hrSvc.createServiceHour(scid, eid, hrs, reflect, descrip)).thenReturn(shr);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(false);
		Mockito.when(mockUserUtil.userIsBoardMember()).thenReturn(false);

		// ready to test....
		mvc.perform(post("/hours/ajax/addHr")
				.param("hrServed", String.valueOf(hrs))
				.param("reflect", reflect)
				.param("descr", descrip)
				.param("eid", String.valueOf(eid))
				.param("scid", String.valueOf(scid))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// there's a row in our table that has an event name/title td inside whose text better be 'gds 2020' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='gds 2020']")).exists())

		// and there's a row in our table that has a service client name td inside whose text better be 'Habitat for Humanity' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_sponsor' and text()='Habitat for Humanity']")).exists())

		// and there's a row in our table that has a hours served td inside whose text better be '3.5' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_hrsServed' and text()='3.5']")).exists())

		// and there's a row in our table that has a hours status td inside whose text better be 'Pending' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_status' and text()='Pending']")).exists())
	
		// and that same row should not have a td with a button inside for editing
		// (since hour is pending)
		.andExpect(xpath(dquote(
				"//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrEdit')]"))
				.exists())

		// and deleting
		.andExpect(xpath(dquote(
				"//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrDel')]"))
				.exists())

		// and viewing
		.andExpect(xpath(dquote(
				"//tr[@id='row1']/td[@class='hrActions']/div[@class='dropdown']//a[contains(@class, 'btnHrView')]"))
				.exists());

		Mockito.verify(hrSvc).createServiceHour(scid, eid, hrs, reflect, descrip);

	}

	/**
	 * Tests the delete function while the hour exists
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void ajaxServiceHourDelete_whenHourExists() throws Exception {

		Mockito.doNothing().when(hrSvc).removeServiceHour(1);

		mvc.perform(post("/hours/ajax/del/1")

				.contentType(MediaType.TEXT_HTML))

				.andExpect(status().isOk())

				// it should have the client's name
				.andExpect(content().string(containsString("1")));

		// other expectations here...

		Mockito.verify(hrSvc).removeServiceHour(1);

	}

	/**
	 * Tests the delete function when hour doesn't exist
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void ajaxServiceHourDelete_whenHourMissing() throws Exception {
		// Throws an exception similar to when an event doesn't exist
		Mockito.doThrow(Exception.class).when(hrSvc).removeServiceHour(1);

		// actually test
		mvc.perform(post("/hours/ajax/del/1")

				.contentType(MediaType.TEXT_HTML))

				.andExpect(status().is4xxClientError());

		Mockito.verify(hrSvc).removeServiceHour(1);

	}

	/**
	 * Note: must use jsonpath as opposed to xpath since returning an json and not
	 * html
	 * 
	 * Testing the ajax method for changing an hour's status.
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void test_ajaxChangeHourStatus() throws Exception {

		// mocking dependencies
		Mockito.when(hrSvc.changeStatus(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(sh2);

		// ready to test
		mvc.perform(get("/hours/ajax/updateStatus/hour/2").param("status", "Approved").param("feedback", "Good job")

				.contentType(MediaType.APPLICATION_JSON))

				.andExpect(status().isOk())

				// expecting a json object whose event id better be 1
				.andExpect(jsonPath("$.shid", is(2)))

				// and whose status now better be "Approved"
				.andExpect(jsonPath("$.status", is("Approved")))

				// and with the feedback message of 'Good job'
				.andExpect(jsonPath("$.feedback", is("Good job")))

		;

		// verify that the service got involved
		Mockito.verify(hrSvc).changeStatus(2, "Approved", "Good job");
	}
}
