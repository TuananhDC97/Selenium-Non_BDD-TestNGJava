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
import com.nashtech.automation.pages.BookPage;
import com.nashtech.automation.pages.BookStorePage;
import com.nashtech.automation.pages.LoginPage;
import com.nashtech.automation.pages.MenuPage;
import com.nashtech.automation.setup.Constant;
import com.nashtech.automation.setup.WebTestSetup;
import com.nashtech.automation.utility.JsonParser;

public class BookTests extends WebTestSetup {

	private LoginPage _loginPage;
	private ProfilePage _profilePage;
	private MenuPage _menuPage;
	private BookStorePage _bookStorePage;
	private BookPage _bookPage;

	User user;

	@BeforeMethod
	public void setup() throws Exception {
		_loginPage = new LoginPage(getDriver());
		_profilePage = new ProfilePage(getDriver());
		_menuPage = new MenuPage(getDriver());
		_bookStorePage = new BookStorePage(getDriver());
		_bookPage = new BookPage(getDriver());
		//api requests for setup
		//login for token 
		user = AccountService.login(Constant.testUser.username, Constant.testUser.password);
		// delete all book 
		BookService.deleteAllBooks(user.id, user.token);
		
	}

	@Test
	public void addBookTest() throws Exception {
		//login in UI and verify books displayed
		_loginPage.visit();
		_loginPage.login("tunguyen01", "123@123aQ");
		assertTrue(_profilePage.isOpened(), "Login failed");
		
		//verify 0 book
		List<String> booksName = _profilePage.getBooksName();
		assertEquals(booksName.size(),0,"Number of book are incorrect");
		
		//to store
		_menuPage.goToBookStore();
		assertTrue(_bookStorePage.isOpened(), "Cannot open book store screen");
		
		//search a book and verify result
		_bookStorePage.search(Constant.testBook.title);
		
		booksName = _bookStorePage.getBooksName();
		assertEquals(booksName.size(),1,"Number of result are incorrect");
		assertEquals(booksName.get(0),Constant.testBook.title,"search result is incorrect");
		
		//select book and add to collection
		_bookStorePage.selectBook(1);
		assertTrue(_bookPage.isOpened(), "Cannot open book detail screen");
		
		_bookPage.saveBook();
		
		//back to profile and verify
		_menuPage.goToProfile();
		assertTrue(_profilePage.isOpened(), "Login failed");
		
		booksName = _profilePage.getBooksName();
		assertEquals(booksName.size(),1,"Number of book are incorrect");
		assertEquals(booksName.get(0),Constant.testBook.title,"Book title is incorrect");
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
