package srv.controller;

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
import srv.controllers.ServiceClientController;
import srv.domain.contact.Contact;
import srv.domain.contact.ContactDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;
import srv.utils.UserUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceClientController.class)
@Import(WebSecurityConfig.class)

public class ServiceClientControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ServiceClientDao mockSrvClientDao;

	@MockBean
	private ContactDao mockConDao;

	@MockBean
	private UserDao mockUserDao;

	@MockBean
	private UserUtil mockUserUtil;

	// handy objects for these tests
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();

	private ServiceClient sc1;
	private ServiceClient sc2;
	private Contact con1;
	private Contact con2;
	private User bm1;
	private User bm2;

	/**
	 * 
	 * TODO will need to refactor the users to be board member users
	 * 
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * service clients, a couple of typical contacts, a couple of
	 * users.  
	 * 
	 */
	@Before
	public void setupTestFixture() {
		
		bm1 = new User()
				.setUid(1)
				.setContactInfo(new Contact()
						.setFirstName("Randy")
						.setLastName("Jackson"))
				;

		bm2 = new User()
				.setUid(2)
				.setContactInfo(new Contact()
						.setFirstName("Roo")
						.setLastName("Jack"))
				;
		
		con1 = new Contact()
				.setFirstName("Joe")
				.setLastName("Smith")
				.setContactId(1)
				.setPhoneNumMobile("111-222-3333")
				.setPhoneNumWork("444-555-6666")
				.setStreet("119 Main St")
				.setCity("Sherman")
				.setState("TX")
				.setEmail("jsmith19@austincollege.edu")
				.setZipcode("75090")
				;

		con2 = new Contact()
				.setFirstName("Tina")
				.setLastName("Franklin")
				.setContactId(2)
				.setPhoneNumMobile("900-900-9003")
				.setPhoneNumWork("800-800-8003")
				.setStreet("120 First St")
				.setCity("Sherman")
				.setState("TX")
				.setEmail("tfrankline19@austincollege.edu")
				.setZipcode("75090")
				;

		sc1 = new ServiceClient()
				.setClientId(1)
				.setName("Habitat for Humanity")
				.setCategory("Community")
				.setMainContact(con1)
				.setOtherContact(con2)
				.setCurrentBoardMember(bm1)
				;

		sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Meals on Wheels")
				.setCategory("Seniors")
				.setMainContact(con2)
				.setOtherContact(con1)
				;		

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
	 * Test how our controller responds when deleting a
	 * service client
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxDeleteServiceClientTest_whenClientExists() throws Exception {

		// for this test, our dao will pretend to delete
		Mockito.doNothing().when(mockSrvClientDao).delete(1);

		mvc.perform(post("/sc/ajax/del/1")

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// it should have the client's id
		.andExpect(content().string(containsString("1")))
		
		;

        // did the mock object get tickled appropriately
		Mockito.verify(mockSrvClientDao).delete(1);

	}
	
	/** 
	 * Note: must use jsonpath as opposed to xpath since returning an json and
	 * not html
	 * 
	 * Test that the controller returns JSON to present the selected service client details
	 */
	@Test
	@WithMockUser(username= "admin", password="admin")
	public void ajaxViewServiceClientTest() throws Exception {

		Mockito.when(mockSrvClientDao.fetchClientById(Mockito.anyInt())).thenReturn(sc1);

		// ready to test
		mvc.perform(get("/sc/ajax/sc/1")

				.contentType(MediaType.APPLICATION_JSON))

		.andExpect(status().isOk())

		// expecting a json object whose service client id better be 1
		.andExpect(jsonPath("$.scid", is(1)))

		// and short name better be "Habitat for Humanity" 
		.andExpect(jsonPath("$.name", is("Habitat for Humanity")))

		// and with the category of 'Community'
		.andExpect(jsonPath("$.category", is("Community")))

		// and with a current board member with id 1
		.andExpect(jsonPath("$.currentBoardMember.uid", is(1)))

		// and with a main contact with id 1
		.andExpect(jsonPath("$.mainContact.contactId", is(1)))

		// and with an other contact with id 2
		.andExpect(jsonPath("$.otherContact.contactId", is(2)))
		;

		// verify the dao got involved
		Mockito.verify(mockSrvClientDao).fetchClientById(1);

	}
	
	
	/**
	 * Make sure the controller is updating an existing service client when asked.
	 * Note all the parameters are valid.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxUpdateServiceClient_whenParamValid() throws Exception {

		int scid = 1;
		String name = "Crisis Center";
		String category = "Variety";
		int cid1 = 2;
		int cid2 = 1;
		int bmId = 2;

		// change the values of our current event type
		sc1.setName(name)
		.setCategory(category)
		.setMainContact(con2)
		.setOtherContact(con1)
		.setCurrentBoardMember(bm2)
		;

		// mock dependencies
		Mockito.doNothing().when(mockSrvClientDao).update(1, name, cid1, cid2, bmId, category);
		Mockito.when(mockSrvClientDao.fetchClientById(1)).thenReturn(sc1);

		// now perform the test and pretend that jquery sends in the parameters to update
		// the service client
		mvc.perform(post("/sc/ajax/editSc")
				.param("name", name)
				.param("cat", category)
				.param("scid", String.valueOf(scid))
				.param("cid1", String.valueOf(cid1))
				.param("cid2", String.valueOf(cid2))
				.param("bmId", String.valueOf(bmId))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// there's a row in our table that has a name/title td inside whose text better be 'Crisis Center' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_title' and text()='Crisis Center']")).exists())

		// and there's a row in our table that has a main contact name td inside whose text better be 'Tina Franklin' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_contact_name' and text()='Tina Franklin']")).exists())

		// and there's a row in our table that has a board member name td inside whose text better be 'Roo Jack' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_bm_name' and text()='Roo Jack']")).exists())

		// and there's a row in our table that has a category td inside whose text better be 'Variety'
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_category' and text()='Variety']")).exists())

		;

		// verify the dao got involved
		Mockito.verify(mockSrvClientDao).update(1, name, cid1, cid2, bmId, category);
		Mockito.verify(mockSrvClientDao).fetchClientById(1);
	}
	
	/**
	 * Test how our controller responds when an exception is thrown
	 * when deleting a service client.
	 * 
	 * @throws Exception
	 */
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxDeleteServiceClientTest_whenClientMissing() throws Exception {
        
		// for this test, our service will throw an exception like we might
		// see if the database could not delete
		Mockito.doThrow(Exception.class) .when(mockSrvClientDao).delete(1);
				
		 
		// let's test....
         mvc.perform(post("/sc/ajax/del/1")
        		 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().is4xxClientError())
                  ;

                  
         // did the mock object get tickled appropriately
         Mockito.verify(mockSrvClientDao).delete(1);
       
    }

	/**
	 * Make sure the controller is creating a new service client when asked.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxCreateServiceClientTest() throws Exception {

		// prepare dummy service client obj
		String name = "Meals on Wheels";
		int scid3 = 3;
		String category = "Seniors";
		int cid1 = 1;
		int cid2 = 2;
		int bmId = 1;

		ServiceClient sc3 = new ServiceClient()
				.setName(name)
				.setClientId(scid3)
				.setCategory(category)
				.setMainContact(con1)
				.setOtherContact(con2)
				.setCurrentBoardMember(bm1);

		// when the controller asks the dao to create a service client in the database, we fake it and use our
		// dummy service client above (sc3)
		Mockito.when(mockSrvClientDao.create(name, cid1, cid2, bmId, category)).thenReturn(sc3);

		// now perform the test and pretend that jquery sends in the parameters to create 
		// the service client
		mvc.perform(post("/sc/ajax/addSc")
				.param("name", name)
				.param("cat", category)
				.param("cid1", String.valueOf(cid1))
				.param("cid2", String.valueOf(cid2))
				.param("bmId", String.valueOf(bmId))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// there's a row in our table that has a name/title td inside whose text better be 'Meals on Wheels' 
		.andExpect(xpath(dquote("//tr[@id='scid-3']/td[@name='sc_title' and text()='Meals on Wheels']")).exists())

		// and there's a row in our table that has a main contact name td inside whose text better be 'Joe Smith' 
		.andExpect(xpath(dquote("//tr[@id='scid-3']/td[@name='sc_contact_name' and text()='Joe Smith']")).exists())

		// and there's a row in our table that has a board member name td inside whose text better be 'Randy Jackson' 
		.andExpect(xpath(dquote("//tr[@id='scid-3']/td[@name='sc_bm_name' and text()='Randy Jackson']")).exists())

		// and there's a row in our table that has a category td inside whose text better be 'Seniors'
		.andExpect(xpath(dquote("//tr[@id='scid-3']/td[@name='sc_category' and text()='Seniors']")).exists())
		;
		
		// verify that the dao got involved
		Mockito.verify(mockSrvClientDao).create(name, cid1, cid2, bmId, category);
	}
	
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicHtmlPageTest() throws Exception {

		when(mockUserUtil.userIsAdmin()).thenReturn(true);
		
		// Create dependencies
		
		// Main/primary contact for service client 1
		Contact main1 = new Contact().setContactId(2).setFirstName("Lois").setLastName("Lane").setEmail("llane86@gmail.com")
				.setPhoneNumWork("803-423-1257").setPhoneNumMobile("800-232-1211").setStreet("118 NW Crawford Street")
				.setCity("Sherman").setState("TX").setZipcode("75090");
		
		// Other/secondary contact for service client 1
		Contact other1 = new Contact().setContactId(3).setFirstName("Joe").setLastName("Smith").setEmail("jsmith12@gmail.com")
				.setPhoneNumWork("903-444-4440").setPhoneNumMobile("401-322-1201").setStreet("25 Frieda Drive")
				.setCity("Gunter").setState("TX").setZipcode("75058");
		
		// Contact information for current board member for service client 1
		Contact bmContact1 = new Contact().setContactId(5).setFirstName("Hunter").setLastName("Couturier").setEmail("hCouturier@gmail.com")
				.setPhoneNumWork("803-426-1527").setPhoneNumMobile("800-191-9412").setStreet("24 First Street")
				.setCity("Dension").setState("TX").setZipcode("75021");
				
		// Current board member for service client 1
		User bm1 = new User().setUid(2).setUsername("hCouturier").setContactInfo(bmContact1);

		// Main/primary contact for service client 2
		Contact main2 = new Contact().setContactId(2).setFirstName("Lois").setLastName("Lane").setEmail("llane86@gmail.com")
				.setPhoneNumWork("803-423-1257").setPhoneNumMobile("800-232-1211").setStreet("118 NW Crawford Street")
				.setCity("Sherman").setState("TX").setZipcode("75090");

		// Other/secondary contact for service client 2
		Contact other2 = new Contact().setContactId(3).setFirstName("Joe").setLastName("Smith").setEmail("jsmith12@gmail.com")
				.setPhoneNumWork("903-444-4440").setPhoneNumMobile("401-322-1201").setStreet("25 Frieda Drive")
				.setCity("Gunter").setState("TX").setZipcode("75058");

		// Contact information for current board member for service client 2
		Contact bmContact2 = new Contact().setContactId(5).setFirstName("Emma").setLastName("Driscoll").setEmail("eDriscoll@gmail.com")
				.setPhoneNumWork("803-426-1527").setPhoneNumMobile("800-191-9412").setStreet("25 First Street")
				.setCity("Dension").setState("TX").setZipcode("75021");

		// Current board member for service client 2
		User bm2 = new User().setUid(3).setUsername("eDriscoll").setContactInfo(bmContact2);
				
				
		// train the dao to ask for these when asked to listAll reasons.
		ServiceClient sc1 = new ServiceClient().setClientId(1).setName("Habitat for Humanity")
				.setMainContact(main1).setOtherContact(other1).setCurrentBoardMember(bm1).setCategory("Community");
	
		ServiceClient sc2 = new ServiceClient().setClientId(2).setName("Crisis Center")
				.setMainContact(main2).setOtherContact(other2).setCurrentBoardMember(bm2).setCategory("Crisis Support");

		List<ServiceClient> dummyList = new ArrayList<ServiceClient>();
		dummyList.add(sc1);
		dummyList.add(sc2);

		Mockito.when(mockSrvClientDao.listAll()).thenReturn(dummyList);

		// now perform the test 

		mvc.perform(get("/test/sc").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("<li id=\"row_1\"> 1, Habitat for Humanity, Lois Lane, Joe Smith, hCouturier, Community</li>")))
				.andExpect(content()
						.string(containsString("<li id=\"row_2\"> 2, Crisis Center, Lois Lane, Joe Smith, eDriscoll, Crisis Support</li>")));

	}

}
