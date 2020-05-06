package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import srv.controllers.home.HomeController;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)

public class HomeControllerTest {

	@Autowired
	private MockMvc mvc;


	@Test
	@WithMockUser(username = "user", password = "user")
	public void servantHomeRedirectionTest() throws Exception {

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/srv/viewHours?userid=user"));

	}

	/*
	 * The following tests should work, however with the way we are using @withMockUser
	 * is breaking userUtil which is only able to process mocked users as
	 * servants, we will have to fix that to continue
	 */
	//	@Test
	//	@WithMockUser(username = "boardmember", password = "boardmember")
	//	public void adminHomeRedirectionTest() throws Exception {
	//		
	//		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML))
	//		.andExpect(status().is3xxRedirection())
	//		.andExpect(redirectedUrl("/srv/home/boardMember?userid=boardmember"));
	//
	//	}
	//	
	//	@Test
	//	@WithMockUser(username = "admin", password = "admin")
	//	public void adminHomeRedirectionTest() throws Exception {
	//		
	//		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML))
	//		.andExpect(status().is3xxRedirection())
	//		.andExpect(redirectedUrl("/srv/home/admin?userid=admin"));
	//
	//	}	
	
	@Test
	@WithMockUser(username = "boardmember", password = "boardmember")
	public void boardMemberHomeTest() throws Exception {

		mvc.perform(get("/home/boardMember").contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Welcome, Board Member")))
		//testing for the Approve Hours Button - Via the text it has
		.andExpect(content().string(containsString("<p>Approve hours with the proper signatures</p>")))
		//testing a broad statement to make sure we actually have buttons on the page
		.andExpect(content().string(containsString("type=\"button\" class=\"btn btn-White\"")));

	}
	
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void adminHomeTest() throws Exception {

		mvc.perform(get("/home/admin").contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Welcome, Admin")))
		//testing for the Manage Service Client Button - Via the text it has
		.andExpect(content().string(containsString("<b> Manage Service Clients</b>")))
		//testing a broad statement to make sure we actually have buttons on the page
		.andExpect(content().string(containsString("href=\"/srv/sc/list\" class=\"btn btn-light\"")));

	}

}


