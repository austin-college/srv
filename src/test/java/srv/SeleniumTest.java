package srv;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SeleniumTest {
	
	public static final String DRIVERPATH = "src/main/resources/chromedriver";
	
    @LocalServerPort
    protected int port;
    
    protected String base;
    
    
    protected static ChromeDriverService service;
    
    protected WebDriver driver;

    
    @Before
    public void setUp() throws Exception {
    	
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
        //options.addArguments("--headless");
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        
    	
        driver = new ChromeDriver(service,options);
        
        this.base = "http://localhost:" + port+"/srv";
        System.err.println(base);
    }
    


    @BeforeClass
    public static void createAndStartService() throws Exception {
      
      System.setProperty("webdriver.chrome.driver", DRIVERPATH);
      
      service = new ChromeDriverService.Builder()
          .usingDriverExecutable(new File(DRIVERPATH))
          .usingAnyFreePort()
          .build();
      
      
      service.start();
    }

    @AfterClass
    public static void stopService() {
      service.stop();
    }


    @After
    public void quitDriver() {
      driver.quit();
    }
    
    

}