package srv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SeleniumTest {

	protected static final int MAX_PAGE_WAIT_SECONDS = 4;
	protected static final int MAX_DIALOG_WAIT_SECONDS = 4;
	
	public static final String WINDRIVERPATH = "src/main/resources/chromedriver.exe";
	public static final String MACDRIVERPATH = "src/main/resources/chromedriver";

	@LocalServerPort
	protected int port;

	protected String base;


	protected static ChromeDriverService service;

	protected WebDriver driver;


	@Before
	public void setUp() throws Exception {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
		options.addArguments("--headless");
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
		String path;
		System.err.println(System.getProperty("os.name"));
		if(System.getProperty("os.name").toLowerCase().contains("win")) {
			path = WINDRIVERPATH;

		} else {
			path = MACDRIVERPATH;

		}
		System.setProperty("webdriver.chrome.driver", path);



		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(path))
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

	/**
	 * On run, this method cause the test system to wait until the dialog box is fully visible (based on the given ID for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogId
	 */
	public void WaitForDialogById(WebDriver driver, int waitTime, String dialogId) {

		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				Boolean isPresent;
				isPresent = driver.findElement(By.id(dialogId)).isDisplayed();

				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method will cause the test system to wait until the dialog box is fully visible (based on the given xpath for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogXpath
	 */
	public void WaitForDialogByXpath(WebDriver driver, int waitTime, String dialogXpath) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				Boolean isPresent;
				isPresent = driver.findElement(By.xpath(dialogXpath)).isDisplayed();
				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method will cause the test system to wait until the dialog box is closed (based on the given xpath for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogXpath
	 */
	public void WaitForDialogToCloseByXpath(WebDriver driver, int waitTime, String dialogXpath) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				Boolean isPresent = null;
				if(driver.findElement(By.xpath(dialogXpath)).isDisplayed() == true) {
					isPresent = false;
				} else {
					isPresent = true;
				}
				return isPresent;
			}
		}
				);

	}
	
	/**
	 * On run, this method cause the test system to wait until the dialog box is fully closed (based on the given ID for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param dialogId
	 */
	public void WaitForDialogToCloseById(WebDriver driver, int waitTime, String dialogId) {


		WebDriverWait wait = new WebDriverWait(driver, waitTime);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				Boolean isPresent;
				if(driver.findElement(By.id(dialogId)).isDisplayed() == true) {
					isPresent = false;
				} else {
					isPresent = true;
				}
				
				return isPresent;
			}
		}
				);

	}

	
	/**
	 * Waits for a page to change;
	 * 
	 * @param driver
	 * @param oldPageUrl the URL of old page
	 * @param waitTime max time to wait in seconds
	 */
	protected void waitForPage(WebDriver driver, String oldPageUrl, int waitTimeOutSecs) {

		WebDriverWait wait = new WebDriverWait(driver, waitTimeOutSecs);

		wait.until(new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {

				boolean same = driver.getCurrentUrl().equals(oldPageUrl);
				return !same;

			}
		});

	}

	/**
	 * Convenient wrapper function for logging in as admin user.
	 */
	protected void loginAsAdmin() {
		loginAs("admin","admin","/home/admin?userid=admin");
	}

	/**
	 * Convenient wrapper function for logging in as boardmember.
	 */
	protected void loginAsBoardmember() {
		loginAs("boardmember","boardmember","/home/boardmember?userid=boardmember");
	}

	/**
	 * Convenient wrapper function for logging in as servant user.
	 */
	protected void loginAsUser() {
		loginAs("user","user","/home/user");
	}
	

	/**
	 * Performs authentication on our site starting at the site splash
	 * page.  Given the userid and password and 
	 * @param userid
	 * @param password
	 * @param home
	 */
	protected void loginAs(String userid, String password, String homeURL) {
		
		driver.get(base + "/splash");
	
		String url = driver.getCurrentUrl();
		
		WebElement link = driver.findElement(By.linkText("Log In"));
		link.click();
	
		waitForPage(driver, url, MAX_PAGE_WAIT_SECONDS);
	
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
		
		txtUser.sendKeys(userid);
	
	
		/*
		 * find and populate password text element
		 */
		WebElement txtPw = driver.findElement(By.id("password"));
		assertNotNull(txtPw);
	
		txtPw.click();
		txtPw.clear();
		txtPw.sendKeys(password);
	
	
		/*
		 * submit the form
		 */
		WebElement form = driver.findElement(By.className("form-signin"));
		assertNotNull(form);
		form.submit();
	
	
		/*
		 * should lead us to the admin's home page.
		 */
		assertEquals(base+homeURL, driver.getCurrentUrl());
	
	}


	/**
	 * Logs user out of site.  Assumes the current page has a navigation bar
	 * with a logout link.   Also assumes logout leads us back to the public 
	 * splash page.
	 * 	 
	 */
	protected void logout() {

		/*
		 * locaate clicks on the log out button
		 */
		WebElement link = driver.findElement(By.xpath("//div/a[@href='/srv/logout']"));
		
		String url = driver.getCurrentUrl();   // before we click...lets save the page
		
		link.click();
		
		waitForPage(driver, url, MAX_PAGE_WAIT_SECONDS);

		assertEquals(base+"/splash", driver.getCurrentUrl());
	}

}