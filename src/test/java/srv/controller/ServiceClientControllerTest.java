package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * service clients, a couple of typical contacts.  
	 * 
	 */
	@Before
	public void setupTestFixture() {

		Contact con1 = new Contact()
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

		Contact con2 = new Contact()
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
	
	
	//credit to Professor Higgs here for this test
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxAddServiceClientTest() throws Exception {

         when(mockUserUtil.userIsAdmin()).thenReturn(true);
                 
         /*
         * prepare dummy client obj
         */
         String clientName = "Habitat for Humanity";
         int cid1 = 1; 
         String category = "Community";
        
         ServiceClient sc1 = new ServiceClient()
                  .setClientId(cid1)
                  .setName(clientName)
                  .setCategory(category);
        
                 
         // when the controller asks the dao to create a client in the database, we 
         // fake it and use our dummy client above (sc1)
         Mockito.when(mockSrvClientDao.create(clientName, cid1, -1, cid1, category)).thenReturn(sc1);
 
         // now perform the test...pretend that jquery sends in the parameters for a new
         // client...  Our mock dao is trained to return a dummy service client (above)
         // we should see an HTML table row return.
         mvc.perform(post("/ajax/addSc")
                  .param("clientName", clientName)
                  .param("mcID", String.valueOf(cid1))
                  .param("ocID", String.valueOf("-1"))
                  .param("bmID", String.valueOf(cid1))
                  .param("cat", category)
                 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should be a table row tagged with right id.
                  .andExpect(content().string(containsString("<tr id=\"scid-1\">")))
                 
                  // it should have the client's name
                  .andExpect(content().string(containsString(clientName)))
                 
                  // other expectations here...
                 .andExpect(content().string(containsString(category)));
    }
	
	

}
