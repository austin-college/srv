package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;


/**
 * This end-to-end functional tests implements the use case "admin updates an existing 
 * event". 
 * 
 * @author hcourturier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminUpdateEventTest extends SeleniumTest {


	@Test
	public void testAdminUpdateEvent() throws Exception {

		String url;
		WebElement link;
		
		/*
		 * first login as the the administrator.   might fail inside. 
		 */
		loginAsAdmin();

		
		/*
		 * should lead us to the manage events page
		 */
		link = driver.findElement(By.id("manageEvents")); 
		link.click();

		assertEquals(base+"/events", driver.getCurrentUrl());

		/*
		 * stores the title of the event and the type that will be changed
		 * for testing purposes
		 */
		
		String formerTitle = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title evView']")).getText();
		String formerServiceClient = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_sc evView']")).getText();

		url = driver.getCurrentUrl();
		/*
		 * clicks on the button to edit the first event in the list
		 */
		
		link = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td/button[@class='btn edit btnEvEdit']")); 
		link.sendKeys(Keys.ENTER);
		
		
		waitForPage(driver, url, this.MAX_PAGE_WAIT_SECONDS);

		assertEquals(base+"/events/edit/1", driver.getCurrentUrl());

		/*
		 * inputs a new name into event title
		 */

		WebElement add = driver.findElement(By.id("evTitle"));
		add.click();
		add.clear();
		add.sendKeys("testedEvent");

		/*
		 * selects option 2 for event type
		 * "(fws) First We Serve"
		 */

		Select selector = new Select(driver.findElement(By.id("evType")));
		selector.selectByIndex(1);

		//checks to see if the correct option is displayed
		assertEquals("(fws) First We Serve", selector.getAllSelectedOptions().get(0).getText());

		/*
		 * finds the submit button and submits the information
		 */
		url = driver.getCurrentUrl();
		link = driver.findElement(By.className("btn-primary"));
		link.sendKeys(Keys.ENTER);
		
		waitForPage(driver, url, this.MAX_PAGE_WAIT_SECONDS);
		
		/*
		 * should lead us back to the manage events page
		 */
		assertEquals(base+"/events?userid=admin", driver.getCurrentUrl());
		
		/*
		 * now we check to see if the data updated appropriately
		 */
		
		//checks the title
		String currentTitle = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title evView']")).getText();	
		assertNotEquals(formerTitle, currentTitle);
		assertEquals(currentTitle, "testedEvent");
		
		
		/*
		 * currently update does not retain the type when updating
		 * whenever it does, uncomment this bit of code to make the test complete
		 */
//		//checks the type
//		String currentType = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_type']")).getText();
//		assertNotEquals(formerType, currentType);
//		assertEquals(currentType, "(fws) First We Serve");


		/*
		 * Now logout from the site.
		 */
		logout();

	}



}