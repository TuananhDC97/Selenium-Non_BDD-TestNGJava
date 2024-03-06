package com.nashtech.automation.pages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nashtech.automation.selenium.ExtendedWebDriver;
import com.nashtech.automation.setup.EnvironmentVariables;

public class ProfilePage{
	
	private ExtendedWebDriver driver;
	
	private By btnLogin = By.className("ico-login");
	private String lbBooks = "xpath=//a[contains(@href,'/profile?book=')]";
	
	public ProfilePage(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = new ExtendedWebDriver(driver);
	}
	
	public boolean isOpened() throws Exception {
		return driver.isUrlToBe(EnvironmentVariables.BASE_URL + "/profile", 5);
	}
	
	public void clickLogin() throws Exception {
		driver.click(btnLogin);
	}
	
	public List<String> getBooksName() throws Exception {
		return driver.getTexts(lbBooks);
	}
	
	
}
