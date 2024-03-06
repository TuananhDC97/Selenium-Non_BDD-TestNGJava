package com.nashtech.automation.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import com.nashtech.automation.config.ConfigLoader;
import com.nashtech.automation.log.Log;
import com.nashtech.automation.report.HtmlReportDirectory;
import com.nashtech.automation.report.HtmlReporter;
import com.nashtech.automation.utility.Common;
import com.nashtech.automation.utility.Constant;
import com.nashtech.automation.utility.FilePaths;

public class ExtendedWebDriver {

	private int EXPLICIT_WAIT;
	private int IMPLICIT_WAIT;
	private WebDriverWait wait;
	private WebDriver driver;

	public ExtendedWebDriver(WebDriver driver) throws FileNotFoundException, IOException {
		this.driver = driver;
		String configExplicit = ConfigLoader.getConfig("explicitWait");
		if (Common.isNullOrBlank(configExplicit)) {
			EXPLICIT_WAIT = Constant.DEFAULT_EXPLICIT_WAIT;
		} else {
			EXPLICIT_WAIT = Integer.valueOf(configExplicit);
		}
		
		String configImplicit = ConfigLoader.getConfig("implicitWait");
		if (Common.isNullOrBlank(configImplicit)) {
			IMPLICIT_WAIT = Constant.DEFAULT_IMPLICIT_WAIT;
		} else {
			IMPLICIT_WAIT = Integer.valueOf(configImplicit);
		}
		
		initExplicitWait();
	}

	public void initExplicitWait() {

		wait = new WebDriverWait(driver, getDefaultExplicitWait());
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
	}

	public int getDefaultExplicitWait() {
		return EXPLICIT_WAIT;
	}
	
	public int getDefaultImplicitWait() {
		return EXPLICIT_WAIT;
	}

