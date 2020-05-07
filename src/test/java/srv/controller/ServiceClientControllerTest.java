package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.config.WebSecurityConfig;
import srv.controllers.serviceclients.ServiceClientController;
import srv.domain.contact.ContactDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
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

}
