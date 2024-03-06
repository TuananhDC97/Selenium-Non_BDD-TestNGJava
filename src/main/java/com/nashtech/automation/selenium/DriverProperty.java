package com.nashtech.automation.selenium;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.nashtech.automation.utility.Common.*;
import com.nashtech.automation.config.ConfigLoader;
import com.nashtech.automation.enumobj.EnumObjects.Browser;
import com.nashtech.automation.enumobj.EnumObjects.DriverType;
import com.nashtech.automation.utility.Constant;
import com.nashtech.automation.utility.JsonParser;

public class DriverProperty {

	private DriverType driverType;

	// information for local/azurelocal browser
	private Browser browser;
	private String browserSize;
	private int implicitWait;

	// for remote driver
	private String remoteUrl;
	private String browserVersion;
	private String platformName;
	private String platformVersion;
	private String screenResolution;
	private String sectionName;

	// other capabilities
	private DesiredCapabilities capabilities;
	private List<String> arguments;

	public DriverProperty(String driverType, String browser, String browserSize, String browserVersion,
			String platformName, String platformVersion, String sectionName) throws FileNotFoundException, IOException {

		setDriverType(driverType);
		setBrowser(browser);
		setBrowserSize(browserSize);
		setImplicitWait();
		this.sectionName = sectionName;

		String capabilities = ConfigLoader.getConfig(this.driverType.toString().toLowerCase() + ".capabilities");
		JSONObject capabilitiesJs = new JSONObject();
		if (!isNullOrBlank(capabilities)) {
			capabilitiesJs = new JSONObject(capabilities);
		}

		switch (this.driverType) {
		case LOCAL:
			break;
		case BROWSERSTACK:
			if (!isNullOrBlank(browserVersion)) {
				capabilitiesJs.put("browser_version", browserVersion);
			}
			if (!isNullOrBlank(platformName)) {
				capabilitiesJs.put("os", platformName);
			}
			if (!isNullOrBlank(platformVersion)) {
				capabilitiesJs.put("os_version", platformVersion);
			}
			if (!isNullOrBlank(screenResolution)) {
				capabilitiesJs.put("resolution", screenResolution);
			}
			break;
		case REMOTE:
			if (!isNullOrBlank(platformName)) {
				capabilitiesJs.put("platformName", platformName);
			}
			if (!isNullOrBlank(platformVersion)) {
				capabilitiesJs.put("platformName", platformVersion);
			}
		default:
			break;
		}
		setCapabilities(capabilitiesJs.toString());
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

	public DriverType getDriverType() {
		return driverType;
	}

	public int getImplicitWait() {
		return implicitWait;
	}

	public void setImplicitWait() throws FileNotFoundException, IOException {
		String config = ConfigLoader.getConfig("implicitWait");
		if (isNullOrBlank(config)) {
			this.implicitWait = Constant.DEFAULT_IMPLICIT_WAIT;
		} else {
			this.implicitWait = Integer.valueOf(config);
		}
	}

	public void setDriverType(String driverType) throws FileNotFoundException, IOException {
		if (!isNullOrBlank(driverType)) {
			this.driverType = DriverType.valueOf(driverType.toUpperCase());
		} else {
			String type = ConfigLoader.getConfig("driverType").toUpperCase();
			if (isNullOrBlank(type)) {
				this.driverType = Constant.DEFAULT_DRIVER_TYPE;
			} else {
				this.driverType = DriverType.valueOf(ConfigLoader.getConfig("driverType").toUpperCase());
			}
		}
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) throws FileNotFoundException, IOException {
		if (!isNullOrBlank(browser)) {
			this.browser = Browser.valueOf(browser.toUpperCase());
		} else {
			String configBrowser = ConfigLoader.getConfig(driverType + ".browser");
			if (isNullOrBlank(configBrowser)) {
				this.browser = Constant.DEFAULT_BROWSER;
			} else {
				this.browser = Browser.valueOf(ConfigLoader.getConfig(driverType + ".browser"));
			}
		}
	}

	public String getBrowserSize() {
		return browserSize;
	}

	public void setBrowserSize(String browserSize) throws FileNotFoundException, IOException {
		if (!isNullOrBlank(browserSize)) {
			this.browserSize = browserSize;
		} else {
			String configBrowser = ConfigLoader.getConfig(driverType + ".browserSize");
			if (isNullOrBlank(configBrowser)) {
				this.browserSize = Constant.DEFAULT_BROWSER_SIZE;
			} else {
				this.browserSize = configBrowser;
			}
		}
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public DesiredCapabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = JsonParser.convertJsonToCapabilities(capabilities);
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) throws MalformedURLException {
		this.remoteUrl = remoteUrl;
	}

	/**
	 * @return the arguments
	 */
	public List<String> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(String arguments) {
		this.arguments = JsonParser.convertJsonToArguments(arguments);
	}

	public String getPlatform() {
		return platformName;
	}

	public void setPlatform(String platformName) {
		this.platformName = platformName;
	}
}
