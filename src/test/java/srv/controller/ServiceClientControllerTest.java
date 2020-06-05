package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

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
	private ServiceClientDao dao;

	@MockBean
	private ContactDao cDao;
	
	@MockBean
	private UserDao uDao;
	
	@MockBean
	private UserUtil userUtil;
	

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicHtmlPageTest() throws Exception {

		when(userUtil.userIsAdmin()).thenReturn(true);
		
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

		Mockito.when(dao.listAll()).thenReturn(dummyList);

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

         when(userUtil.userIsAdmin()).thenReturn(true);
                 
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
         Mockito.when(dao.create(clientName, cid1, -1, cid1, category)).thenReturn(sc1);
 
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
	
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxDeleteServiceClientTest() throws Exception {
        
         when(userUtil.userIsAdmin()).thenReturn(true);
        
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
         Mockito.when(dao.create(clientName, cid1, -1, cid1, category) ).thenReturn(sc1);
         

         //For now we are going to delete the Service Client we just added
         //In the future for more robust testing we need to delete
         //an already there Service Client        
         mvc.perform(post("/ajax/delSc")
                  .param("ID", String.valueOf(cid1))
                 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())

  				  .andExpect(content().string(containsString("1 was deleted")));
    }
	
	
	

}
