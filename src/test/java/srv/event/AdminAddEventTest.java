package srv.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void testAdminAddEvent() throws Exception {

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
		
		Thread.sleep(2000);
		
		//this checks the dialog box to see if its visible
		assertEquals(true, link.findElement(By.xpath("//div/span[@id='ui-id-2']")).isDisplayed());

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
		
		
		/*
		 * finds the add new button and clicks it
		 */
		link = driver.findElement(By.className("newBtnClass"));
		link.click();
		
		Thread.sleep(2000);
		

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
		
	}

}