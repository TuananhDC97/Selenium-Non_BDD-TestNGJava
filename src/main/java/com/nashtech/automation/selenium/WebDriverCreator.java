package com.nashtech.automation.selenium;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.nashtech.automation.config.ConfigLoader;
import com.nashtech.automation.enumobj.EnumObjects.Browser;
import com.nashtech.automation.log.Log;
import com.nashtech.automation.utility.Common;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverCreator {

	public static WebDriver startDriver(DriverProperty driverProperty) throws Exception {
		switch (driverProperty.getDriverType()) {
		case LOCAL:
			return createLocalDriver(driverProperty);
		case BROWSERSTACK:
			return createBrowserStackDriver(driverProperty);
		default:
			return createLocalDriver(driverProperty);
		}

	}

	/**
	 * This method is used to open a webdriver, it's used for selenium grid as well
	 * 
	 * @author Hanoi Automation team
	 * @param None
	 * @return None
	 * @throws Exception
	 *             The method throws an exeption when browser is invalid or can't
	 *             start webdriver
	 */
	private static WebDriver createLocalDriver(DriverProperty driverProperty) throws Exception {
		WebDriver _driver = null;
		MutableCapabilities options = createDriverOption(driverProperty.getBrowser());
		try {
			switch (driverProperty.getBrowser()) {
			case FIREFOX:
				WebDriverManager.firefoxdriver().setup();
				FirefoxOptions op = (FirefoxOptions) options;
				_driver = new FirefoxDriver(op);
				break;
			case CHROME:
				WebDriverManager.chromedriver().setup();
				ChromeOptions op1 = (ChromeOptions) options;
				_driver = new ChromeDriver(op1);
				break;
			case SAFARI:
				WebDriverManager.safaridriver().setup();
				_driver = new SafariDriver();
				break;
			case EDGE:
				WebDriverManager.edgedriver().setup();
				EdgeOptions op2 = (EdgeOptions) options;
				_driver = new EdgeDriver(op2);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.error(String.format("Cannot start webdriver for: [%s] \n%s", driverProperty.getBrowser(),
					e.getMessage()));
			throw (e);
		}
		setBrowserSize(_driver, driverProperty.getBrowserSize());
		_driver.manage().timeouts().implicitlyWait(driverProperty.getImplicitWait(), TimeUnit.SECONDS);
		_driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		_driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		return _driver;
	}

	// still on update
	private static WebDriver createBrowserStackDriver(DriverProperty driverProperty) throws Exception {

		String username = ConfigLoader.getConfig("browserstack.username");
		String accessKey = ConfigLoader.getConfig("browserstack.accessKey");
		String remoteUrl = ConfigLoader.getConfig("browerstack.remoteUrl");
		String strBrowserStackServer = String.format(remoteUrl, username, accessKey);

		WebDriver _driver = null;

		try {

			DesiredCapabilities options = driverProperty.getCapabilities();
			options.setCapability("browser", driverProperty.getBrowser());

			_driver = new RemoteWebDriver(new URL(strBrowserStackServer), options);
			Log.info(String.format("Starting remote webdriver for: [%s]", driverProperty.getBrowser()));
			Log.info("Capabilities: " + options.toString());
		} catch (Exception e) {
			Log.error(String.format("Cannot start remove webdriver for: [%s]", driverProperty.getBrowser()));
			Log.error("Capabilities: " + driverProperty.getCapabilities().toString());
			throw (e);
		}
		setBrowserSize(_driver, driverProperty.getBrowserSize());
		_driver.manage().timeouts().implicitlyWait(driverProperty.getImplicitWait(), TimeUnit.SECONDS);
		((RemoteWebDriver) _driver).setFileDetector(new LocalFileDetector());
		return _driver;
	}

	private static MutableCapabilities createDriverOption(Browser driverType) {

		switch (driverType) {
		case FIREFOX:
			return getFirefoxOption();
		case CHROME:
			return getChromeOptions();
		case SAFARI:
			SafariOptions safariOptions = new SafariOptions();
			return safariOptions;
		case EDGE:
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setPageLoadStrategy("eager");
			return edgeOptions;
		}
		return null;
	}
	
	private static MutableCapabilities getChromeOptions() {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.prompt_for_download", "false");
		chromePrefs.put("safebrowsing.enabled", "true");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		chromeOptions.addArguments("safebrowsing-disable-download-protection");
		chromeOptions.addArguments("disable-geolocation");
		// AGRESSIVE: options.setPageLoadStrategy(PageLoadStrategy.NONE); //
		// https://www.skptricks.com/2018/08/timed-out-receiving-message-from-renderer-selenium.html
		chromeOptions.addArguments("start-maximized"); // https://stackoverflow.com/a/26283818/1689770
		chromeOptions.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
		// options.addArguments("--headless"); // only if you are ACTUALLY running
		// headless
		chromeOptions.addArguments("--no-sandbox"); // https://stackoverflow.com/a/50725918/1689770
		chromeOptions.addArguments("--disable-infobars"); // https://stackoverflow.com/a/43840128/1689770
		chromeOptions.addArguments("--disable-dev-shm-usage"); // https://stackoverflow.com/a/50725918/1689770
		chromeOptions.addArguments("--disable-browser-side-navigation"); // https://stackoverflow.com/a/49123152/1689770
		chromeOptions.addArguments("--disable-gpu"); // https://stackoverflow.com/questions/51959986/how-to-solve-selenium-chromedriver-timed-out-receiving-message-from-renderer-exc

		chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		return chromeOptions;
	}
	
	private static MutableCapabilities getFirefoxOption() {
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.addPreference("security.insecure_password.ui.enabled", false);
		firefoxOptions.addPreference("security.insecure_field_warning.contextual.enabled", false);
		firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		return firefoxOptions;
	}

	public static void setBrowserSize(WebDriver driver, String browerSize) {
		if (Common.isNullOrBlank(browerSize) || browerSize.equalsIgnoreCase("maximum")) {
			driver.manage().window().maximize();
		} else {
			String[] size = browerSize.split("x");
			int width = Integer.valueOf(size[0]);
			int height = Integer.valueOf(size[1]);
			driver.manage().window().setSize(new Dimension(width, height));
		}
	}

}