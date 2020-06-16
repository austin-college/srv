package srv.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import srv.SeleniumTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SplashTest extends SeleniumTest {
	
	
	/**
	 * Test to ensure that the incomplete "/" endpoint redirects to
	 * a more proper "/splash" ....which is really our site landing URL.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testRedirectToSplash() throws Exception {
        driver.get(base + "/");
        
        assertEquals("Welcome",driver.getTitle());
        assertEquals(base+"/splash", driver.getCurrentUrl());
    }
    
    
    /**
     * Test to ensure that authenticating as an admin really does
     * navigate us to the admin's home page.
     * 
     * @throws Exception
     */
    @Test
    public void testAdminLogin() throws Exception {
    	
        driver.get(base + "/splash");

        String oldPageURL = driver.getCurrentUrl();
        WebElement link = driver.findElement(By.linkText("Log In"));
        link.click();
        
        waitForPage(driver, oldPageURL, 4);
         
        
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
        
        
        
    }
    
}