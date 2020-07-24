package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;
import srv.domain.user.JdbcTemplateServantUserDao;

/**
 * This end-to-end functional tests implements the use case "admin adds a new
 * event". 
 * 
 * @author hcouturier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminAddEventTest extends SeleniumTest {
	
	@Autowired
	JdbcTemplateServantUserDao servantDao;


	@Test
	public void testAdminAddEvent() throws Exception {

		loginAsAdmin();


		JavascriptExecutor jse = (JavascriptExecutor)driver;
		
		/*
		 * should lead us to the manage events page
		 */
		WebElement link = driver.findElement(By.id("manageEvents")); 
		link.click();

		assertEquals(base+"/events", driver.getCurrentUrl());

		/*
		 * opens up the creation of a new event
		 */

		link = driver.findElement(By.id("btnEvNew")); 
		link.click();

		WaitForDialogByXpath(driver, this.MAX_PAGE_WAIT_SECONDS, "//div/span[@id='ui-id-2']");

		/*
		 * clicks on the combo box to open a list of options
		 */
		link = driver.findElement(By.id("evType"));
		link.click();

		/*
		 * selects option 2
		 * "(fws) First We Serve"
		 */
		Select selector = new Select(driver.findElement(By.id("evType")));
		selector.selectByIndex(1);

		//checks to see if the correct option is displayed
		assertEquals("(fws) First We Serve", selector.getAllSelectedOptions().get(0).getText().trim());

		String oldPageUrl = driver.getCurrentUrl();
		
		/*
		 * finds the add new button and clicks it
		 */
		link = driver.findElement(By.className("newBtnClass"));
		link.click();

		waitForPage(driver, oldPageUrl, this.MAX_PAGE_WAIT_SECONDS);


		/*
		 * should lead us to the edit event page
		 */
		assertEquals(base+"/events/edit/6", driver.getCurrentUrl());

		/*
		 * inputs a name into event title
		 */
		WebElement addName = driver.findElement(By.id("evTitle"));
		addName.click();
		addName.clear();
		addName.sendKeys("testedEvent");

		//tests if the correct text is displayed
		assertEquals("testedEvent", addName.getAttribute("value") );
		
		/*
		 * inputs location into event address
		 */
		WebElement eventAddress = driver.findElement(By.id("evAddress"));
		eventAddress.click();
		eventAddress.clear();
		eventAddress.sendKeys("testedLocation");
		
		// asserts that correct txt displayed
		assertEquals("testedLocation", eventAddress.getAttribute("value"));
		
		/*
		 * inputs date  in date field
		 */
		//WebElement date = driver.findElement(By.id("evDate"));
		//date.click();
		//date.clear();
		//Thread.sleep(2000);
		//date.sendKeys("2020/06/26 00:00");
		
		// finish out test w current date 
		//assertEquals("2020/06/26 00:00", date.getAttribute("value"));
		/*
		 * finds the submit button and submits the information
		 */
		
		/*
		 *  inputs the number of volunteers 
		 */
		WebElement eventVN = driver.findElement(By.id("evVN"));
		//eventVN.click();
		eventVN.clear();
		//Thread.sleep(1000);
		eventVN.sendKeys("5");
		
		
		assertEquals("5",eventVN.getAttribute("value"));

		/*
		 * inputs total volunteer hours needed
		 */
		WebElement evNVH = driver.findElement(By.id("evNVH"));
		evNVH.clear();
		evNVH.sendKeys("8");
		
		assertEquals("8", evNVH.getAttribute("value"));
		
		/*
		 * inputs total pledged hours
		 */
		WebElement evRsvp = driver.findElement(By.id("evRsvp"));
		evRsvp.clear();
		evRsvp.sendKeys("4");
		
		assertEquals("4", evRsvp.getAttribute("value"));
		
		/*
		 * inputs event note
		 */
		WebElement evNote = driver.findElement(By.id("evNote aria-describedby="));
		evNote.clear();
		evNote.sendKeys("test note field");
		
		assertEquals("test note field", evNote.getAttribute("value"));
		
		/*
		 * clicks on "Ongoing/Continuous Event" checkbox
		 */
		WebElement evContinuous = driver.findElement(By.id("evContinuous"));
		evContinuous.click();
		
		assertTrue(evContinuous.isSelected());
		
		// leaving the contact untested until implemented
		
		/*
		 * find and click on event sponsor combo box
		 */
		//link = driver.findElement(By.id("serviceClient"));
		//link.sendKeys(Keys.ENTER);
		
		// DEBUG THIS BC SRVCLIENT is not showing up once selected
		/*
		 * selects 'For Testing Only' from selector for Event Sponsor
		 */
		/*
		 * Select evSponsor = new Select(driver.findElement(By.id("serviceClient")));
		 * evSponsor.selectByIndex(3);
		 * 
		 * assertEquals("For Testing Only",
		 * evSponsor.getAllSelectedOptions().get(0).getText().trim());
		 * 
		 */
		 link = driver.findElement(By.id("btnEvEdit"));
		 System.err.println(link.toString());
		 System.err.println(link.getLocation());
		 jse.executeScript("window.scrollBy" + link.getLocation(), "");
		 link.sendKeys(Keys.ENTER);
		 



		// This block of code checks to see if the fields updated
		//Once the submit button works, uncomment this code 
		
		 // should lead us back to the manage events page
		 
			
			assertEquals(base+"/events?userid=admin", driver.getCurrentUrl());
				
			/*
			 * now we check to see if the data updated appropriately
			 */
				
				//checks the title
			String currentTitle = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-6']/td[@class='ev_title evView']")).getText();		
			assertEquals("testedEvent", currentTitle);
			
			/*
			 * click on currentTitle in table so event details dialogue pops up
			 */
			
			
			
			/*
			 * find search button on edge of manage events table 
			 */
			link = driver.findElement(By.xpath("//td/button[@class='btn btnEvView'][@eid='6']"));
			
			// scroll to the button 
			jse.executeScript("window.scrollBy" + link.getLocation(), "");
			System.err.println(link.isDisplayed());
		
			System.err.println(link.getLocation());
			
			/*
			 * click the button and wait for eventDetails dialog
			 */
			link.sendKeys(Keys.ENTER);
			
			// assures us that the eventDetails dialogue has popped up
			// make sure xpath is specific enough (use /label) IF click intercepted exception comes up 
			WaitForDialogByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/h3/label[@for='volunteersNeeded']");
			
			/*
			 * checking location field displays correct data
			 */
			
			WebElement location = driver.findElement(By.xpath("//div/div/input[@id='location']"));
			System.err.println(location.getText());
			System.err.println(location.getAttribute("placeholder"));
			assertEquals("testedLocation", location.getAttribute("placeholder"));
			
			location = driver.findElement(By.xpath("//div/div/input[@id='sponsor']"));
			assertEquals("(fws) First We Serve", location.getAttribute("placeholder"));
			
			location = driver.findElement(By.xpath("//div/div/input[@id='volunteersNeeded']"));
			assertEquals("5", location.getAttribute("placeholder"));
			
			location = driver.findElement(By.xpath("//div/div/input[@id='hrsNeeded']"));
			assertEquals("8", location.getAttribute("placeholder"));
			
			location = driver.findElement(By.xpath("//div/div/input[@id='rsvpHrs']"));
			assertEquals("4", location.getAttribute("placeholder"));
			
			//location = driver.findElement(By.xpath("//div/div/input[@id='srvClient']"));
			//assertEquals("For Testing Only", location.getAttribute("placeholder"));
			
			/*
			 * click on close out button 
			 */
			
			link = driver.findElement(By.xpath("//div/div/div/button[@class='cancBtnClass'][@id='cancel']"));
			//jse.executeScript("window.scrollBy" + link.getLocation(),"");
			link.click();
			System.err.println(link.getLocation());
			
			
			
	//		WaitForDialogToCloseByXpath(driver, MAX_DIALOG_WAIT_SECONDS,"//div/div/button[@class='ui-widget-overlay ui-front']");
			
			logout();
			
			
			

	}

}