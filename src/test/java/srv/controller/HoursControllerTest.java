package srv.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.config.WebSecurityConfig;
import srv.controllers.EventController;
import srv.controllers.HoursController;
import srv.domain.event.Event;
import srv.domain.event.eventype.JdbcTemplateEventTypeDao;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;

@RunWith(SpringRunner.class)
@WebMvcTest(HoursController.class)
@Import(WebSecurityConfig.class)
public class HoursControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	ServiceHoursService hrSvc;
	
	@MockBean
	ServiceHoursDao dao;
	
	// TODO this will need to be fixed/removed when UserUtil and HoursController is fixed
	@MockBean
	JdbcTemplateUserDao uDao;
	
	@MockBean
	UserUtil mockUserUtil;
	
	
	@MockBean
	private EventService mockService;
	
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

	@Test
	public void basicHtmlPageTest() throws Exception {
		
		// train the dao to ask for these when asked to listAll reasons.
		ServiceHours h1 = new ServiceHours().setShid(1).setEvent(new Event().setTitle("Spending Time with Toys for Tots")).setHours((double) 6).setStatus("Approved");
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Teaching Part Time")).setHours((double) 2).setStatus("Pending");

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
	
	/**
	 * Testing hours list for hours manager, specifically the action buttons for different hours' status
	 */
	@Test
	@WithMockUser(username = "user", password = "user")
	public void hoursPageTest() throws Exception {
		
		// train the dao to ask for these when asked to listAll reasons.
		ServiceHours h1 = new ServiceHours().setShid(1).setEvent(new Event().setTitle("Spending Time with Toys for Tots")).setHours((double) 6).setStatus("Approved");
		ServiceHours h2 = new ServiceHours().setShid(2).setEvent(new Event().setTitle("Teaching Part Time")).setHours((double) 2).setStatus("Pending");
		ServiceHours h3 = new ServiceHours().setShid(3).setEvent(new Event().setTitle("Some Name")).setHours(1.0).setStatus("Rejected");

		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(h1);
		dummyList.add(h2);
		dummyList.add(h3);
			
		Mockito.when(hrSvc.filteredHours(null)).thenReturn(dummyList);
		
		// now perform the test
		mvc.perform(get("/hours")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		
		// our page displays a table somewhere inside for showing hours
		.andExpect(xpath(dquote("//table[@id='hrs_tbl']")).exists())

		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'Spending Time with Toys for Tots' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='hrs_eventName' and text()='Spending Time with Toys for Tots']")).exists())

		// and that same row should not have a td with a button inside for editing (since hour is approved)
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='hrActions']/button[contains(@class, 'btnHrEdit')]")).doesNotExist())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='hrActions']/button[contains(@class, 'btnHrDel')]")).doesNotExist())

	
		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'Teaching Part Time' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='hrs_eventName' and text()='Teaching Part Time']")).exists())

		// and that same row better have a td with a button inside for editing (since hour is pending)
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@class='hrActions']/button[contains(@class, 'btnHrEdit')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@class='hrActions']/button[contains(@class, 'btnHrDel')]")).exists())
		
		
		// and there's a row in our table that has a hrs_eventName td inside whose text better be 'Some Name' 
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@name='hrs_eventName' and text()='Some Name']")).exists())

		// and that same row better have a td with a button inside for editing (since hour is rejected)
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@class='hrActions']/button[contains(@class, 'btnHrEdit')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row3']/td[@class='hrActions']/button[contains(@class, 'btnHrDel')]")).exists())

		;
	}
}
