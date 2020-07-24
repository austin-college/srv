package srv.controller;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

import srv.controllers.EventTypeController;

import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;


@RunWith(SpringRunner.class)
@WebMvcTest(EventTypeController.class)
@Import(WebSecurityConfig.class)

public class EventTypeControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private EventTypeDao mockEtDao;

	@MockBean
	private ServiceClientDao mockScDao;


	// handy objects for these tests
	private List<EventType> testTypes = new ArrayList<EventType>();
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();

	private EventType et1;
	private EventType et2;	
	private ServiceClient sc1;
	private ServiceClient sc2;

	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * event types and service clients.  
	 * 
	 */
	@Before
	public void setupTestFixture() {

		sc1 = new ServiceClient()
				.setClientId(1)
				.setName("Habitat for Humanity")
				.setCategory("Community");

		sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Meals on Wheels")
				.setCategory("Seniors");		

		et1 = new EventType()
				.setEtid(1)
				.setName("gds")
				.setDescription("great day of service for test")
				.setDefHours(5.0)
				.setDefClient(sc1)
				.setPinHours(false);

		et2 = new EventType()
				.setEtid(2)
				.setName("fws")
				.setDescription("first we serve for test")
				.setDefHours(3.5)
				.setDefClient(sc2)
				.setPinHours(false);

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
	 * Make sure the base page shows a table of 2 event types with buttons to
	 * allow the user to create a new event type while also editing, viewing, and deleting
	 * an event type.
	 * 
	 * @throws Excpetion
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest() throws Exception {

		// Mock dependencies
		Mockito.when(mockEtDao.listAll()).thenReturn(testTypes);
		Mockito.when(mockScDao.listAll()).thenReturn(testClients);

		mvc.perform(get("/eventTypes")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())

		// our page displays a table somewhere inside for showing event types
		.andExpect(xpath(dquote("//table[@id='tblEvType']")).exists())

		// and there's a row in our table that has a et name/description td inside whose text better be '(gds) great day of service for test' 
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_name_descr' and text()='(gds) great day of service for test']")).exists())

		// and there's a row in our table that has a et default hour td inside whose text better be '5' 
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_hr' and text()='5']")).exists())

		// and there's a row in our table that has a et default service client name td inside whose text better be 'Habitat for Humanity' 
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_sc' and text()='Habitat for Humanity']")).exists())


		// and that same row as a td with a button inside for editing
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtEdit')]")).exists())

		// and viewing
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtView')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtDel')]")).exists())


		// and there's a row in our table that has a et name/description td inside whose text better be '(fws) first we serve for test' 
		.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@name='et_name_descr' and text()='(fws) first we serve for test']")).exists())

		// and there's a row in our table that has a et default hour td inside whose text better be '3.5' 
		.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@name='et_hr' and text()='3.5']")).exists())

		// and there's a row in our table that has a et default service client name td inside whose text better be 'Meals on Wheels' 
		.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@name='et_sc' and text()='Meals on Wheels']")).exists())

		// and our page better have a create a new event type dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='addDlg' and @title='Add Event Type']")).exists())

		// and a dialog for selecting a default service client
		.andExpect(xpath(dquote("//div[@id='dlgScSel' and @title='Select Default Service Client...']")).exists())
		;
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

		/*
		 * prepare dummy event type obj
		 */
		String etName = "abc";
		int etid3 = 3;
		String descr = "longer name";
		double defHrs = 5.0;
		boolean pinHrs = true;
		int scid = 3;

		EventType et3 = new EventType()
				.setEtid(etid3)
				.setName(etName)
				.setDescription(descr)
				.setDefHours(defHrs)
				.setPinHours(pinHrs)
				.setDefClient(new ServiceClient()
						.setClientId(3)
						.setName("A Sponsor Name"));

		// when the controller asks the dao to create an event type in the database, we fake it and use our
		// dummy event type above (et3)
		Mockito.when(mockEtDao.create(etName, descr, defHrs, pinHrs, scid)).thenReturn(et3);

		// ready to test....
		mvc.perform(post("/eventTypes/ajax/addEt")
				.param("name", etName)
				.param("descr", descr)
				.param("defHrs", String.valueOf(defHrs))
				.param("pinHrs", String.valueOf(pinHrs))
				.param("scid", String.valueOf(scid))


				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// there should be a table row tagged with right id.

		// whose text better be '(gds) great day of service for test' 
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@name='et_name_descr' and text()='(abc) longer name']")).exists())

		// and there's a row in our table that has a et default hour td inside whose text better be '5' 
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@name='et_hr' and text()='5']")).exists())

		// and there's a row in our table that has a et default service client name td inside whose text better be 'A Sponsor Name' 
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@name='et_sc' and text()='A Sponsor Name']")).exists())


		// and that same row as a td with a button inside for editing
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='etActions']/button[contains(@class, 'btnEtEdit')]")).exists())

		// and viewing
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='etActions']/button[contains(@class, 'btnEtView')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='etActions']/button[contains(@class, 'btnEtDel')]")).exists())
		;

		Mockito.verify(mockEtDao).create(etName, descr, defHrs, pinHrs, scid);

	}

	/** 
	 * Note: must use jsonpath as opposed to xpath since returning an json and
	 * not html
	 * 
	 * Test that the controller returns JSON to present the selected event type details
	 */
	@Test
	@WithMockUser(username= "admin", password="admin")
	public void testAjaxViewEventType() throws Exception {

		Mockito.when(mockEtDao.fetchEventTypeById(Mockito.anyInt())).thenReturn(et1);

		// ready to test
		mvc.perform(get("/eventTypes/ajax/eventType/1")

				.contentType(MediaType.APPLICATION_JSON))

		.andExpect(status().isOk())

		// expecting a json object whose event id better be 1
		.andExpect(jsonPath("$.etid", is(1)))

		// and short name better be "gds" 
		.andExpect(jsonPath("$.name", is("gds")))

		// and with the longer name/description of 'great day of service for test'
		.andExpect(jsonPath("$.description", is("great day of service for test")))

		// and with default hours of 5.0
		.andExpect(jsonPath("$.defHours", is(5.0)))

		// and with pin hours false
		.andExpect(jsonPath("$.pinHours", is(false)))

		// and with default client name of 'Habitat for Humanity'
		.andExpect(jsonPath("$.defClient.name", is("Habitat for Humanity")))
		;

		// verify the dao got involved
		Mockito.verify(mockEtDao).fetchEventTypeById(1);

	}

	/**
	 * Make sure the controller is updating an existing event when asked.
	 * Note all the parameters are valid.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxEditEventTypeTest_whenParamValid() throws Exception {

		int etid = 1;
		String name = "ftp";
		String descr = "For Testing Purposes";
		Double defHrs = 3.0;
		boolean pinHrs = true;
		Integer scid = 2;

		// change the values of our current event type
		et1.setDefClient(sc2)
			.setDefHours(defHrs)
			.setDescription(descr)
			.setName(name)
			.setPinHours(pinHrs);
		
		// mock dependencies
		Mockito.doNothing().when(mockEtDao).update(1, "ftp", "For Testing Purposes", 3.0, true, 2);
		Mockito.when(mockEtDao.fetchEventTypeById(1)).thenReturn(et1);

		// now perform the test and pretend that jquery sends in the parameters to update
		// the event type
		mvc.perform(post("/eventTypes/ajax/editEt")
				.param("name", name)
				.param("descr", descr)
				.param("defHrs", String.valueOf(defHrs))
				.param("pinHrs", String.valueOf(pinHrs))
				.param("scid", String.valueOf(scid))
				.param("etid", String.valueOf(etid))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// there's a row in our table that has a et name/description td inside whose text better be '(ftp) For Testing Purposes' 
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_name_descr' and text()='(ftp) For Testing Purposes']")).exists())

		// and there's a row in our table that has a et default hour td inside whose text better be '3' 
		.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_hr' and text()='3']")).exists())

		// and there's a row in our table that has a et default service client name td inside whose text better be 'Meals on Wheels' 
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@name='et_sc' and text()='Meals on Wheels']")).exists())
		;

		// verify the dao got involved
		Mockito.verify(mockEtDao).update(1, "ftp", "For Testing Purposes", 3.0, true, 2);
		Mockito.verify(mockEtDao).fetchEventTypeById(1);
	}

	/**
	 * Make sure the controller is handling the case where the event type
	 * is invalid (the dao throws an exception). 
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void testAjaxNewEventType_whenExceptionThrown() throws Exception {

		// for this test, our dao will throw an exception like we might
		// see if the database could not add
		Mockito.doThrow(Exception.class) .when(mockEtDao).create(
				Mockito.anyString(),
				Mockito.anyString(),
				Mockito.anyDouble(),
				Mockito.anyBoolean(),
				Mockito.anyInt());

		// ready to test....
		mvc.perform(post("/eventTypes/ajax/addEt")
				.param("name", "")
				.param("descr", "")
				.param("defHrs", String.valueOf(0.0))
				.param("pinHrs", String.valueOf(false))
				.param("scid", String.valueOf(-1))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().is4xxClientError());

		

		// verify that the dao got tickled appropriately
		Mockito.verify(mockEtDao).create(Mockito.anyString(),
				Mockito.anyString(),
				Mockito.anyDouble(),
				Mockito.anyBoolean(),
				Mockito.anyInt());

	}
	
	/**
	 * Make sure the controller is handling the case where the event type
	 * is invalid (invalid params, ParamUtil throws exception). 
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void testAjaxEditEventType_whenExceptionThrown() throws Exception {


		// for this test, our dao will throw an exception like we might
		// see if the database could not add
		Mockito.doThrow(Exception.class) .when(mockEtDao).update(
				Mockito.anyInt(),
				Mockito.anyString(),
				Mockito.anyString(),
				Mockito.anyDouble(),
				Mockito.anyBoolean(),
				Mockito.anyInt());

		// ready to test....
		mvc.perform(post("/eventTypes/ajax/edit")
				.param("name", "")
				.param("descr", "")
				.param("defHrs", String.valueOf("abc"))
				.param("pinHrs", String.valueOf(false))
				.param("scid", String.valueOf(-1))
				.param("etid", String.valueOf(-1))
				
				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().is4xxClientError());

		;


	}
}
