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
import srv.domain.contact.ContactDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
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
	private UserUtil userUtil;
	

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicHtmlPageTest() throws Exception {

		
		when(userUtil.userIsAdmin()).thenReturn(true);
		
		
		// train the dao to ask for these when asked to listAll reasons.
		ServiceClient sc1 = new ServiceClient().setClientId(1).setName("Habitat for Humanity")
				.setBoardMember("Billy Bob").setCategory("Community");
		ServiceClient sc2 = new ServiceClient().setClientId(2).setName("Crisis Center").setBoardMember("Rick Astley")
				.setCategory("Crisis Support");

		List<ServiceClient> dummyList = new ArrayList<ServiceClient>();
		dummyList.add(sc1);
		dummyList.add(sc2);

		Mockito.when(dao.listAll()).thenReturn(dummyList);

		// now perform the test

		mvc.perform(get("/test/sc").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("<li id=\"row_1\"> 1, Habitat for Humanity, Billy Bob, Community</li>")))
				.andExpect(content().string(
						containsString("<li id=\"row_2\"> 2, Crisis Center, Rick Astley, Crisis Support</li>")));

	}
	
	
//	@Test
//	@WithMockUser(username = "admin", password = "admin")
//	public void servantHomeRedirectionTest() throws Exception {
//
//		when(userUtil.userIsAdmin()).thenReturn(true);
//
//		mvc.perform(get("/sc/list").contentType(MediaType.TEXT_HTML))
//				.andExpect(status().isOk())
//				.andExpect(content().string(containsString("Service Client List")))
//				//checks to see if there is a button that adds a Service Client
//				.andExpect(content().string(containsString("class=\"addBtn ui-button ui-widget\" type=\"submit\"\r\n" + 
//						"				value=\"Add Service Client\"")))
//				//checks for a table
//				.andExpect(content().string(containsString("class=\"table ui-widget ui-widget-content table-striped table-bordered table-hover\"")));
//	}
	
	
	//credit to Professor Higgs here for this test
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxAddSerciceClientTest() throws Exception {

        
         when(userUtil.userIsAdmin()).thenReturn(true);

        
         /*
         * prepare dummy client obj
         */
         String clientName = "Habitat for Humanity";
         int cid1 = 1; 
         String bmName = "Billy Bob";
         String category = "Community";
        
         ServiceClient sc1 = new ServiceClient()
                  .setClientId(cid1)
                  .setName(clientName)
                  .setBoardMember(bmName)
                  .setCategory(category);

        
         // when the controller asks the dao to create a client in the database, we 
         // fake it and use our dummy client above (sc1)
         Mockito.when(dao.create(clientName, cid1, -1, bmName, category) ).thenReturn(sc1);

         // now perform the test...pretend that jquery sends in the parameters for a new
         // client...  Our mock dao is trained to return a dummy service client (above)
         // we should see an HTML table row return.
        
         mvc.perform(post("/ajax/addSc")
                  .param("clientName", clientName)
                  .param("mcID", String.valueOf(cid1))
                  .param("ocID", String.valueOf("-1"))
                  .param("bmName", bmName)
                  .param("cat",category)
                 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                 
                  // it should be a table row tagged with right id.
                  .andExpect(content().string(containsString("<tr id=\"scid-1\">")))
                 
                  // it should have the client's name
                  .andExpect(content().string(containsString(clientName)))
                 
                  // other expectations here...
                  ;
        

    }
	
	@Test
    @WithMockUser(username = "admin", password = "admin")
    public void ajaxDeleteSerciceClientTest() throws Exception {

        
         when(userUtil.userIsAdmin()).thenReturn(true);

        
         /*
         * prepare dummy client obj
         */
         String clientName = "Habitat for Humanity";
         int cid1 = 1; 
         String bmName = "Billy Bob";
         String category = "Community";
        
         ServiceClient sc1 = new ServiceClient()
                  .setClientId(cid1)
                  .setName(clientName)
                  .setBoardMember(bmName)
                  .setCategory(category);

        
         // when the controller asks the dao to create a client in the database, we 
         // fake it and use our dummy client above (sc1)
         Mockito.when(dao.create(clientName, cid1, -1, bmName, category) ).thenReturn(sc1);
         

         //For now we are going to delete the Service Client we just added
         //In the future for more robust testing we need to delete
         //an already there Service Client
        
         mvc.perform(post("/ajax/delSc")
                  .param("clientName", clientName)
                  .param("mcID", String.valueOf(cid1))
                  .param("ocID", String.valueOf("-1"))
                  .param("bmName", bmName)
                  .param("cat",category)
                  .param("ID", String.valueOf(cid1))
                 
                  .contentType(MediaType.TEXT_HTML))
        
                  .andExpect(status().isOk())
                //checks to see if there is a button that adds a Service Client
  				.andExpect(content().string(containsString("1 was deleted")));

    }
	
	
	

}
