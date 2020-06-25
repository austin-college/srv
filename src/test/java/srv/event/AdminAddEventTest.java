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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;

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


	@Test
	public void testAdminAddEvent() throws Exception {

		driver.get(base + "/splash");
		
		String oldPageUrl = driver.getCurrentUrl();
		
		WebElement link = driver.findElement(By.linkText("Log In"));
		link.click();

		waitForPage(driver, oldPageUrl, this.MAX_PAGE_WAIT_SECONDS);

		/*
		 * should be at the login page now
		 */
		assertEquals(base+"/login", driver.getCurrentUrl());

		/*
		 * find and populate user text element
		 */
		WebElement txtUser = driver.findElement(By.id("username"));
		txtUser.click();
		txtUser.clear();
		txtUser.sendKeys("admin");


		/*
		 * find and populate password text element
		 */
		WebElement txtPw = driver.findElement(By.id("password"));
		assertNotNull(txtPw);

		txtPw.click();
		txtPw.clear();
		txtPw.sendKeys("admin");


		/*
		 * submit the form
		 */
		WebElement form = driver.findElement(By.className("form-signin"));
		assertNotNull(form);
		form.submit();


		/*
		 * should lead us to the admin's home page.
		 */
		assertEquals(base+"/home/admin?userid=admin", driver.getCurrentUrl());

		/*
		 * from this point on we should be logged in as an admin
		 * -credit to Professor Higgs for the code above
		 */


		/*
		 * should lead us to the manage events page
		 */
		link = driver.findElement(By.id("manageEvents")); 
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

		oldPageUrl = driver.getCurrentUrl();
		
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
		 * 
		 */
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