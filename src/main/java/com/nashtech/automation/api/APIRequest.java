package com.nashtech.automation.api;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.nashtech.automation.log.Log;
import com.nashtech.automation.report.HtmlReporter;
import com.nashtech.automation.report.markuphelper.MarkupHelper;

import okhttp3.MultipartBody.Builder;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIRequest {

	enum Method {
		GET, POST, PUT, DELETE, PATCH;
	}

	private final OkHttpClient httpClient;

	private String baseUrl;
	private String path;
	public String fullUrl;

	private RequestBody bodyEntity;
	private Request request;

	// private Map<String, String> parameters = new HashMap<String, String>();
	private String paramaters = "";
	private Map<String, String> headers = new HashMap<String, String>();
	private String jsonBody;
	private Builder multipartBuilder;
	private List<NameValuePair> formData;
	private APIResponse apiResponse;
	private String method;

	

	public APIRequest() {
		httpClient = new OkHttpClient().newBuilder().connectTimeout(120, TimeUnit.SECONDS)
				.readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).build();
	}

	public APIRequest baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public APIRequest path(String path) {
		this.path = path;
		return this;
	}

	public APIRequest addParam(String key, String value) throws UnsupportedEncodingException {

		if (paramaters.equals("")) {
			paramaters += "?" + key + "=" + URLEncoder.encode(value, "UTF-8");
		} else {
			paramaters += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
		}
		return this;
	}

	public APIRequest addParam(String key, int[] values) {

		if (paramaters.equals("")) {
			paramaters = "?";
		}

		StringJoiner joiner = new StringJoiner("&");
		for (int i = 0; i < values.length; i++) {
			joiner.add(String.join("=", key, String.valueOf(values[i])));
		}

		paramaters += joiner.toString();
		return this;
	}

	public APIRequest addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	public APIRequest basic(String username, String password) {
		String key = String.join(":", username, password);
		String encode = Base64.getUrlEncoder().encodeToString(key.getBytes());
		headers.put(HttpHeaders.AUTHORIZATION, "Basic " + encode);
		return this;
	}

	public APIRequest oauth2(String accessToken) {
		headers.put(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		return this;
	}

	public APIRequest body(String body) {
		this.jsonBody = body;
		return this;
	}

	public APIRequest addFormData(String key, String value) {
		if (formData == null) {
			formData = new ArrayList<NameValuePair>();
		}

		formData.add(new BasicNameValuePair(key, value));
		return this;
	}

	public APIRequest addFormDataPart(String name, String value) {
		if (multipartBuilder == null) {
			multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		}
		multipartBuilder.addFormDataPart(name, value);
		return this;
	}

	public APIRequest addFormDataPart(String name, String filepath, String fileType) {
		if (multipartBuilder == null) {
			multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		}
		multipartBuilder.addFormDataPart(name, filepath,
				RequestBody.create(MediaType.parse(fileType), new File(filepath)));
		return this;
	}

	private String urlBuilder() {
		fullUrl = baseUrl + path + paramaters;
		return fullUrl;
	}

	private RequestBody bodyBuilder() throws UnsupportedEncodingException {

		if (bodyEntity == null && jsonBody != null) {
			// bodyEntity = RequestBody.create(MediaType.parse("application/json;
			// charset=utf-8"), jsonBody);
			bodyEntity = RequestBody.create(null, jsonBody);
			return bodyEntity;
		}

		if (bodyEntity == null && multipartBuilder != null) {
			bodyEntity = multipartBuilder.build();
			return bodyEntity;
		}

		return null;

	}

	private APIResponse execute(Method method) throws Exception {

		okhttp3.Request.Builder builder = new Request.Builder();
		RequestBody body = bodyBuilder();

		for (Entry<String, String> header : headers.entrySet()) {
			builder.addHeader(header.getKey(), header.getValue());
		}

		if (body == null)
			body = RequestBody.create(null, "");

		switch (method) {
		case GET:
			request = builder.url(urlBuilder()).get().build();
			break;
		case POST:
			request = builder.url(urlBuilder()).post(body).build();
			break;

		case PUT:
			request = builder.url(urlBuilder()).put(body).build();
			break;
		case PATCH:
			request = builder.url(urlBuilder()).patch(body).build();
			break;

		case DELETE:
			request = builder.url(urlBuilder()).delete(body).build();
			break;

		default:
			throw new Exception(String.format("This method [%s] has not been implemented", method.name()));
		}
		Response response = httpClient.newCall(request).execute();
		Log.info(String.format("[Request] - [%s] [%s]", method, request.url()));
		apiResponse = new APIResponse(response);
		HtmlReporter.info(MarkupHelper.createAPIRequestStep(this, apiResponse));
		return apiResponse;
	}

	public APIResponse get() throws Exception {
		method = "GET";
		return execute(Method.GET);
	}

	public APIResponse post() throws Exception {
		method = "POST";
		return execute(Method.POST);
	}

	public APIResponse patch() throws Exception {
		method = "PATCH";
		return execute(Method.PATCH);
	}

	public APIResponse put() throws Exception {
		method = "PUT";
		return execute(Method.PUT);
	}

	public APIResponse delete() throws Exception {
		method = "DELETE";
		return execute(Method.DELETE);
	}

	public APIResponse getResponse() throws Exception {
		return apiResponse;
	}
	
	public String method() {
		return method;
	}
	
	public Headers getHeaders() {
		return request.headers();
	}
	
	public RequestBody getBody() {
		return request.body();
	}

}
