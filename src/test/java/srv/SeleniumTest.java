package srv;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
	 * On run, this method will click on the button that makes the dialog box visible (based on the given ID for said button)
	 * and will cause the test system to wait until the dialog box is fully visible (based on the given ID for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param clickedItemId
	 * @param dialogId
	 */
	public void clickAndWaitForDialogById(WebDriver driver, int waitTime, String clickedItemId, String dialogId) {

		driver.findElement(By.id(clickedItemId)).click();

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
	 * On run, this method will click on the button that makes the dialog box visible (based on the given xpath for said button)
	 * and will cause the test system to wait until the dialog box is fully visible (based on the given xpath for said Dialog box)
	 * before allowing the code following it to run
	 * 
	 * 
	 * @param driver
	 * @param waitTime
	 * @param clickedItemXpath
	 * @param dialogXpath
	 */
	public void clickAndWaitForDialogByXpath(WebDriver driver, int waitTime, String clickedItemXpath, String dialogXpath) {

		driver.findElement(By.xpath(clickedItemXpath)).click();

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



}