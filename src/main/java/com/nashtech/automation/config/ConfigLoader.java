package com.nashtech.automation.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ConfigLoader {

	private static JSONObject environmentConfiguration;
	private static JSONObject frameworkConfiguration;

	private static void initConfig() throws FileNotFoundException, IOException {
		if (environmentConfiguration == null && frameworkConfiguration == null) {
			readConfiguration();
		}
	}

	private static void readConfiguration() throws FileNotFoundException, IOException {
		InputStream iev = ConfigLoader.class.getResourceAsStream("/environment.json");
		InputStream icf = ConfigLoader.class.getResourceAsStream("/config.json");

		environmentConfiguration = new JSONObject(new JSONTokener(iev));
		frameworkConfiguration = new JSONObject(new JSONTokener(icf));

		String configEnv = System.getProperty("environment");
		if (configEnv != null) {
			environmentConfiguration = environmentConfiguration.getJSONObject(configEnv);
		} else {
			String env = environmentConfiguration.getString("environment");
			environmentConfiguration = environmentConfiguration.getJSONObject(env);
		}

	}

	public static String getEnv(String key) throws FileNotFoundException, IOException {
		initConfig();
		String config = System.getProperty(key);
		if (config != null) {
			return config;
		}
		return environmentConfiguration.get(key).toString();
	}

	public static String getConfig(String keychain) throws FileNotFoundException, IOException {
		initConfig();
		String config = System.getProperty(keychain);
		if (config != null) {
			return config;
		}
		config = "";
		try {
			if(!keychain.contains(".")) {
				return frameworkConfiguration.get(keychain).toString();
			}
			String key[] = keychain.split(".");
			int length = key.length;
			JSONObject currentObject = frameworkConfiguration;
			for (int i = 0; i < length; i++) {
				if (i != length - 1) {
					currentObject = currentObject.getJSONObject(key[i]);
				} else {
					config = currentObject.get(key[i]).toString();
				}
			}
		} catch (Exception e) {
			return config;
		}
		return config;
	}

}