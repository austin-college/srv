package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminAddEventTest extends SeleniumTest {

	@Test
	public void testRedirectToSplash() throws Exception {
		driver.get(base + "/");

		assertEquals("Welcome",driver.getTitle());
		assertEquals(base+"/splash", driver.getCurrentUrl());
	}


	private void clickAndWaitForPage(WebDriver driver, By by, int waitTime) {

		final String currentUrl = driver.getCurrentUrl();
		driver.findElement(by).click();

		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return (!d.getCurrentUrl().equals(currentUrl));
			}
		});

	}



	@Test
	public void testAdminLogin() throws Exception {

		driver.get(base + "/splash");

		WebElement link = driver.findElement(By.linkText("Log In"));
		link.click();

		//clickAndWaitForPage(driver, By.id("nav-login"), 4);
		Thread.sleep(2000);

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


		// Thread.sleep(000);

		/*
		 * find and populate password text element
		 */
		WebElement txtPw = driver.findElement(By.id("password"));
		assertNotNull(txtPw);

		txtPw.click();
		txtPw.clear();
		txtPw.sendKeys("admin");

		//Thread.sleep(5000);

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
		 * should lead us to the manage service clients page
		 */
		link = driver.findElement(By.id("manageClients")); 
		link.click();

		assertEquals(base+"/sc/list", driver.getCurrentUrl());

	}

}