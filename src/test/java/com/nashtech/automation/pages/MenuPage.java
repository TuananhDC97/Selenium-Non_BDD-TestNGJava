package com.nashtech.automation.pages;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.WebDriver;

import com.nashtech.automation.selenium.ExtendedWebDriver;

public class MenuPage {

	private ExtendedWebDriver driver;

	private String tabProfile = "xpath=//span[text()='Profile']";
	private String tabBookStore = "xpath=//span[text()='Book Store']";

	public MenuPage(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = new ExtendedWebDriver(driver);
	}

	public void goToBookStore() throws Exception {
		driver.scrollElementIntoMiddle(tabBookStore);
		driver.click(tabBookStore);
	}
	
	public void goToProfile() throws Exception {
		driver.scrollElementIntoMiddle(tabProfile);
		driver.click(tabProfile);
	}
}
