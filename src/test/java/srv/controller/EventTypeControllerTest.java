package srv.controller;


import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


@RunWith(SpringRunner.class)
@WebMvcTest(EventTypeController.class)
@Import(WebSecurityConfig.class)

class EventTypeControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EventTypeDao mockEtDao;

	
	// handy objects for these tests
	private List<EventType> testTypes = new ArrayList<EventType>();
	
	private EventType et1;
	private EventType et2;	
	
	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * event types and service clients.  
	 * 
	 */
	@BeforeEach
	public void setupTestFixture() {
	
		ServiceClient sc1 = new ServiceClient()
				.setClientId(1)
				.setName("Habitat for Humanity")
				.setCategory("Community");

		ServiceClient sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Meals on Wheels")
				.setCategory("Seniors");		
		
		EventType et1 = new EventType()
				.setEtid(1)
				.setName("gds")
				.setDescription("great day of service for test")
				.setDefHours(5)
				.setDefClient(sc1)
				.setPinHours(false);

		EventType et2 = new EventType()
				.setEtid(2)
				.setName("fws")
				.setDescription("first we serve for test")
				.setDefHours(3)
				.setDefClient(sc2)
				.setPinHours(false);

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

		mvc.perform(get("/eventTypes")
				.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a table somewhere inside for showing event types
			.andExpect(xpath(dquote("//table[@id='tblEvType']")).exists())
			
			// and there's a row in our table that has a et name/description td inside whose text better be '(gds) great day of service for test' 
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='et_name_descr etView' and text()='(gds) great day of service for test']")).exists())
			
			// and there's a row in our table that has a et default hour td inside whose text better be '5' 
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='et_hr etView' and text()='5']")).exists())
	
			// and there's a row in our table that has a et default service client name td inside whose text better be 'Habitat for Humanity' 
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='et_sc etView' and text()='Habitat for Humanity']")).exists())
			
		
			// and that same row as a td with a button inside for editing
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtEdit')]")).exists())
			
			// and viewing
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtView')]")).exists())

			// and deleting
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='etActions']/button[contains(@class, 'btnEtDel')]")).exists())

			
			// and there's a row in our table that has a et name/description td inside whose text better be '(fws) first we serve for test' 
			.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@class='et_name_descr etView' and text()='(fws) first we serve for test']")).exists())
			
			// and there's a row in our table that has a et default hour td inside whose text better be '3' 
			.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@class='et_hr etView' and text()='3']")).exists())
	
			// and there's a row in our table that has a et default service client name td inside whose text better be 'Meals on Wheels' 
			.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@class='et_sc etView' and text()='Meals on Wheels']")).exists())
			;
	}
}
