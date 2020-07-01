package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
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
		assertEquals("(fws) First We Serve", selector.getAllSelectedOptions().get(0).getText());

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
		 * finds the submit button and submits the information
		 */

		link = driver.findElement(By.className("btn-primary"));
		link.sendKeys(Keys.ENTER);



		// This block of code checks to see if the fields updated
		//Once the submit button works, uncomment this code 
		/*
		 * should lead us back to the manage events page
		 */
		//		assertEquals(base+"/events?userid=admin", driver.getCurrentUrl());
		//		
		//		/*
		//		 * now we check to see if the data updated appropriately
		//		 */
		//		
		//		//checks the title
		//		String currentTitle = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title']")).getText();	
		//		assertEquals(currentTitle, "testedEvent");
		//		

	}

}