package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.config.WebSecurityConfig;
import srv.controllers.HomeController;
import srv.utils.UserUtil;


/**
 * Tests the /home area of our site. 
 * 
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
@Import(WebSecurityConfig.class)

public class HomeControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserUtil userUtil;

	/**
	 * This test ensures that when the user is just a servant user, the /home page
	 * redirects to the servant's home page.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void servantHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/servant?userid=user"));

	}

	@Test
	@WithMockUser(username = "boardmember", password = "boardmember")
	public void bmHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);
		when(userUtil.userIsBoardMember()).thenReturn(true);

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/boardMember?userid=boardmember"));

	}

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void adminHomeRedirectionTest() throws Exception {

		when(userUtil.userIsServant()).thenReturn(true);
		when(userUtil.userIsBoardMember()).thenReturn(true);
		when(userUtil.userIsAdmin()).thenReturn(true);

		mvc.perform(get("/home").contentType(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/srv/home/admin?userid=admin"));

	}

	@Test
	@WithMockUser(username = "boardmember", password = "boardmember")
	public void boardMemberHomeTest() throws Exception {

		mvc.perform(get("/home/boardMember").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome, Board Member")))
				// testing for the Approve Hours Button - Via the text it has
				.andExpect(content().string(containsString("<p>Approve hours with the proper signatures</p>")))
				// testing a broad statement to make sure we actually have buttons on the page
				.andExpect(content().string(containsString("type=\"button\" class=\"btn btn-White\"")));

	}

	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void adminHomeTest() throws Exception {

		mvc.perform(get("/home/admin").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome, Admin")))
				// testing for the Manage Service Client Button - Via the text it has
				.andExpect(content().string(containsString("<b> Manage Service Clients</b>")))
				// testing a broad statement to make sure we actually have buttons on the page
				.andExpect(content().string(containsString("href=\"/srv/sc/list\" class=\"btn btn-light\"")));

	}

}
