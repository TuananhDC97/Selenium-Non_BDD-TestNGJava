package com.nashtech.automation.pages;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nashtech.automation.selenium.ExtendedWebDriver;
import com.nashtech.automation.setup.EnvironmentVariables;

public class BookPage {

	private ExtendedWebDriver driver;
	
	private String btnAddToCollection = "xpath=//button[text()='Add To Your Collection']";

	public BookPage(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = new ExtendedWebDriver(driver);
	}

	public boolean isOpened() throws Exception {
		return driver.isUrlToBe(EnvironmentVariables.BASE_URL + "/books?book=", 5);
	}
	
	public void saveBook() throws Exception {
		driver.scrollElementIntoMiddle(btnAddToCollection);
		driver.click(btnAddToCollection);
		driver.acceptAlert();
	}
}