	public WebElement highlightElement(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].style.border='2px solid red'", element);
		return element;
	}

	public List<WebElement> highlightElement(List<WebElement> elements) {
		for (WebElement e : elements) {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].style.border='2px solid red'", e);
		}
		return elements;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findElement(String elementInfo) throws Exception {
		By by = getElementBy(elementInfo);
		return findElement(by);
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findElements(String elementInfo) throws Exception {
		By by = getElementBy(elementInfo);
		return findElements(by);
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findChildElement(WebElement parentElement, String childElement) throws Exception {
		By by = getElementBy(childElement);
		return findChildElement(parentElement, by);
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findChildElements(WebElement parentElement, String childElement) throws Exception {
		By by = getElementBy(childElement);
		return findChildElements(parentElement, by);
	}

	public By getElementBy(String element) throws Exception {
		String[] extract = element.split("=", 2);
		String byInfo = extract[0];
		String value = extract[1];
		By by = null;
		try {

			switch (byInfo) {
			case "id":
				by = By.id(value);
				break;
			case "name":
				by = By.name(value);
				break;
			case "css":
				by = By.cssSelector(value);
				break;
			case "linktext":
				by = By.linkText(value);
				break;
			case "partiallinktext":
				by = By.partialLinkText(value);
				break;
			case "tagname":
				by = By.tagName(value);
				break;
			case "xpath":
				by = By.xpath(value);
				break;
			}
		} catch (Exception e) {
			HtmlReporter.fail("Cannot get element by [" + element + "]", e);
			throw (e);
		}
		return by;

	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findElement(By by) throws Exception {
		WebElement element = null;
		try {
			element = driver.findElement(by);
			highlightElement(element);
		} catch (NoSuchElementException e) {
			HtmlReporter.fail("The element : [" + by + "] located by : [" + by.toString() + "] isn't found", e);
			throw new NoSuchElementException(
					"The element : [" + by + "] located by : [" + by.toString() + "] isn't found");
		} catch (StaleElementReferenceException e) {
			return findElement(by);
		} catch (Exception e) {
			HtmlReporter.fail("Error when find The element : [" + by + "] located by : [" + by.toString() + "]", e);
			throw (e);
		}
		return element;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findElements(By by) throws Exception {
		List<WebElement> listElement = new ArrayList<WebElement>();
		try {
			listElement = driver.findElements(by);
			highlightElement(listElement);
		} catch (NoSuchElementException e) {
			HtmlReporter.fail(
					"The list element : [" + by.toString() + "] located by : [" + by.toString() + "] isn't found", e);
			throw new NoSuchElementException(
					"The element : [" + by.toString() + "] located by : [" + by.toString() + "] isn't found");
		} catch (StaleElementReferenceException e) {
			return findElements(by);
		} catch (Exception e) {
			HtmlReporter.fail(
					"The list element : [" + by.toString() + "] located by : [" + by.toString() + "] isn't found", e);
			throw (e);
		}

		return listElement;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findChildElement(WebElement parentElement, By childElement) throws Exception {
		WebElement element = null;
		try {
			parentElement.findElement(childElement);
			highlightElement(element);
		} catch (NoSuchElementException e) {
			HtmlReporter.fail("The child element : [" + childElement + "] located by : [" + childElement.toString()
					+ "] isn't found", e);
			throw new NoSuchElementException("The child element : [" + childElement + "] located by : ["
					+ childElement.toString() + "] isn't found");
		} catch (Exception e) {
			HtmlReporter.fail("The child element : [" + childElement + "] located by : [" + childElement.toString()
					+ "] isn't found", e);
			throw (e);
		}
		return element;
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public List<WebElement> findChildElements(WebElement parent, By childElement) throws Exception {
		List<WebElement> listElement = null;
		try {
			parent.findElement(childElement);
			highlightElement(listElement);
		} catch (Exception e) {
			HtmlReporter.fail("The list element : [" + childElement + "] located by : [" + childElement.toString()
					+ "] isn't found", e);
			throw (e);
		}
		return listElement;
	}

	/**
	 * Set the time out to wait for page load
	 * 
	 * @param seconds
	 *            Wait time in seconds
	 */
	public void setImplicitWaitTime(int seconds) {
		try {
			driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	/**
	 * Execute javascript. This method used to execute a javascript
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            the js function
	 * @throws Exception
	 *             The exception is thrown if can't execute java script
	 */
	public void executeJavascript(String jsFunction) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript(jsFunction);
			HtmlReporter.pass("Excecuted the java script: [" + jsFunction + "]");
		} catch (Exception e) {
			HtmlReporter.fail("Can't excecute the java script: [" + jsFunction + "]", e);
			throw (e);
		}
	}

	/**
	 * This method is used to execute a java script function for an object argument.
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            The java script function
	 * @param object
	 *            The argument to execute script
	 * @throws Exception
	 *             The exception is thrown if object is invalid.
	 */
	public void executeJavascript(String jsFunction, Object... object) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript(jsFunction, object);
			Log.info("Excecute the java script: [" + jsFunction + "] for the object: [" + object + "]");
		} catch (Exception e) {
			Log.info("Can't excecute the java script: [" + jsFunction + "] for the object: [" + object + "]");
			throw (e);

		}
	}

	/**
	 * This method is used to wait for the page load
	 * 
	 * @author Hanoi Automation team
	 * @param
	 * @return None
	 * @throws Exception
	 */
	public void waitForPageLoad() {

		WebDriverWait wait = new WebDriverWait(driver, getDefaultExplicitWait());

		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		// JQuery Wait
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0;
			}
		};

		// Angular Wait
		String angularReadyScript = "return angular.element(document).injector().get('$http').pendingRequests.length";
		ExpectedCondition<Boolean> angularLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver).executeScript(angularReadyScript) == 0;
			}
		};

		wait.until(jsLoad);
		// wait.until(jQueryLoad);
		 wait.until(angularLoad);
	}

	/**
	 * This method is used to navigate the browser to the url
	 * 
	 * @author Hanoi Automation team
	 * @param url
	 *            the url of website
	 * @return None
	 * @throws Exception
	 *             The exception is thrown if the driver can't navigate to the url
	 */
	public void openUrl(String url) throws Exception {
		try {
			driver.get(url);
			HtmlReporter.pass("Navigated to the url : [" + url + "]");
		} catch (Exception e) {
			HtmlReporter.fail("Can't navigate to the url : [" + url + "]", e);
			throw (e);

		}
	}

	public String getCurrentURL() {
		String currentURL = driver.getCurrentUrl();
		HtmlReporter.pass("Currrent URL: [" + currentURL + "]");
		return currentURL;
	}

	public void clearText(String element) throws Exception {
		By by = getElementBy(element);
		clearText(by);
	}

	public void clearText(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			e.clear();
		} catch (StaleElementReferenceException e) {
			clearText(by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't clear text of the element: [" + by.toString() + "]", e);
			throw (e);
		}
	}

	public void sendkeys(String element, CharSequence keysToSend) throws Exception {
		By by = getElementBy(element);
		sendkeys(by, keysToSend);

	}

	public void sendkeys(By by, CharSequence keysToSend) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			e.sendKeys(keysToSend);
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			sendkeys(by, keysToSend);
		} catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + by.toString() + "]", e);
			throw (e);
		}
	}

	public void sendKeys(String element, CharSequence keysToSend, int delay) throws Exception {
		try {
			WebElement e = findElement(element);
			for (int i = 0; i < keysToSend.length(); i++) {
				char c = keysToSend.charAt(i);
				e.sendKeys(Character.toString(c));
				Thread.sleep(delay);
			}
			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + element + "]");
		} catch (StaleElementReferenceException e) {
			sendKeys(element, keysToSend, delay);
		} catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + element + "]", e);
			throw (e);
		}
	}

	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendkeysByAction(String element, CharSequence keysToSend) throws Exception {
		By by = getElementBy(element);
		sendkeysByAction(by,keysToSend);
	}
	
	public void sendkeysByAction(By by, CharSequence keysToSend) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			String platform = System.getProperty("os.name");
			if (platform.toLowerCase().contains("mac")) {
				new Actions(driver).click(e).pause(200).keyDown(Keys.COMMAND).sendKeys("a").keyUp(Keys.COMMAND)
						.pause(200).sendKeys(Keys.BACK_SPACE).pause(200).sendKeys(keysToSend).perform();
			} else {
				new Actions(driver).click(e).pause(200).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
						.pause(200).sendKeys(Keys.BACK_SPACE).pause(200).sendKeys(keysToSend).perform();
			}

			HtmlReporter.pass("Inputed [" + keysToSend + "] into element [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			sendkeys(by, keysToSend);
		} catch (Exception e) {
			HtmlReporter.fail("Can't sendkeys [" + keysToSend + "] to the element: [" + by.toString() + "]", e);
			throw (e);
		}
	}

	/**
	 * Get the text of a web element
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of web element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getText(String element) throws Exception {
		By by = getElementBy(element);
		return getText(by);
	}

	public String getText(By by) throws Exception {
		try {
			String text = "";
			WebElement e = findElement(by);
			if (e.getTagName().equalsIgnoreCase("input")) {
				text = e.getAttribute("value");
			} else {
				text = e.getText();
			}
			HtmlReporter.pass("Element [" + by.toString() + "] has text: [" + text + "]");
			return text;
		} catch (StaleElementReferenceException e) {
			return getText(by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of element: [" + by.toString() + "]", e);
			throw e;
		}
	}

	public String getText(WebElement parent, String childElement) throws Exception {
		By by = getElementBy(childElement);
		return getText(parent, by);
	}

	public String getText(WebElement parent, By by) throws Exception {
		try {
			String text = "";
			WebElement e = findChildElement(parent, by);
			highlightElement(e);
			if (e.getTagName().equalsIgnoreCase("input")) {
				text = e.getAttribute("value");
			} else if (e.getTagName().equalsIgnoreCase("select")) {
				text = new Select(e).getFirstSelectedOption().getText();
			} else {
				text = e.getText();
			}
			HtmlReporter.pass("Element [" + by.toString() + "] has text: [" + text + "]");
			return text;
		} catch (StaleElementReferenceException e) {
			return getText(parent, by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of element: [" + by.toString() + "]", e);
			throw e;
		}
	}

	public List<String> getTexts(String element) throws Exception {
		By by = getElementBy(element);
		return getTexts(by);
	}

	public List<String> getTexts(By by) throws Exception {
		List<String> texts = new ArrayList<String>();
		try {
			String output = "";
			List<WebElement> elements = findElements(by);
			for (WebElement e : elements) {
				if (e.getTagName().equalsIgnoreCase("input")) {
					texts.add(e.getAttribute("value"));
					output += "[" + e.getAttribute("value") + "]-";
				} else {
					texts.add(e.getText());
					output += "-[" + e.getText() + "]";
				}
			}

			HtmlReporter.pass("List Elements [" + by.toString() + "] has text: " + output + "");
			return texts;
		} catch (StaleElementReferenceException e) {
			return getTexts(by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of list elements: [" + by.toString() + "]", e);
			throw e;
		}
	}

	public String getTitle() throws Exception {
		try {
			waitForPageLoad();
			String title = driver.getTitle();
			HtmlReporter.pass("Current title is: [" + title + "]");
			return title;
		} catch (Exception e) {
			HtmlReporter.fail("Cannot get title of screen", e);
			throw e;
		}
	}

	public String getTextSelectedDDL(String element) throws Exception {
		By by = getElementBy(element);
		return getTextSelectedDDL(by);
	}

	public String getTextSelectedDDL(By by) throws Exception {
		try {
			String text = "";
			Select ddl = new Select(findElement(by));
			text = ddl.getFirstSelectedOption().getText();
			HtmlReporter.pass("Current value of [" + by.toString() + "] is : [" + text + "]");
			return text;
		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of Dropdown: [" + by.toString() + "]", e);
			return "";

		}
	}

	public String getTextDDL(String element) throws Exception {
		By by = getElementBy(element);
		return getTextDDL(by);
	}

	public String getTextDDL(By by) throws Exception {
		try {
			String text = "";
			Select ddl = new Select(findElement(by));
			for (WebElement option : ddl.getOptions()) {
				text = text + option.getText();
			}
			HtmlReporter.pass("Got the text of Dropdown [" + by.toString() + "] is : [" + text + "]");
			return text;

		} catch (Exception e) {
			HtmlReporter.fail("Can't get text of Dropdown: [" + by.toString() + "]", e);
			return "";

		}
	}

	public String getAttribute(String element, String attribute) throws Exception {
		By by = getElementBy(element);
		return getAttribute(by, attribute);
	}

	public String getAttribute(By by, String attribute) throws Exception {
		try {
			String attributeValue = findElement(by).getAttribute(attribute);
			HtmlReporter.pass(
					"Attribute [" + attribute + "] of element [" + by.toString() + "] is [" + attributeValue + "]");
			return attributeValue;
		} catch (StaleElementReferenceException e) {
			return getAttribute(by.toString(), attribute);
		} catch (Exception e) {
			HtmlReporter.fail("Can't get the attribute [" + attribute + "] of element [" + by.toString() + "]", e);
			throw e;

		}
	}

	public void setAttribute(String element, String attribute, String value) throws Exception {
		By by = getElementBy(element);
		setAttribute(by, attribute, value);
	}

	public void setAttribute(By by, String attribute, String value) throws Exception {
		try {
			WebElement e = findElement(by);
			executeJavascript("arguments[0].setAttribute(arguments[1], arguments[2]);", e, attribute, attribute);
			HtmlReporter
					.pass("Set Attribute [" + attribute + "] of element [" + by.toString() + "] to [" + value + "]");
		} catch (StaleElementReferenceException e) {
			setAttribute(by, attribute, attribute);
		} catch (Exception e) {
			HtmlReporter.fail("Can't set the attribute [" + attribute + "] to element [" + by.toString() + "]", e);
			throw e;

		}
	}

	public void click(String element) throws Exception {
		By by = getElementBy(element);
		click(by);
	}

	public void click(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			e.click();
			HtmlReporter.pass("Click on the element: [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			click(by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't click on the element: [" + by.toString() + "]", e);
			throw (e);
		}
	}

	public void doubleClick(String element) throws Exception {
		By by = getElementBy(element);
		doubleClick(by);
	}

	public void doubleClick(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			Actions action = new Actions(driver);
			action.moveToElement(e).doubleClick().build().perform();
			HtmlReporter.pass("DoubleClick on the element: [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			doubleClick(by);
		} catch (Exception e) {
			HtmlReporter.fail("DoubleClick on the element: [" + by.toString() + "] failed", e);
			throw e;

		}
	}

	public void clickByJS(String element) throws Exception {
		By by = getElementBy(element);
		clickByJS(by);
	}

	public void clickByJS(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			executeJavascript("arguments[0].click();", e);
			HtmlReporter.pass("Click by JavaScript on the element: [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			clickByJS(by);
		} catch (Exception e) {
			HtmlReporter.fail("Can't click by Java Script on the element: [" + by.toString() + "]", e);
			throw (e);
		}
	}

	public void clickByAction(String element) throws Exception {
		By by = getElementBy(element);
		clickByAction(by);
	}

	public void clickByAction(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			Actions action = new Actions(driver);
			action.click(e).build().perform();
			HtmlReporter.pass("Click by Actions on the element: [" + by.toString() + "]");
		} catch (StaleElementReferenceException e) {
			clickByAction(by);
		} catch (Exception e) {
			HtmlReporter.fail("Click by Actions on [" + by.toString() + "] failed", e);
			throw e;

		}
	}

	/**
	 * Select a radio button
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectRadioButton(String element) throws Exception {
		By by = getElementBy(element);
		selectRadioButton(by);
	}

	public void selectRadioButton(By by) throws Exception {
		try {

			WebElement e = waitForElementDisplayed(by);
			if (!e.isSelected()) {
				e.click();
			}
			HtmlReporter.pass("Radio button element: [" + by.toString() + "] is selected.");

		} catch (StaleElementReferenceException e) {
			selectRadioButton(by);
		} catch (Exception e) {
			HtmlReporter.fail("Radio button element: [" + by.toString() + "] isn't selected.", e);
			throw (e);
		}
	}

	/**
	 * Select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectCheckBox(String element) throws Exception {
		By by = getElementBy(element);
		selectCheckBox(by);
	}

	public void selectCheckBox(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			if (!e.isSelected()) {
				e.click();
			}
			HtmlReporter.pass("Checkbox element: [" + by.toString() + "] is selected.");
		} catch (StaleElementReferenceException e) {
			selectRadioButton(by);
		} catch (Exception e) {
			HtmlReporter.fail("Checkbox element: [" + by.toString() + "] isn't selected.", e);
			throw (e);
		}

	}

	/**
	 * De-select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void deselectCheckBox(String element) throws Exception {
		By by = getElementBy(element);
		deselectCheckBox(by);
	}

	public void deselectCheckBox(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);

			if (e.isSelected()) {
				e.click();
			}
			HtmlReporter.pass("Checkbox element: [" + by.toString() + "] is deselected.");

		} catch (StaleElementReferenceException e) {
			deselectCheckBox(by);
		} catch (Exception e) {
			HtmlReporter.fail("Checkbox element: [" + by.toString() + "] isn't deselected.", e);
			throw (e);
		}
	}

	/**
	 * Select an option in the Drop Down list
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param chosenOption
	 *            The option is chosen
	 * @throws Exception
	 */
	public void selectDDLByText(String element, String chosenOption) throws Exception {
		By by = getElementBy(element);
		selectDDLByText(by, chosenOption);
	}

	public void selectDDLByText(By by, String chosenOption) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			Select ddl = new Select(e);
			ddl.selectByVisibleText(chosenOption);
			HtmlReporter.pass("Select option by Text: [" + chosenOption + "] from select box: [" + by.toString() + "]");

		} catch (StaleElementReferenceException e) {
			selectDDLByText(by, chosenOption);
		} catch (Exception e) {
			HtmlReporter.fail(
					"Can't select option: [" + chosenOption + "] by Text from the select box: [" + by.toString() + "]",
					e);

			throw (e);
		}
	}

	/**
	 * Select an option in the Drop Down list by value
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param value
	 *            The value is chosen
	 * @throws Exception
	 */
	public void selectDDLByValue(String element, String value) throws Exception {
		By by = getElementBy(element);
		selectDDLByValue(by, value);
	}

	public void selectDDLByValue(By by, String value) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			Select ddl = new Select(e);
			ddl.selectByValue(value);
			HtmlReporter.pass("Select option by Value: [" + value + "] from select box: [" + by.toString() + "]");

		} catch (StaleElementReferenceException e) {
			selectDDLByValue(by, value);
		} catch (Exception e) {
			HtmlReporter.fail(
					"Can't select option: [" + value + "] by Value from the select box: [" + by.toString() + "]", e);

			throw e;
		}
	}

	public boolean isTitleToBe(String expectedTitle, int timeout) throws Exception {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.titleIs(expectedTitle));
			if (result) {
				HtmlReporter.info(String.format("Title is become matched with expectation: [%s]", expectedTitle));
			} else {
				HtmlReporter
						.warning(String.format("Title is not become matched with expectation: [%s]", expectedTitle));
			}
			return result;
		} catch (TimeoutException e) {
			HtmlReporter.warning(String.format("Title is not become matched with expectation: [%s]", expectedTitle));
			return false;
		}
	}
	
	public boolean isUrlToBe(String expectedTitle, int timeout) throws Exception {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.urlContains(expectedTitle));
			if (result) {
				HtmlReporter.info(String.format("Url is become matched with expectation: [%s]", expectedTitle));
			} else {
				HtmlReporter
						.warning(String.format("Url is not become matched with expectation: [%s]", expectedTitle));
			}
			return result;
		} catch (TimeoutException e) {
			HtmlReporter.warning(String.format("Url is not become matched with expectation: [%s]", expectedTitle));
			return false;
		}
	}
	
	public void waitForTextOfElementToBe(String element, String expectedText) throws Exception {
		By by = getElementBy(element);
		waitForTextOfElementToBe(by,expectedText,getDefaultExplicitWait());
	}
	
	public void waitForTextOfElementToBe(By by, String expectedText) throws Exception {
		waitForTextOfElementToBe(by,expectedText,getDefaultExplicitWait());
	}

	public void waitForTextOfElementToBe(String element, String expectedText, int timeout) throws Exception {
		By by = getElementBy(element);
		waitForTextOfElementToBe(by,expectedText,timeout);
	}

	public void waitForTextOfElementToBe(By by, String expectedText, int timeout) throws Exception {
		try {
			setImplicitWaitTime(timeout);
			wait.withTimeout(Duration.ofSeconds(timeout));
			// wait.until((driver) -> !element.getText().equals(""));
			wait.until(new ExpectedCondition<String>() {
				@Override
				public String apply(WebDriver driver) {
					String currenText;
					try {
						currenText = driver.findElement(by).getText().replace("\\n", "").replace("\n", "");
					} catch (StaleElementReferenceException e) {
						return null;
					} catch (NoSuchElementException e) {
						return null;
					}
					return currenText.equals(expectedText) ? currenText : null;
				}

				@Override
				public String toString() {
					return String.format("Current text: [%s]", driver.findElement(by).getText());
				}
			});
		} catch (TimeoutException e) {
			Log.warn(String.format("Text of Element [%s] is not become [%s] in expected time = %s", by.toString(),
					expectedText, timeout));
			throw e;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become [%s] in expected time = %s",
					by.toString(), expectedText, timeout), e);
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
	}
	
	public void waitForTextOfElementToContains(String element, String expectedText) throws Exception {
		By by = getElementBy(element);
		waitForTextOfElementToBe(by,expectedText,getDefaultExplicitWait());
	}
	
	public void waitForTextOfElementToContains(By by, String expectedText) throws Exception {
		waitForTextOfElementToBe(by,expectedText,getDefaultExplicitWait());
	}

	public void waitForTextOfElementToContains(String element, String expectedText, int timeout) throws Exception {
		By by = getElementBy(element);
		waitForTextOfElementToBe(by,expectedText,timeout);
	}

	public boolean waitForTextOfElementToContains(By by, String expectedText, int timeout) throws Exception {
		try {
			setImplicitWaitTime(timeout);
			wait.withTimeout(Duration.ofSeconds(timeout));
			// wait.until((driver) -> !element.getText().equals(""));
			wait.until(new ExpectedCondition<String>() {
				@Override
				public String apply(WebDriver driver) {
					String currenText;
					try {
						currenText = driver.findElement(by).getText();
					} catch (StaleElementReferenceException e) {
						return null;
					} catch (NoSuchElementException e) {
						return null;
					}
					return currenText.contains(expectedText) ? currenText : null;
				}

				@Override
				public String toString() {
					return String.format("Current text: [%s]", driver.findElement(by).getText());
				}
			});
			return true;
		} catch (TimeoutException e) {
			Log.warn(String.format("Text of Element [%s] is not become contains [%s] in expected time = %s",
					by.toString(), expectedText, timeout));
			throw e;
		} catch (Exception e) {
			HtmlReporter.fail(String.format("Text of Element [%s] is not become contains [%s] in expected time = %s",
					by.toString(), expectedText, timeout), e);
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
	}
	
	public boolean isElementEnabled(String element) throws Exception {
		By by = getElementBy(element);
		return isElementEnabled(by);
	}

	public boolean isElementEnabled(By by) throws Exception {
		try {
			boolean check = findElement(by).isEnabled();
			if (check) {
				Log.info(String.format("Element: [%s] is enabled", by.toString()));
				return true;
			} else {
				Log.info(String.format("Element: [%s] is disabled", by.toString()));
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	public boolean isElementDisplayed(String element) throws Exception {
		return isElementDisplayed(element, getDefaultExplicitWait());
	}
	
	public boolean isElementDisplayed(By by) throws Exception {
		return isElementDisplayed(by, getDefaultExplicitWait());
	}

	public boolean isElementDisplayed(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return isElementDisplayed(by,timeout);
	}
	
	public boolean isElementDisplayed(By by, int timeout) throws Exception {
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			if (e != null) {
				highlightElement(e);
				Log.info(String.format("Element: [%s] is displayed", by.toString()));
				return true;
			} else {
				Log.warn(String.format("Element: [%s] is not displayed", by.toString()));
				return false;
			}
		} catch (NoSuchElementException e) {
			Log.warn(String.format("Element: [%s] is not displayed in %s", by.toString(), timeout));
			return false;
		} catch (StaleElementReferenceException e) {
			return isElementDisplayed(by, timeout);
		} catch (TimeoutException e) {
			Log.warn(String.format("Element: [%s] is not displayed in %s", by.toString(), timeout));
			return false;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
	}

	public WebElement waitForElementDisplayed(String element) throws Exception {
		return waitForElementDisplayed(element, getDefaultExplicitWait());
	}

	public WebElement waitForElementDisplayed(By by) throws Exception {
		return waitForElementDisplayed(by, getDefaultExplicitWait());
	}

	public WebElement waitForElementDisplayed(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return waitForElementDisplayed(by, timeout);
	}

	public WebElement waitForElementDisplayed(By by, int timeout) throws Exception {
		WebElement element = null;
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			Log.info(String.format("Element: [%s] is displayed in expected time = %s", by.toString(), timeout));
			highlightElement(element);
		} catch (TimeoutException e) {
			Log.error(String.format("Element [%s] is not displayed in expected time = %s", by.toString(), timeout));
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
		return element;
	}

	
	public boolean isElementDisappear(String element) throws Exception {
		return isElementDisappear(element, getDefaultExplicitWait());
	}

	public boolean isElementDisappear(By by) throws Exception {
		return isElementDisappear(by, getDefaultExplicitWait());
	}

	public boolean isElementDisappear(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return isElementDisappear(by, timeout);
	}
	
	public boolean isElementDisappear(By by, int timeout) throws Exception {
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			if (result) {
				Log.info(String.format("Element: [%s] is disappeared", by.toString()));
			} else {
				Log.warn(String.format("Element: [%s] is not disappeared", by.toString()));
			}
			return result;
		} catch (TimeoutException e) {
			Log.warn(String.format("Element: [%s] is not disappeared in %s", by.toString(), timeout));
			return false;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
	}

	
	public boolean waitForElementDisappear(String element) throws Exception {
		return waitForElementDisappear(element, getDefaultExplicitWait());
	}

	public boolean waitForElementDisappear(By by) throws Exception {
		return waitForElementDisappear(by, getDefaultExplicitWait());
	}

	public boolean waitForElementDisappear(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return waitForElementDisappear(by, timeout);
	}
	
	public boolean waitForElementDisappear(By by, int timeout) throws Exception {
		try {
			setImplicitWaitTime(0);
			wait.withTimeout(Duration.ofSeconds(timeout));
			boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			if (result) {
				HtmlReporter
						.pass(String.format("Element: [%s] is disppeared in expected time = %s", by.toString(), timeout));
			} else {
				HtmlReporter.pass(
						String.format("Element: [%s] is not disppeared in expected time = %s", by.toString(), timeout));
			}
		} catch (TimeoutException e) {
			HtmlReporter.fail(
					String.format("Element [%s] is not displayed in expected time = %s", by.toString(), timeout), e);
			throw e;
		} finally {
			setImplicitWaitTime(IMPLICIT_WAIT);
		}
		return true;
	}
	
	public WebElement waitForElementPresent(String element) throws Exception {
		return waitForElementPresent(element, getDefaultExplicitWait());
	}

	public WebElement waitForElementPresent(By by) throws Exception {
		return waitForElementPresent(by, getDefaultExplicitWait());
	}

	public WebElement waitForElementPresent(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return waitForElementPresent(by, timeout);
	}

	public WebElement waitForElementPresent(By by, int timeout) throws Exception {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			HtmlReporter.fail("The element : [" + by.toString() + "] isn't present", e);
			throw e;
		}
	}
	
	public WebElement waitForElementToBeClickable(String element) throws Exception {
		return waitForElementToBeClickable(element, getDefaultExplicitWait());
	}

	public WebElement waitForElementToBeClickable(By by) throws Exception {
		return waitForElementToBeClickable(by, getDefaultExplicitWait());
	}

	public WebElement waitForElementToBeClickable(String element, int timeout) throws Exception {
		By by = getElementBy(element);
		return waitForElementToBeClickable(by, timeout);
	}

	public WebElement waitForElementToBeClickable(By by, int timeout) throws Exception {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			return wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			HtmlReporter.fail("The element : [" + by.toString() + "] isn't able to click", e);
			throw e;
		}
	}

	/**
	 * Checking a web element is present or not
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return True if the element is present, False if the element is not present
	 * @throws Exception
	 */
	public boolean isElementPresent(String element) {
		try {
			findElement(element);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Open url in new tab
	 * 
	 * @param url
	 *            Url to of new tab *
	 * @throws Exception
	 */
	public void openNewTab(String url) throws Exception {
		try {
			// Open tab 2 using CTRL + t keys.
			driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
			// Open URL In 2nd tab.
			driver.get(url);
			// Switch to current selected tab's content.
			driver.switchTo().defaultContent();
			HtmlReporter.pass("Opened new tab with url [" + url + "]");
		} catch (Exception e) {
			HtmlReporter.pass("Failed to open new tab with url [" + url + "]");
			throw (e);

		}
	}

	/**
	 * Verify the present of an alert
	 * 
	 * @return
	 */
	public boolean isAlertPresent(int timeout) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.alertIsPresent());
			HtmlReporter.pass("Alert is presented in expected time");
			return true;
		} catch (Exception Ex) {
			HtmlReporter.pass("Alert is presented in expected time");
			return false;
		}
	}

	/**
	 * Accept an Alert
	 * 
	 * @throws Exception
	 */
	public void acceptAlert() throws Exception {
		try {
			if (isAlertPresent(5)) {
				driver.switchTo().alert().accept();
				HtmlReporter.pass("Accept Alert");
			}
		} catch (Exception e) {
			HtmlReporter.fail("Can't accept Alert", e);
			throw (e);
		}
	}

	/**
	 * Hide an element by javascript
	 * 
	 * @param by
	 *            By locator
	 * @throws Exception
	 */
	public void hideElement(By by) throws Exception {
		try {
			WebElement element = driver.findElement(by);
			executeJavascript("arguments[0].style.visibility='hidden'", element);
			waitForPageLoad();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Perform mouse hover action
	 * 
	 * @param by
	 *            The By locator object of element
	 * @param elementName
	 *            Name of element used to write
	 * @return
	 * @throws Exception
	 */

	public void mouseHover(String element) throws Exception {
		By by = getElementBy(element);
		mouseHover(by);
	}
	
	public void mouseHover(By by) throws Exception {
		try {
			WebElement e = waitForElementDisplayed(by);
			Actions action = new Actions(driver);
			action.moveToElement(e).perform();
			HtmlReporter.pass("mouseHover [" + by.toString() + "] successfully");
		} catch (Exception e) {
			HtmlReporter.fail("mouseHover [" + by.toString() + "] failed", e);
			throw e;
		}
	}

	/**
	 * Scroll the web page to the element
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void scrollIntoView(String element) throws Exception {
		By by = getElementBy(element);
		scrollIntoView(by);
	}
	
	public void scrollIntoView(By by) throws Exception {
		try {
			executeJavascript("arguments[0].scrollIntoView(true);", findElement(by));
		} catch (Exception e) {
			throw (e);

		}
	}
	
	public void scrollElementIntoMiddle(String element) throws Exception {
		By by = getElementBy(element);
		scrollElementIntoMiddle(by);
	}
	
	public void scrollElementIntoMiddle(By by) throws Exception {
		String script = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
				+ "var elementTop = arguments[0].getBoundingClientRect().top;"
				+ "window.scrollBy(0, elementTop-(viewPortHeight/2));";

		executeJavascript(script, findElement(by));
	}

	public void cleanBrowser() throws Exception {

		try {
			driver.manage().deleteAllCookies();
			((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
			((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
			Thread.sleep(3000);
			HtmlReporter.pass("Clean the browser!!!");
		} catch (Exception e) {
			HtmlReporter.fail("Cannot clean browser!!!", e);
			throw (e);
		}
	}

	/**
	 * Get Browser Type
	 * 
	 * @return Browser Type
	 */
	public String getBrowserInfor(String infor) {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();

		String browserName = cap.getBrowserName();
		String browserVersion = (String) cap.getCapability("browserVersion");
		String osName = Platform.fromString((String) cap.getCapability("platformName")).name().toLowerCase();

		switch (infor) {
		case "browserName":
			return browserName;
		case "browserVersion":
			return browserVersion;
		case "platform":
			return osName;
		default:
			return "unknown information";
		}
	}

	/**
	 * This method is used to capture a screenshot then write to the TestNG Logger
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot() {

		String failureImageFileName = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss.SSS")
				.format(new GregorianCalendar().getTime()) + ".jpg";
		try {

			if (driver != null) {
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String screenShotDirector = HtmlReportDirectory.getScreenshotFolder();
				FileUtils.copyFile(scrFile, new File(screenShotDirector + File.separator + failureImageFileName));
				return screenShotDirector + File.separator + failureImageFileName;
			}
			return "";
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This method is used to capture a screenshot
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot(String filename) throws Exception {

		String screenShotDirector = HtmlReportDirectory.getScreenshotFolder();
		String screenshotFile = FilePaths.correctPath(screenShotDirector + filename);

		try {
			if (driver != null) {
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(scrFile, new File(screenshotFile));
				return screenshotFile;

			} else {
				return "";
			}
		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * This method is used to capture a screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir) throws Exception {
		fileDir = FilePaths.correctPath(fileDir);
		try {

			if (driver != null) {
				Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver);
				ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
			} else {
				fileDir = "";
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
		return fileDir;
	}

	/**
	 * This method is used to capture an element's screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir, String element) throws Exception {
		fileDir = FilePaths.correctPath(fileDir);
		try {

			if (driver != null) {
				WebElement e = findElement(element);
				Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver, e);
				ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			throw e;
		}
		return fileDir;

	}

	/**
	 * To compare the layout of a web page with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	/*
	 * public void compareScreenshot(String filename) throws Exception { String
	 * screenshotFileName = filename + "." +
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"); String baseLineImage =
	 * HtmlReporter.strBaseLineScreenshotFolder + screenshotFileName; String
	 * actualImage = HtmlReporter.strActualScreenshotFolder + screenshotFileName; //
	 * String diffImage = Common.strWebDiffScreenshotFolder + screenshotFileName;
	 * try { waitForPageLoad(); if (!Common.pathExist(baseLineImage)) {
	 * takeScreenshotWithAshot(baseLineImage); } else {
	 * takeScreenshotWithAshot(actualImage); ImageCompare imageComparitor = new
	 * ImageCompare(); BufferedImage diffBuff =
	 * imageComparitor.diffImages(baseLineImage, actualImage, 30, 10); if (diffBuff
	 * == null) { Log.info("The actual screenshot of page [" + filename +
	 * "] matches with the baseline"); } else {
	 * Log.error("The actual screenshot of page [" + filename +
	 * "] doesn't match with the baseline"); ImageIO.write(diffBuff,
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"), new
	 * File(HtmlReporter.strDiffScreenshotFolder, screenshotFileName)); throw new
	 * Exception("The actual screenshot doesn't match with the baseline"); } } }
	 * catch (Exception e) { throw e; } }
	 */

	/**
	 * To compare the layout of a web element with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	/*
	 * public void compareScreenshot(String filename, String element) throws
	 * Exception { String screenshotFileName = filename + "." +
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"); String baseLineImage =
	 * HtmlReporter.strBaseLineScreenshotFolder + screenshotFileName; String
	 * actualImage = HtmlReporter.strActualScreenshotFolder + screenshotFileName; //
	 * String diffImage = Common.strWebDiffScreenshotFolder + screenshotFileName;
	 * try { waitForPageLoad(); if (!Common.pathExist(baseLineImage)) {
	 * takeScreenshotWithAshot(baseLineImage, element); } else {
	 * takeScreenshotWithAshot(actualImage, element); ImageCompare imageComparitor =
	 * new ImageCompare(); BufferedImage diffBuff =
	 * imageComparitor.diffImages(baseLineImage, actualImage, 30, 10); if (diffBuff
	 * == null) { Log.info("The actual screenshot of element [" + filename +
	 * "] matches with the baseline"); } else {
	 * Log.error("The actual screenshot of element [" + filename +
	 * "] doesn't match with the baseline"); ImageIO.write(diffBuff,
	 * Common.constants.getProperty("SCREENSHOT_FORMAT"), new
	 * File(HtmlReporter.strDiffScreenshotFolder, screenshotFileName)); throw new
	 * Exception( "The actual screenshot of element [" + filename +
	 * "] doesn't match with the baseline"); } } } catch (Exception e) { throw e; }
	 * }
	 */

	public void switchWindowByTitle(String title) throws Exception {
		try {
			boolean switched = false;
			Set<String> allWindows = driver.getWindowHandles();
			for (String windowHandle : allWindows) {
				driver.switchTo().window(windowHandle);
				if (driver.getTitle().equals(title)) {
					HtmlReporter.pass("Switched to Window with title:" + title);
					switched = true;
					break;
				}
			}
			if (!switched) {
				HtmlReporter.fail("Cannot find any window with title: " + title + " to switch");
			}
		} catch (Exception e) {
			HtmlReporter.fail("Switched to Window with title:" + title);
		}
	}

	// only use for browserstack
	public void markTestStatus(String status, String reason) throws Exception {
		if (false) {
			reason = reason.replace("\"", "'");
			String script = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""
					+ status + "\", \"reason\": \"" + reason + "\"}}";
			executeJavascript(script);
		}
	}

	public void wait(int second) throws InterruptedException {
		Thread.sleep(second * 1000);
	}

}
