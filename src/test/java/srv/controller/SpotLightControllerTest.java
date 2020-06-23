package srv.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static srv.utils.StringUtil.dquote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import srv.config.WebSecurityConfig;
import srv.controllers.SpotLightController;
import srv.domain.spotlight.SpotLight;
import srv.domain.spotlight.SpotLightDao;

/**
 * Tests to ensure our spotlight management endpoint /spotlight controller is working properly.
 * 
 * @see srv.controller.SpotLightController
 * 
 * @author mahiggs
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SpotLightController.class)
@Import(WebSecurityConfig.class)
public class SpotLightControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private SpotLightDao mockDao;   // the controller needs the dao help;  

	@Autowired
	private WebApplicationContext webApplicationContext;    // context is faked for test


	private static final int DEFAULT_IMAGE_SIZE = 1741373; 

	
	/**
	 * Make sure the base page presents the spotlight image and the spotlight
	 * text and elements to manage.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void basicBasePageTest() throws Exception {
		
		SpotLight spot = new SpotLight().setSid(1)
				.setSpotText("example text")
				.setImgType(null)
				.setImgSize(null)
				.setImg("dummy image".getBytes());
		
		
		Mockito.when(mockDao.spotLightById(1)).thenReturn(spot);
				
		mvc.perform(get("/spotlight")
			.contentType(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			
			// our page displays a form to upload image
			.andExpect(xpath(dquote("//form[@action='/srv/spotlight/img/uploadFile']")).exists())
			// with a submit button
			.andExpect(xpath(dquote("//form[@action='/srv/spotlight/img/uploadFile']/button[@type='submit']")).exists())
			
			
			// our image is shown 
			.andExpect(xpath(dquote("//img[@src='/srv/spotlight/img/1']")).exists())
			
			
			// our page displays a form to replace the text
			.andExpect(xpath(dquote("//form[@action='/srv/spotlight/txt']")).exists())
			// with a submit button
			.andExpect(xpath(dquote("//form[@action='/srv/spotlight/txt']/button[@type='submit']")).exists())

			
			
			// there exists an editor div allowing user to interactively edit
			.andExpect(xpath(dquote("//div[@id='editor']")).exists())
			// the editor must show the text from the spotlight object
			.andExpect(xpath(dquote("//div[@id='editor']/p[contains(text(),'example text')]")).exists())
			
			;
		
		
		// make sure the dao was asked to fetch the splotlight obj from db
		Mockito.verify(mockDao).spotLightById(1);

	}
	
	
	
	/**
	 * Our endpoint to retrieve a specific image from the db...
	 * 
	 * When the image if not specified, a default image is provided
	 * by this url endpoint.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void getImageTest_whenImageMissing() throws Exception {
		
		SpotLight spot = new SpotLight().setSid(1)
				.setSpotText("example text")
				.setImgType(null)
				.setImgSize(null)
				.setImg(null);
		
		
		Mockito.when(mockDao.spotLightById(1)).thenReturn(spot);
				
		MvcResult result = mvc.perform(get("/spotlight/img/1")
			.contentType(MediaType.IMAGE_PNG_VALUE))
			.andExpect(status().isOk())
			.andReturn()
			;

		byte[] img = result.getResponse().getContentAsByteArray();
		
		assertNotNull(img);

		/*
		 * So it's not null... it better be our default spotlight image.
		 */
		
		// See /src/main/resources/static/images/defaultSpotlight.jpg
		

		/*
		 * Its not perfect test, but currently our default image is 1741373
		 * byte big. Pretty certain we are using it if the size matches.
		 */
		assertEquals(DEFAULT_IMAGE_SIZE,img.length);
		
	}
	
	
	/**
	 * Our controller should handle when user forgets to specify a file
	 * to upload, but posts to our upload endpoint/url.
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void submitImageTest_whenFileIsMissing() throws Exception {

		/*
		 * Prepare a dummy spotlight object for mock dao to use.
		 */
		SpotLight spot = new SpotLight().setSid(1)
				.setSpotText("example text");
		
		/*
		 * train the mock dao to return our dummy..and expect when needed. 
		 */
		Mockito.when(mockDao.spotLightById(1)).thenReturn(spot);

		/*
		 * Prepare out test environment to use the a mocked up multipartfile object
		 * for this test only.   NOTE:  "file" is the name of the parameter for 
		 * the request.  In this test, we want the image to be null...ie, the user
		 * submitted without choosing a file.   Do we handle it?
		 * 
		 */
        MockMultipartFile fstmp = new MockMultipartFile("file", (byte[])null);
        
        // everything should work for this test
        mvc.perform(MockMvcRequestBuilders.multipart("/spotlight/img/uploadFile")
                .file(fstmp)
                )         
        .andExpect(status().is3xxRedirection());    // if all good, we redirect to base page
        
        Mockito.verifyNoInteractions(mockDao);  // our method should abort before dealing with dao.
		
	}
	
	
	/**
	 * Test when user is uploading an image....
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void submitImageTest() throws Exception {

		/* use the default spot light image for testing too.  NOTE:  if changed, this will break test. */
		ClassPathResource imgFile = new ClassPathResource("/static/images/defaultSpotlight.jpg", this.getClass().getClassLoader());
		
		System.out.println(imgFile.getFile().getAbsolutePath());  // echo to ensure we are getting the file from correct place

		/*
		 * Prepare a dummy spotlight object for mock dao to use.
		 */
		SpotLight spot = new SpotLight().setSid(1)
				.setSpotText("example text");
