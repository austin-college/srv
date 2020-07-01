package srv.servant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
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
 * This end-to-end functional tests implements the use case "admin adds a new
 * event". 
 * 
 * @author hcouturier
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServantAddsHours extends SeleniumTest {

	@Autowired
	JdbcTemplateServantUserDao servantDao;


	@Test
	public void testAdminAddEvent() throws Exception {
		//logs in as the user
		loginAs("user", "user", "/home/servant?userid=user");

		//makes sure we are on the user page
		assertEquals(base+"/home/servant?userid=user", driver.getCurrentUrl());

		WebElement link = driver.findElement(By.xpath("//div/a[@href='/srv/hours']"));
		link.sendKeys(Keys.ENTER);

		//makes sure we are on the enter hours page
		assertEquals(base+"/hours", driver.getCurrentUrl());

		//clicks on the add hours button
		link = driver.findElement(By.xpath("//div/button[@class='btn btn-md btn-dark addBtn']"));
		link.sendKeys(Keys.ENTER);

		//this running confirms that the dialog box was clicked and is visible
		WaitForDialogByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/div[@class='ui-dialog-content ui-widget-content']");

		//clicks on the check mark for the first item
		link = driver.findElement(By.xpath("//td[@class='ev_sel sorting_1']/input[@class='boxSel']"));
		link.click();
		
		//makes sure that the box was selected
		assertEquals(true, link.isSelected());

		//clicks on the submit button which opens a new Dialog
		link = driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']/button[@id='btnEvSelDlgSubmit']"));		
		assertEquals(true,link.isEnabled());
		link.click();

		//this running confirms that the dialog box was clicked and is visible
		WaitForDialogByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/span[@id='ui-id-4']");

		//enters hours served
		link = driver.findElement(By.xpath("//div/input[@id='hrsSrvd']"));
		link.clear();
		link.sendKeys("24");

		//enters a description
		link = driver.findElement(By.xpath("//div/textarea[@id='description']"));
		link.clear();
		link.sendKeys("A generic description");

		//enters a reflection
		link = driver.findElement(By.xpath("//div/textarea[@id='reflection']"));
		link.clear();
		link.sendKeys("A generic reflection");

		//clicks on the submit button
		link = driver.findElement(By.xpath("//div/button[@id='btnSubmitFeedBackDlg']"));
		link.sendKeys(Keys.ENTER);

		//confirms all the information has been entered
		link = driver.findElement(By.xpath("//tr[@id='row8']/td[@name='hrs_eventName']"));

		assertEquals("GDS2020", link.getText());

		//confirms all the information has been entered
		link = driver.findElement(By.xpath("//tr[@id='row8']/td[@name='hrs_hrsServed']"));

		assertEquals("24", link.getText());
		
		logout();




	}

}