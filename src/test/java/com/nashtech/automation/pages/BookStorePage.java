package com.nashtech.automation.pages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.nashtech.automation.selenium.ExtendedWebDriver;
import com.nashtech.automation.setup.EnvironmentVariables;

public class BookStorePage{
	
	private ExtendedWebDriver driver;
	
	private String lbBooks = "xpath=//a[contains(@href,'/books?book=')]";
	private String lbBook = "xpath=(//a[contains(@href,'/books?book=')])[%s]";
	private String tfSearch = "id=searchBox";
	private String btnSearch = "css=div.input-group-append";
	
	public BookStorePage(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = new ExtendedWebDriver(driver);
	}
	
	public boolean isOpened() throws Exception {
		return driver.isUrlToBe(EnvironmentVariables.BASE_URL + "/books", 5);
	}
	
	public void search(String bookName) throws Exception {
		driver.sendkeys(tfSearch, bookName);
		driver.click(btnSearch);
	}
	
	public List<String> getBooksName() throws Exception {
		return driver.getTexts(lbBooks);
	}
	
	public void selectBook(int index) throws Exception {
		driver.click(String.format(lbBook, index));
	}
	
	
}
