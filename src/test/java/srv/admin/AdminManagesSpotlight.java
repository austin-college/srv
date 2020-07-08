package srv.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.SendKeysAction;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;
import srv.domain.user.JdbcTemplateServantUserDao;

/**
 * This end-to-end functional tests implements the use case "admin selects
 * a new spotlight". 
 * 
 * @author hcouturier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminManagesSpotlight extends SeleniumTest {

	@Autowired
	JdbcTemplateServantUserDao servantDao;


	@Test
	public void testAdminManagesSpotlight() throws Exception {
		//logs in as the user
		loginAs("admin", "admin", "/home/admin?userid=admin");

		//makes sure we are on the admin's home page
		assertEquals(base+"/home/admin?userid=admin", driver.getCurrentUrl());
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,250)", "");
		
		//clicks on the manage spotlight button
		WebElement link = driver.findElement(By.xpath("//div/a[@href='/srv/spotlight']")); 
		link.click();
		
		//makes sure we are on the Manage Spotlight page
		assertEquals(base+"/spotlight", driver.getCurrentUrl());
		
		//selects browse button for adding a new image and submits an image
		link = driver.findElement(By.xpath("//div/input"));
		//TODO make this work for everyone, right now its just me
		link.sendKeys("C:\\Users\\hunte\\OneDrive\\Desktop\\CS 380 Workspace\\srv\\src\\main\\resources\\static\\images\\defaultProfilePicture.png");
		
		jse.executeScript("window.scrollBy(0,250)", "");
		
		//submits the image
		link = driver.findElement(By.xpath("//form[@action='/srv/spotlight/img/uploadFile']/button"));
		link.click();
		
		//clicks on the edit text, text area and writes text
		link = driver.findElement(By.xpath("//div[@id='editor']/div"));
		link.clear();
		link.sendKeys("TEST OF THE SYSTEM");
		
		//clicks on the submit button for text
		link = driver.findElement(By.xpath("//form[@id='frmText']/button"));
		link.click();
		
		//logs out of the system to return to splash page
		logout();
		
		//checks that the image and text have been changed
		
		//text
		assertEquals("TEST OF THE SYSTEM", driver.findElement(By.xpath("//div[@id='spotlightDesc']/p")).getText());
		
		//image
		//TODO figure out how to make sure the image was uploaded
		assertEquals("","");
		
		
		
		
		
	}

}