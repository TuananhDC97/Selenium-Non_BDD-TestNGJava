package com.nashtech.automation.selenium;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

public class WebDriverManager_ {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	public static void initDriver(String driverType, String browser, String browserSize, String browserVersion,
			String platformName, String platformVersion, String sectionName) throws FileNotFoundException, IOException {
		if (getDriver() == null) {
			try {
				DriverProperty driverProperty = new DriverProperty(driverType, browser, browserSize, browserVersion,
						platformName, platformVersion, sectionName);
				WebDriver driver_ = WebDriverCreator.startDriver(driverProperty);
				driver.set(driver_);
			} catch (Exception exception) {
				exception.printStackTrace();
				throw new SkipException("Cannot initiator driver!", exception);
			}
		}else {
			System.out.println("not nulllll");
		}
	}

	public static WebDriver getDriver() {
		return driver.get();
	}
	
	public static void closeDriver() {
		if (driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}
}
