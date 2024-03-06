package com.nashtech.automation.service;

import org.json.JSONObject;

import com.nashtech.automation.api.APIRequest;
import com.nashtech.automation.api.APIResponse;
import com.nashtech.automation.config.ConfigLoader;
import com.nashtech.automation.model.User;
import com.nashtech.automation.setup.EnvironmentVariables;

public class AccountService {
	
	private static String gentokenPath = "/Account/v1/GenerateToken";
	private static String loginPath = "/Account/v1/Login";
	private static String userDetaiPath = "/Account/v1/User/%s";
	
	
	private static APIResponse genTokenRequest(String username, String password) throws Exception {
		
		String requestBody = "{\r\n" + 
				"  \"userName\": \"" + username + "\",\r\n" + 
				"  \"password\": \"" + password + "\"\r\n" + 
				"}";
		
		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
			.path(gentokenPath)
			.addHeader("content-type", "application/json")
			.body(requestBody)
			.post();
		return response;
	}
	
	private static APIResponse getUserDetailRequest(String token, String userId) throws Exception {
		
		
		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
			.path(String.format(userDetaiPath, userId))
			.addHeader("content-type", "application/json")
			.addHeader("Authorization", "Bearer " + token)
			.get();
		return response;
	}
	
	private static APIResponse loginRequest(String username, String password) throws Exception {
		
		String requestBody = "{\r\n" + 
				"  \"userName\": \"" + username + "\",\r\n" + 
				"  \"password\": \"" + password + "\"\r\n" + 
				"}";
		
		JSONObject data = new JSONObject();
		data.put("userName", username);
		data.put("password", password);
		data.toString();
		
		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
			.path(loginPath)
			.addHeader("content-type", "application/json")
			.body(requestBody)
			.post();
		return response;
	}
	
	public static User login(String username, String password) throws Exception {
		APIResponse response = loginRequest(username,password);
		JSONObject jsonObject = new JSONObject(response.getStringResponse());
		String token = jsonObject.getString("token");
		String userId = jsonObject.getString("userId");
		User user = new User();
		user.setId(userId);
		user.setToken(token);
		return user;
	}
	
	public static String getUserDetail(String token, String userId) throws Exception {
		APIResponse response = getUserDetailRequest(token,userId);
		return response.getStringResponse();
	}

	private static APIResponse getUserBooksList(String userId, String token) throws Exception {
		
		APIRequest request = new APIRequest();
		APIResponse response = request.baseUrl(EnvironmentVariables.BACKEND_HOST)
			.path("/" + userId)
			.addHeader("Authorization", "Bearer " + token)
			.get();
		return response;
	}
	
}
