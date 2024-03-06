package com.nashtech.automation.pages;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nashtech.automation.selenium.ExtendedWebDriver;
import com.nashtech.automation.setup.EnvironmentVariables;

public class LoginPage {

	private ExtendedWebDriver driver;

	private String tfEmail = "id=userName";
	private String tfPassword = "id=password";
	private String btnLogin = "id=login";
	private String lbError = "xpath=//p[@id='name']";

	public LoginPage(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = new ExtendedWebDriver(driver);
	}

	public void visit() throws Exception {
		driver.openUrl(EnvironmentVariables.BASE_URL + "/login");
	}
	
	public void login(String email, String password) throws Exception {
		driver.sendkeys(tfEmail, email);
		driver.sendkeys(tfPassword, password);
		driver.click(btnLogin);
	}

	public String GetMessageErrorText() throws Exception {
		return driver.getText(lbError);
	}
}
