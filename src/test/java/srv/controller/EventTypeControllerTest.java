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
import srv.domain.serviceclient.ServiceClientDao;


@RunWith(SpringRunner.class)
@WebMvcTest(EventTypeController.class)
@Import(WebSecurityConfig.class)

class EventTypeControllerTest {

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
	@BeforeEach
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
				.setDefHours(3.0)
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
			.andExpect(xpath(dquote("//tr[@id='etid-1']/td[@class='et_name_descr etView' and text()='(gds) great day of service for test']")).exists())
			
			// and there's a row in our table that has a et default hour td inside whose text better be '5.0' 
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
			
			// and there's a row in our table that has a et default hour td inside whose text better be '3.0' 
			.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@class='et_hr etView' and text()='3']")).exists())
	
			// and there's a row in our table that has a et default service client name td inside whose text better be 'Meals on Wheels' 
			.andExpect(xpath(dquote("//tr[@id='etid-2']/td[@class='et_sc etView' and text()='Meals on Wheels']")).exists())
			
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
		
		// when the controller asks the dao to create an event ytpe in the database, we fake it and use our
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
     			.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='et_name_descr etView' and text()='(abc) longer name']")).exists())
     			
     			// and there's a row in our table that has a et default hour td inside whose text better be '5' 
     			.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='et_hr etView' and text()='5']")).exists())
     	
     			// and there's a row in our table that has a et default service client name td inside whose text better be 'A Sponsor Name' 
     			.andExpect(xpath(dquote("//tr[@id='etid-3']/td[@class='et_sc etView' and text()='A Sponsor Name']")).exists())
     			
     		
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
	 * Make sure the controller is handling the case where the service client
	 * is invalid (the dao throws an exception). 
	 * 
	 * @throws Exception
	 */
//	@Test
//    @WithMockUser(username = "admin", password = "admin")
//    public void testAjaxNewEventType_whenExceptionThrown() throws Exception {
//        
//
//		// for this test, our dao will throw an exception like we might
//		// see if the database could not add
//		Mockito.doThrow(Exception.class) .when(mockEtDao).create(
//				Mockito.anyString(),
//				Mockito.anyString(),
//				Mockito.anyDouble(),
//				Mockito.anyBoolean(),
//				Mockito.anyInt());
//				
//		 // ready to test....
//         mvc.perform(post("/eventTypes/ajax/addEt")
//        		 .param("name", "")
//        		 .param("descr", "")
//        		 .param("defHrs", String.valueOf(0.0))
//        		 .param("pinHrs", String.valueOf(false))
//        		 .param("scid", String.valueOf(-1))
//        		 
//                 .contentType(MediaType.TEXT_HTML))
//		  
//         		 .andExpect(status().is4xxClientError());
//
//         		 ;
//        
         //Mockito.verify(mockEtDao).create(1);
       
//    }
}
