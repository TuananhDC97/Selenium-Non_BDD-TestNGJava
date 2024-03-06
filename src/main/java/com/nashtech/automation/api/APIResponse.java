package com.nashtech.automation.api;

import java.io.IOException;
import org.json.JSONObject;

import com.nashtech.automation.log.Log;
import com.nashtech.automation.utility.JsonParser;

import okhttp3.Response;

public class APIResponse {

	private String responseInString;
	private int statusCode;

	public APIResponse(Response response) throws IOException {
		responseInString = response.body().string();
		statusCode = response.code();
		Log.info("[Response] - [" + statusCode + "] [" + responseInString.replaceAll("\\r|\\n|\\t|\t", "") + "]");
	}

	public int getStatusCode() {
		return this.statusCode;
	}


	public String getStringResponse() throws IOException {
		return responseInString;
	}

	public String extractJsonValue(String keychains) throws Exception {
		JsonParser jsonParser = new JsonParser();
		return jsonParser.extractJsonValue(keychains);
	}

}
