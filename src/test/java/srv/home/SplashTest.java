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
    	
        this.loginAsAdmin();

        
    }
    
}