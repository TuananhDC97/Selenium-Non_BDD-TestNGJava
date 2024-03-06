package com.nashtech.automation.setup;

import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.nashtech.automation.log.Log;
import com.nashtech.automation.tesnghelper.APITestBaseSetup;
import com.nashtech.automation.tesnghelper.WebTestBaseSetup;
import com.nashtech.automation.utility.ExcelHelper;

public class APITestSetup extends APITestBaseSetup {

	@BeforeSuite
	public void setupSuite() throws Exception {
	}

	@BeforeTest
	public void setupTest() throws Exception {
		EnvironmentVariables.init();
	}

	@BeforeClass
	public void setupClass() throws Exception {
		
	}

	@BeforeMethod
	public void setupBefore(Method method, Object[] listParameter) throws Exception {
	}

	@AfterMethod(alwaysRun = true)
	public void teardownMethod(ITestResult result) throws Exception {
	}

	@AfterClass(alwaysRun = true)
	public void teardownClass() throws Exception {
	}

	@AfterTest(alwaysRun = true)
	public void teardownTest() throws Exception {
	}

	@AfterSuite(alwaysRun = true)
	public void teardownSuite() throws Exception {
	}
}
