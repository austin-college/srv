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
import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
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
	private BoardMemberUserDao mockBmUserDao;

	@MockBean
	private UserUtil mockUserUtil;

	// handy objects for these tests
	private List<ServiceClient> testClients = new ArrayList<ServiceClient>();
	private List<BoardMemberUser> testUsers = new ArrayList<BoardMemberUser>();
	private List<Contact> testContacts = new ArrayList<Contact>();

	private ServiceClient sc1;
	private ServiceClient sc2;
	private Contact con1;
	private Contact con2;
	private BoardMemberUser bm1;
	private BoardMemberUser bm2;

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
		
		bm1 = new BoardMemberUser();
		bm1.setUid(1);
		bm1.setContactInfo(new Contact()
						.setFirstName("Randy")
						.setLastName("Jackson"));

		bm2 = new BoardMemberUser();
		bm2.setUid(2);
		bm2.setContactInfo(new Contact()
						.setFirstName("Roo")
						.setLastName("Jack"));
		
		con1 = new Contact()
				.setFirstName("Joe")
				.setLastName("Smith")
				.setContactId(1)
				.setPrimaryPhone("111-222-3333")
				.setSecondaryPhone("444-555-6666")
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
				.setPrimaryPhone("900-900-9003")
				.setSecondaryPhone("800-800-8003")
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
				.setCurrentBoardMember(bm1)
				;

		sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Meals on Wheels")
				.setCategory("Seniors")
				.setMainContact(con2)
				.setCurrentBoardMember(bm2)
				;		

		testClients.add(sc1);
		testClients.add(sc2);
		testUsers.add(bm1);
		testUsers.add(bm2);
		testContacts.add(con1);
		testContacts.add(con2);

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
		.setCurrentBoardMember(bm2)
		;

		// mock dependencies
		Mockito.doNothing().when(mockSrvClientDao).update(1, name, cid1, bmId, category);
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
		Mockito.verify(mockSrvClientDao).update(1, name, cid1, bmId, category);
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
				.setCurrentBoardMember(bm1);

		// when the controller asks the dao to create a service client in the database, we fake it and use our
		// dummy service client above (sc3)
		Mockito.when(mockSrvClientDao.create(name, cid1, bmId, category)).thenReturn(sc3);

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
		Mockito.verify(mockSrvClientDao).create(name, cid1, bmId, category);
	}
	
	/**
	 * Make sure the controller is handling the case where the service client
	 * is invalid (the dao throws an exception).
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxCreateServiceClientTest_whenExceptionThrown() throws Exception {
		
		// for this test, our dao will throw an exception like we might see
		// if the database could not add
		Mockito.doThrow(Exception.class).when(mockSrvClientDao).create(
				Mockito.anyString(),
				Mockito.anyInt(), 
				Mockito.anyInt(), 
				Mockito.anyString());
		
		// ready to test....
		mvc.perform(post("/sc/ajax/addSc")
				.param("name", "")
				.param("cat", "")
				.param("cid1", String.valueOf(-1))
				.param("bmId", String.valueOf(1))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().is4xxClientError());
		;

		// verify that the dao got tickled appropriately
		Mockito.verify(mockSrvClientDao).create(
				Mockito.anyString(),
				Mockito.anyInt(), 
				Mockito.anyInt(), 
				Mockito.anyString());
	}
	
	/**
	 * Make sure the controller is handling the case where the service client
	 * is invalid (the dao throws an exception).
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxUpdateServiceClientTest_whenExceptionThrown() throws Exception {
		
		// for this test, our dao will throw an exception like we might see
		// if the database could not add
		Mockito.doThrow(Exception.class).when(mockSrvClientDao).update(
				Mockito.anyInt(),
				Mockito.anyString(),
				Mockito.anyInt(), 
				Mockito.anyInt(), 
				Mockito.anyString());
		
		// ready to test....
		mvc.perform(post("/sc/ajax/editSc")
				.param("name", "")
				.param("cat", "")
				.param("cid1", String.valueOf(-1))
				.param("bmId", String.valueOf(1))
				.param("scid", String.valueOf(1))

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().is4xxClientError());
		;
		

		// verify that the dao got tickled appropriately
		Mockito.verify(mockSrvClientDao).update(
				Mockito.anyInt(),
				Mockito.anyString(),
				Mockito.anyInt(), 
				Mockito.anyInt(), 
				Mockito.anyString());
	}
	
	/** 
	 * Note: must use jsonpath as opposed to xpath since returning an json and
	 * not html
	 * 
	 * Test that the controller returns JSON to present the selected contact details
	 */
	@Test
	@WithMockUser(username= "admin", password="admin")
	public void ajaxFetchContactTest() throws Exception {

		Mockito.when(mockConDao.fetchContactById(Mockito.anyInt())).thenReturn(con1);

		// ready to test
		mvc.perform(get("/sc/ajax/contact/1")

				.contentType(MediaType.APPLICATION_JSON))

		.andExpect(status().isOk())

		// expecting a json object whose contact id better be 1
		.andExpect(jsonPath("$.contactId", is(1)))

		// and first name better be 'Joe'
		.andExpect(jsonPath("$.firstName", is("Joe")))

		// and last name better be 'Smith'
		.andExpect(jsonPath("$.lastName", is("Smith")))

		// and with a mobile phone number better be '111-222-3333'
		.andExpect(jsonPath("$.primaryPhone", is("111-222-3333")))

		// and with a work phone number better be '444-555-6666'
		.andExpect(jsonPath("$.secondaryPhone", is("444-555-6666")))

		// and with a street name of '119 Main St'
		.andExpect(jsonPath("$.street", is("119 Main St")))
		
		// and with city of 'Sherman'
		.andExpect(jsonPath("$.city", is("Sherman")))
		
		// and with state of "TX"
		.andExpect(jsonPath("$.state", is("TX")))
		
		// and with zipcode '75090'
		.andExpect(jsonPath("$.zipcode", is("75090")))
		
		// and with an email 'jsmith19@austincollege.edu'
		.andExpect(jsonPath("$.email", is("jsmith19@austincollege.edu")))
		;

		// verify the dao got involved
		Mockito.verify(mockConDao).fetchContactById(1);

	}
	
	/** 
	 * Make sure the base page shows a table of 2 service clients with buttons to allow
	 * the user to create a new service client while also editing, viewing, and deleting
	 * a service client
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basePageTest() throws Exception {

		// Mock dependencies
		Mockito.when(mockSrvClientDao.listAll()).thenReturn(testClients);
		Mockito.when(mockBmUserDao.listAll()).thenReturn(testUsers);
		Mockito.when(mockConDao.listAll()).thenReturn(testContacts);
		Mockito.when(mockUserUtil.userIsAdmin()).thenReturn(true);

		mvc.perform(get("/sc")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())

		// our page displays a table somewhere inside for showing service clients
		.andExpect(xpath(dquote("//table[@id='client_tbl']")).exists())

		// and there's a row in our table that has a service client name/title td inside whose text better be 'Habitat for Humanity' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_title' and text()='Habitat for Humanity']")).exists())

		// and there's a row in our table that has a main contact name td inside whose text better be 'Joe Smith' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_contact_name' and text()='Joe Smith']")).exists())

		// and there's a row in our table that has a board member name td inside whose text better be 'Randy Jackson' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_bm_name' and text()='Randy Jackson']")).exists())

		// and there's a row in our table that has a category td inside whose text better be 'Community' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_category' and text()='Community']")).exists())
		
		
		
		// and that same row as a td with a button inside for editing
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@class='text-center scActions']/button[contains(@class, 'btnScEdit')]")).exists())

		// and viewing
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@class='text-center scActions']/button[contains(@class, 'btnScView')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@class='text-center scActions']/button[contains(@class, 'btnScDel')]")).exists())

		

		// and there's a row in our table that has a service client name/title td inside whose text better be 'Meals on Wheels' 
		.andExpect(xpath(dquote("//tr[@id='scid-2']/td[@name='sc_title' and text()='Meals on Wheels']")).exists())

		// and there's a row in our table that has a main contact name td inside whose text better be 'Tina Franklin' 
		.andExpect(xpath(dquote("//tr[@id='scid-2']/td[@name='sc_contact_name' and text()='Tina Franklin']")).exists())

		// and there's a row in our table that has a board member name td inside whose text better be 'Roo Jack' 
		.andExpect(xpath(dquote("//tr[@id='scid-2']/td[@name='sc_bm_name' and text()='Roo Jack']")).exists())
		
		// and there's a row in our table that has a category td inside whose text better be 'Community' 
		.andExpect(xpath(dquote("//tr[@id='scid-1']/td[@name='sc_category' and text()='Community']")).exists())

		

		// and our page better have a create a new service client dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='addDlg' and @title='New Sponsor']")).exists())
		
		// and our page better have a delete service client dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='deleteDlg' and @title='DELETE SPONSOR']")).exists())
		
		// and our page better have a view service client dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='viewDlg' and @title='Sponsor Details']")).exists())
		
		// and our page better have an edit service client dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='editDlg' and @title='Update Sponsor']")).exists())
		;
	}

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicTest() throws Exception {

		when(mockUserUtil.userIsAdmin()).thenReturn(true);
		
		// Create dependencies
		
		// Main/primary contact for service client 1
		Contact main1 = new Contact().setContactId(2).setFirstName("Lois").setLastName("Lane").setEmail("llane86@gmail.com")
				.setPrimaryPhone("803-423-1257").setSecondaryPhone("800-232-1211").setStreet("118 NW Crawford Street")
				.setCity("Sherman").setState("TX").setZipcode("75090");
		
		
		// Contact information for current board member for service client 1
		Contact bmContact1 = new Contact().setContactId(5).setFirstName("Hunter").setLastName("Couturier").setEmail("hCouturier@gmail.com")
				.setPrimaryPhone("803-426-1527").setSecondaryPhone("800-191-9412").setStreet("24 First Street")
				.setCity("Dension").setState("TX").setZipcode("75021");
				
		// Current board member for service client 1
		User bm1 = new User().setUid(2).setUsername("hCouturier").setContactInfo(bmContact1);

		// Main/primary contact for service client 2
		Contact main2 = new Contact().setContactId(2).setFirstName("Lois").setLastName("Lane").setEmail("llane86@gmail.com")
				.setPrimaryPhone("803-423-1257").setSecondaryPhone("800-232-1211").setStreet("118 NW Crawford Street")
				.setCity("Sherman").setState("TX").setZipcode("75090");


		// Contact information for current board member for service client 2
		Contact bmContact2 = new Contact().setContactId(5).setFirstName("Emma").setLastName("Driscoll").setEmail("eDriscoll@gmail.com")
				.setPrimaryPhone("803-426-1527").setSecondaryPhone("800-191-9412").setStreet("25 First Street")
				.setCity("Dension").setState("TX").setZipcode("75021");

		// Current board member for service client 2
		User bm2 = new User().setUid(3).setUsername("eDriscoll").setContactInfo(bmContact2);
				
				
		// train the dao to ask for these when asked to listAll reasons.
		ServiceClient sc1 = new ServiceClient().setClientId(1).setName("Habitat for Humanity")
				.setMainContact(main1).setCurrentBoardMember(bm1).setCategory("Community");
	
		ServiceClient sc2 = new ServiceClient().setClientId(2).setName("Crisis Center")
				.setMainContact(main2).setCurrentBoardMember(bm2).setCategory("Crisis Support");

		List<ServiceClient> dummyList = new ArrayList<ServiceClient>();
		dummyList.add(sc1);
		dummyList.add(sc2);

		Mockito.when(mockSrvClientDao.listAll()).thenReturn(dummyList);

		// now perform the test 

		mvc.perform(get("/test/sc").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("<li id=\"row_1\"> 1, Habitat for Humanity, Lois Lane, hCouturier, Community</li>")))
				.andExpect(content()
						.string(containsString("<li id=\"row_2\"> 2, Crisis Center, Lois Lane, eDriscoll, Crisis Support</li>")));

	}

}
