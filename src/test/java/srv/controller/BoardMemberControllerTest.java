package srv.controller;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import srv.controllers.BoardMemberController;
import srv.domain.contact.Contact;
import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
import srv.services.BoardMemberService;


@RunWith(SpringRunner.class)
@WebMvcTest(BoardMemberController.class)
@Import(WebSecurityConfig.class)
public class BoardMemberControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private BoardMemberService mockBmHrListSrv;
	
	@MockBean
	private BoardMemberUserDao mockBmDao;
	
	// handy objects for these tests
	private List<BoardMemberUser> testBoardMembers = new ArrayList<BoardMemberUser>();
	
	private BoardMemberUser bm1;
	private BoardMemberUser bm2;
	
	
	/**
	 * Called before each an every test in order to have sufficient
	 * data for this series of tests.   We make a couple of typical
	 * board members and contacts. 
	 * 
	 */
	@Before
	public void setupTestFixture() {
		
		Contact contact1 = new Contact()
				.setContactId(1)
				.setFirstName("Rusty")
				.setLastName("Buckle")
				.setPrimaryPhone("903-338-3302")
				.setSecondaryPhone("111-333-2422")
				.setEmail("rBuckle19@austincollege.edu")
				;
		
		Contact contact2 = new Contact()
				.setContactId(2)
				.setFirstName("Lizzy")
				.setLastName("Roo")
				.setPrimaryPhone("123-555-2002")
				.setSecondaryPhone(null)
				.setEmail("lRoo18@austincollege.edu")
				;
				
		// uid, username, contactInfo, expectedGradYear, aff, hasCar, carCapacity
		bm1 = new BoardMemberUser(1, "rbuckle19", contact1, 2023, null, false, 0).setIsCoChair(false);
		bm2 = new BoardMemberUser(2, "lRoo18", contact2, 2022, null, false, 0).setIsCoChair(true);
		
		testBoardMembers.add(bm1);
		testBoardMembers.add(bm2);
				
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
	 * Make sure the base page shows a table of 2 board members (one who is a co-chair and one
	 * who is not) with buttons to allow the user to create a new board members  while also 
	 * editing, viewing, and deleting a board member, and making a board member a co-chair
	 * 
	 * @throws Excpetion
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest() throws Exception {

		// Mock dependencies
		Mockito.when(mockBmDao.listAllBoardMemberUsers()).thenReturn(testBoardMembers);

		mvc.perform(get("/boardmembers")
				.contentType(MediaType.TEXT_HTML))
		.andExpect(status().isOk())

		// our page displays a table somewhere inside for showing board members
		.andExpect(xpath(dquote("//table[@id='bmTbl']")).exists())

		// and there's a row in our table that has a username td inside whose text better be 'rbuckle19' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='bm_username' and text()='rbuckle19']")).exists())

		// and there's a row in our table that has a full name td inside whose text better be 'Rusty Buckle' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='bm_fullName' and text()='Rusty Buckle']")).exists())

		// and there's a row in our table that has an email td inside whose text better be 'rBuckle19@austincollege.edu' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='bm_email' and text()='rBuckle19@austincollege.edu']")).exists())

		// and there's a row in our table that has a phone num td inside whose text better be '903-338-3302' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='bm_phoneNum' and text()='903-338-3302']")).exists())

		// and there's a row in our table that has an expected grad year td inside whose text better be '2023' 
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@name='bm_gradYr' and text()='2023']")).exists())

		
		
		// and that same row as a td with a button inside for editing
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='bmActions']/button[contains(@class, 'btnBmEdit')]")).exists())

		// and viewing
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='bmActions']/button[contains(@class, 'btnBmView')]")).exists())

		// and deleting
		.andExpect(xpath(dquote("//tr[@id='row1']/td[@class='bmActions']/button[contains(@class, 'btnBmDel')]")).exists())

		

		// and there's a row in our table that has a username td inside whose text better be 'lRoo18' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='bm_username' and text()='lRoo18']")).exists())

		// and there's a row in our table that has a full name td inside whose text better be 'Lizzy Roo' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='bm_fullName' and text()='Lizzy Roo']")).exists())

		// and there's a row in our table that has an email td inside whose text better be 'lRoo18@austincollege.edu' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='bm_email' and text()='lRoo18@austincollege.edu']")).exists())

		// and there's a row in our table that has a phone num td inside whose text better be '123-555-2002' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='bm_phoneNum' and text()='123-555-2002']")).exists())

		// and there's a row in our table that has an expected grad year td inside whose text better be '2022' 
		.andExpect(xpath(dquote("//tr[@id='row2']/td[@name='bm_gradYr' and text()='2022']")).exists())

		
		// and our page better have a delete dialog defined/hidden
		.andExpect(xpath(dquote("//div[@id='dlgDelete' and @title='DELETE SELECTED BOARD MEMBER']")).exists())
					
		;
	}
	

	/**
	 * Test to make sure our controller demotes/deletes the selected board member by
	 * removing the row from the table.
	 * 
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxDeleteBoardMemberTest_whenBoardMemberExists() throws Exception {

		// for this test, our dao will pretend to delete
		Mockito.doNothing().when(mockBmDao).delete(1);

		mvc.perform(post("/boardmembers/ajax/del/1")

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().isOk())

		// it should have the board member's user id that better be 1
		.andExpect(content().string(containsString("1")))

		;

		Mockito.verify(mockBmDao).delete(1);
	}

	/**
	 * Test how our controller responds when an exception is thrown.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void ajaxDeleteBoardMemberTest_whenBoardMemberMissing() throws Exception {

		// for this test, our dao will throw an exception like we might
		// see if the database could not delete
		Mockito.doThrow(Exception.class) .when(mockBmDao).delete(1);


		// let's test....
		mvc.perform(post("/boardmembers/ajax/del/1")

				.contentType(MediaType.TEXT_HTML))

		.andExpect(status().is4xxClientError())
		;


		// did the mock object get tickled appropriately
		Mockito.verify(mockBmDao).delete(1);

	}
}
