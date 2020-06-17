package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminDeleteEventTest extends SeleniumTest {


	@Test
	public void testAdminDeleteEvent() throws Exception {

		int waitTime = 2;
		String oldPageUrl = driver.getCurrentUrl();
		driver.get(base + "/splash");

		WebElement link = driver.findElement(By.linkText("Log In"));
		link.click();

		waitForPage(driver, oldPageUrl, waitTime);

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
		 * stores the title of the event that will be deleted
		 * for testing purposes
		 */

		String formerTitle = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title evView']")).getText();	

		/*
		 * clicks on the button to delete the first event in the list
		 */

		link = driver.findElement(By.xpath("//table/tbody/tr[@id='eid-1']/td/button[@class='btn edit btnEvDel']")); 
		link.sendKeys(Keys.ENTER);

		WaitForDialogByXpath(driver, waitTime, "//div/span[@id='ui-id-1']");

		/*
		 * clicks on the confirm delete button
		 */

		link = driver.findElement(By.xpath("//div/div/div/button[@class='delBtnClass']"));
		link.click();
		
		Thread.sleep(2000);
		
		//checks to see if the element is no longer visible
		assertTrue(driver.findElements(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title evView']")).size() == 0);

		/*
		 * clicks on the log out button
		 */
		link = driver.findElement(By.xpath("//div/a[@href='/srv/logout']"));
		link.click();
		
		 assertEquals(base+"/splash", driver.getCurrentUrl());

	}

}