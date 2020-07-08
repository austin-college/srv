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
		
		//clicks on the manage spotlight button
		WebElement link = driver.findElement(By.xpath("//div/a[@href='/srv/spotlight']")); 
		link.click();
	}

}