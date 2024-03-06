package com.nashtech.automation.tests.api;

import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.nashtech.automation.model.User;
import com.nashtech.automation.service.AccountService;
import com.nashtech.automation.service.BookService;
import com.nashtech.automation.setup.APITestSetup;
import com.nashtech.automation.setup.Constant;
import com.nashtech.automation.utility.JsonParser;

public class APITests extends APITestSetup {

	User user;

	@BeforeMethod
	public void setupLogin() throws Exception {
		user = AccountService.login(Constant.testUser.username, Constant.testUser.password);
		
		String userDetail = AccountService.getUserDetail(user.token, user.id);
		System.out.println(user.token);
		JSONObject data = new JSONObject(userDetail);
		assertEquals(JsonParser.extractToString(data, "username"), Constant.testUser.username, "failed");
		assertEquals(JsonParser.extractToString(data, "userId"), user.id, "failed");

	}

	@Test
	public void LoginTest() throws Exception {
		User normalUser = AccountService.login(Constant.testUser.username, Constant.testUser.password);
		String userDetail = AccountService.getUserDetail(normalUser.token, normalUser.id);
		//JSONObject data = new JSONObject(userDetail);
		//assertEquals(data.getString("code"), "1200", "failed");
	}

	@Test
	public void addBookTest() throws Exception {
		// delete all book before test
		BookService.deleteAllBooks(user.id, user.token);

		// get user and verify all book are removed
		String userDetail = AccountService.getUserDetail(user.token, user.id);
		JSONObject data = new JSONObject(userDetail);
		assertEquals(data.getJSONArray("books").length(), 0, "all book not being deleted");

		// add book
		BookService.addBook(user.id, Constant.testBook.isbn, user.token);

		// get user again and verify new book
		userDetail = AccountService.getUserDetail(user.token, user.id);
		data = new JSONObject(userDetail);
		// verify new book
		assertEquals(data.getJSONArray("books").length(), 1, "new book not added");
		assertEquals(JsonParser.extractToString(data, "books[0].isbn"), Constant.testBook.isbn, "failed");
		assertEquals(JsonParser.extractToString(data, "books[0].title"), Constant.testBook.title,
				"failed");
		assertEquals(JsonParser.extractToString(data, "books[0].subTitle"),
				Constant.testBook.subTitle, "failed");
		assertEquals(JsonParser.extractToString(data, "books[0].author"), Constant.testBook.author, "failed");
		assertEquals(JsonParser.extractToString(data, "books[0].publish_date"), Constant.testBook.publish_date, "failed");
	}
}
