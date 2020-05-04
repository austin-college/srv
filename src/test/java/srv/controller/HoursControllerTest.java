package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.controllers.hours.HoursController;
import srv.controllers.serviceclients.ServiceClientController;
import srv.domain.contact.ContactDao;
import srv.domain.event.Event;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;
import srv.services.ServiceHoursService;

@RunWith(SpringRunner.class)
@WebMvcTest(HoursController.class)
public class HoursControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ServiceHoursService hrSvc;
	
	@MockBean
	ServiceHoursDao dao;

	@Test
	public void basicHtmlPageTest() throws Exception {
		
		// train the dao to ask for these when asked to listAll reasons.
		ServiceHours h1 = new ServiceHours().setShid(1).setEvent(new Event().setTitle("Spending Time with Toys for Tots")).setHours(6);
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Teaching Part Time")).setHours(2);

		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(h1);
		dummyList.add(h2);

		Mockito.when(hrSvc.listHours()).thenReturn(dummyList);

		// now perform the test

		mvc.perform(get("/test/hours").contentType(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(content()
						.string(containsString("<li  id=\"row_1\"> Num: 1, 6 hours, Event Title: Spending Time with Toys for Tots</li>")))
				.andExpect(content().string(
						containsString("<li  id=\"row_2\"> Num: 2, 2 hours, Event Title: Teaching Part Time</li>")));

	}

}
