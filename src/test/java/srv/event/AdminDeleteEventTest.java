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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;
import srv.domain.user.JdbcTemplateServantUserDao;


/**
 * This end-to-end functional tests implements the use case "admin deletes an existing 
 * event". 
 * 
 * @author hcouturier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminDeleteEventTest extends SeleniumTest {
	
	@Autowired
	JdbcTemplateServantUserDao servantDao;


	@Test
	public void testAdminDeleteEvent() throws Exception {

		loginAsAdmin();

		/*
		 * should lead us to the manage events page
		 */
		WebElement link = driver.findElement(By.id("manageEvents")); 
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

		WaitForDialogByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/span[@id='ui-id-1']");

		/*
		 * clicks on the confirm delete button
		 */

		link = driver.findElement(By.xpath("//div/div/div/button[@class='delBtnClass']"));
		link.click();
		
		WaitForDialogToCloseByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/div/div/button[@class='delBtnClass']");
		
//		Thread.sleep(2000);
		
		//checks to see if the element is no longer visible
		assertTrue(driver.findElements(By.xpath("//table/tbody/tr[@id='eid-1']/td[@class='ev_title evView']")).size() == 0);

		logout();

	}

}