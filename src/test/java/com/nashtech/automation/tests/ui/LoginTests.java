package com.nashtech.automation.tests.ui;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nashtech.automation.pages.ProfilePage;
import com.nashtech.automation.service.AccountService;
import com.nashtech.automation.service.BookService;
import com.nashtech.automation.model.User;
import com.nashtech.automation.pages.LoginPage;
import com.nashtech.automation.setup.Constant;
import com.nashtech.automation.setup.WebTestSetup;
import com.nashtech.automation.utility.JsonParser;

public class LoginTests extends WebTestSetup {

	private LoginPage _loginPage;

	User user;

	@BeforeMethod
	public void setup() throws Exception {
		_loginPage = new LoginPage(getDriver());
	}

	@DataProvider(name = "data")
	public Object[][] data() {
		return new Object[][] { { "Test@Test112.com", "12378" }, { "Test@Test112.com", "123456" } };
	}

	@Test(dataProvider = "data")
	public void loginTestUnsuccessfullyDataDriven(String email, String pass) throws Exception {
		_loginPage.visit();
		_loginPage.login(email, pass);
		assertEquals(_loginPage.GetMessageErrorText().trim(), "Invalid username or password!");
	}

}