//				.setImgType(MediaType.IMAGE_PNG_VALUE)
//				.setImgSize(this.DEFAULT_IMAGE_SIZE);
		
		/*
		 * train the mock dao to return our dummy..and expect when needed. 
		 */
		Mockito.when(mockDao.spotLightById(1)).thenReturn(spot);

		/*
		 * Prepare out test environment to use the a mocked up multipartfile object
		 * for this test only.   NOTE:  "file" is the name of the parameter for 
		 * the request.  
		 */
        MockMultipartFile fstmp = new MockMultipartFile("file", "defaultSpotlight.jpg", MediaType.IMAGE_PNG_VALUE, imgFile.getInputStream());
        
        // everything should work for this test
        mvc.perform(MockMvcRequestBuilders.multipart("/spotlight/img/uploadFile")
                .file(fstmp)
                )         
                .andExpect(status().is3xxRedirection());    // if all good, we redirect to base page
        
        /*
         * Make sure the controller method initialized the spotlight object correctly 
         * when preparing to save to database (update).
         */
        assertEquals(MediaType.IMAGE_PNG_VALUE,spot.getImgType());
        assertEquals(DEFAULT_IMAGE_SIZE, spot.getImgSize().intValue());
        assertNotNull(spot.getImg());
        assertEquals(this.DEFAULT_IMAGE_SIZE,spot.getImg().length);
        
        /*
         * Finally, make sure the dao was told to update with our prepared
         * object.
         */
        Mockito.verify(mockDao).update(spot);   // verify that the dao was told to update
        
		
	}
	
	@Test
	@WithMockUser(username = "admin", password = "admin")
	public void submitTextTest() throws Exception {
		
		SpotLight spot = new SpotLight().setSid(1)
				.setSpotText("old spotlight text");
		
		
		Mockito.when(mockDao.spotLightById(1)).thenReturn(spot);
				
		mvc.perform(post("/spotlight/txt")
				.param("spotTxt", "new text for spotlight"))
			.andExpect(status().is3xxRedirection());    // if all good, we redirect to base page
			;
		
		// ensure that the method changed the spot object preparing
		// for update
		assertEquals("new text for spotlight",spot.getSpotText());
		
		// make sure the dao was asked to fetch the splotlight obj from db
		Mockito.verify(mockDao).spotLightById(1);
		Mockito.verify(mockDao).update(spot);   // verify that the dao was told to update

	}

}
