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

		//clicks on the checkmark for the first item
		link = driver.findElement(By.xpath("//td/input[@class='boxSel']"));
		link.sendKeys(Keys.ENTER);

		//TODO test to make sure the item was properly selected
		
		//clicks on the submit button which opens a new Dialog
		link = driver.findElement(By.xpath("//div/button[@id='addBtnDlg']"));
		link.sendKeys(Keys.ENTER);
		
//		//this running confirms that the dialog box was clicked and is visible
//		WaitForDialogByXpath(driver, MAX_DIALOG_WAIT_SECONDS, "//div/span[@id='ui-id-4']");





	}

}