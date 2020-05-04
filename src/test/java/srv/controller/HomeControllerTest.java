package srv.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
	public void basicHtmlPageTest() throws Exception {
		boolean pageLoadedSucessfully = false;
		pageLoadedSucessfully = UserUtil.userIsServant();

		
		assertTrue(pageLoadedSucessfully);
		
		

	}

}


