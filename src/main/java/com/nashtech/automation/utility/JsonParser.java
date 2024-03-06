package com.nashtech.automation.utility;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonParser {

	public enum OUTPUT_TYPE {
		INT, STRING, DOUBLE
	}

	private JSONObject json;

	public JsonParser(JSONObject json) {
		this.json = json;
	}

	public JsonParser() {
		// TODO Auto-generated constructor stub
	}

	public static JSONArray convetToJsonArray(String jsonString) {
		return new JSONArray(jsonString);
	}

	public static JSONObject convetToJsonObject(String jsonString) {
		return new JSONObject(jsonString);
	}

	public String extractJsonValue(String keychains) throws Exception {
		return extractToString(this.json, keychains);
	}

	public static String extractToString(JSONObject json, String keychains) throws Exception {
		return extractValue(json, keychains);
	}

	public static int extractToInt(JSONObject json, String keychains) throws Exception {
		String extractedValue = extractValue(json, keychains);
		return Integer.parseInt(extractedValue);
	}

	public static double extractToDouble(JSONObject json, String keychains) throws Exception {
		String extractedValue = extractValue(json, keychains);
		return Double.parseDouble(extractedValue);
	}

	public static String extractToString(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		return extractValue(json, keychains);
	}

	public static int extractToInt(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		String extractedValue = extractValue(json, keychains);
		return Integer.parseInt(extractedValue);
	}

	public static double extractToDouble(String jsonString, String keychains) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		String extractedValue = extractValue(json, keychains);
		return Double.parseDouble(extractedValue);
	}

	public static DesiredCapabilities convertJsonToCapabilities(String json) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		Map<String, String> caps = convertJsonToMap(json);
		if (caps != null) {
			Set<String> keys = caps.keySet();
			for (String key : keys) {
				capabilities.setCapability(key, caps.get(key));
			}
		}
		return capabilities;
	}

	public static List<String> convertJsonToArguments(String json) {
		List<String> args = new ArrayList<String>();

		Map<String, String> maps = convertJsonToMap(json);
		if (maps != null) {
			Set<String> keys = maps.keySet();
			for (String key : keys) {
				args.add(maps.get(key));
			}
		}
		return args;
	}

	public static Map<String, String> convertJsonToMap(String json) throws JSONException {
		JSONObject jsonObj = new JSONObject(json);
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> keys = jsonObj.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = jsonObj.getString(key);
			map.put(key, value);
		}
		return map;
	}

	private static String extractValue(JSONObject json, String keychains) throws Exception {

		String errorMessage = "";

		if (json != null) {
			JSONObject currentNode = json;
			String[] keys = keychains.split("\\.");

			for (int i = 0; i < keys.length; i++) {
				String currentExtractKey = keys[i];
				String key = "";
				String indexInArray = StringUtils.substringBetween(currentExtractKey, "[", "]");

				if (indexInArray != null) {
					key = currentExtractKey.substring(0, currentExtractKey.indexOf("["));
				} else {
					key = currentExtractKey;
				}

				if (currentNode.has(key)) {

					Object extractedNode = currentNode.get(key);

					if ((indexInArray != null) && (extractedNode instanceof JSONArray)) {

						extractedNode = ((JSONArray) extractedNode).get(Integer.parseInt(indexInArray));

					} else if ((indexInArray != null) && !(extractedNode instanceof JSONArray)) {

						errorMessage = "[Extract][FAILED] [" + key + "] in [" + keychains + "] is not an json array";
						break;

					}

					if (i == keys.length - 1) {

						String extractedValue = extractedNode.toString();
						return extractedValue.equalsIgnoreCase("") || extractedValue.equalsIgnoreCase("null") ? ""
								: extractedValue;

					} else if (!(extractedNode instanceof JSONObject) && !(extractedNode instanceof JSONArray)) {

						errorMessage = "[Extract][FAILED] key [" + key + "] in [" + keychains + "] does not contain ["
								+ keys[i + 1] + "]";
						break;

					} else {

						currentNode = (JSONObject) extractedNode;

					}
				} else {

					errorMessage = "[FAILED] key [" + key + "] in [" + keychains + "] does not exist";
					break;
				}
			}

		} else {
			throw new Exception("Response is null!!!");
		}

		throw new Exception(errorMessage);
	}

}
