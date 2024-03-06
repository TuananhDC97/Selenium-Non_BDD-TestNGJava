package com.nashtech.automation.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nashtech.automation.api.APIRequest;
import com.nashtech.automation.api.APIResponse;
import com.nashtech.automation.setup.EnvironmentVariables;

public class BookService {

	private static String deleteBookPath = "/BookStore/v1/Book";
	private static String deleteAllBookPath = "/BookStore/v1/Books";
	private static String addBookPath = "/BookStore/v1/Books";
	
	public static APIResponse addBook(String userId, String isbn, String token) throws Exception {

		

		JSONArray books = new JSONArray();
		JSONObject book = new JSONObject();
		book.put("isbn", isbn);
		books.put(book);
		
		JSONObject requestBody = new JSONObject();
		requestBody.put("userId", userId);
		requestBody.put("collectionOfIsbns", books);
		
		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
				.path(addBookPath)
				.addHeader("content-type", "application/json")
				.addHeader("Authorization", "Bearer " + token)
				.body(requestBody.toString())
				.post();
		return response;
	}

	public static APIResponse deleteBook(String userId, String isbn, String token) throws Exception {

		JSONObject data = new JSONObject();
		data.put("isbn", isbn);
		data.put("userId", userId);
		data.toString();

		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST).path(deleteBookPath)
				.addHeader("content-type", "application/json").addHeader("Authorization", "Bearer " + token)
				.body(data.toString()).post();
		return response;
	}

	public static APIResponse deleteAllBooks(String userId, String token) throws Exception {

		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
				.path(deleteAllBookPath)
				.addParam("UserId", userId)
				.addHeader("Authorization", "Bearer " + token)
				.delete();
		return response;
	}
}
