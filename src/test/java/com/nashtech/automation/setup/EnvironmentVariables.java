package com.nashtech.automation.setup;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nashtech.automation.config.ConfigLoader;

public class EnvironmentVariables {
	
	public String ENVIRONMENT;
	
	public static String BASE_URL;
	public static String BACKEND_HOST;
	
	public static void init() throws FileNotFoundException, IOException {
		BASE_URL = ConfigLoader.getEnv("url");
		BACKEND_HOST = ConfigLoader.getEnv("backend_host");
	}
	
}