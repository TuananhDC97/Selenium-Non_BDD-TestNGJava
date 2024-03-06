package com.nashtech.automation.tesnghelper;

import java.io.File;
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
import org.testng.annotations.Parameters;

import com.nashtech.automation.log.Log;
import com.nashtech.automation.report.HtmlReportDirectory;
import com.nashtech.automation.report.HtmlReporter;

public class APITestBaseSetup {

	@BeforeSuite
	public void beforeSuite() throws Exception {
		// Init Report Directory
		HtmlReportDirectory.initReportDirectory();
	}

	@BeforeTest
	@Parameters()
	public void beforeTest() throws Exception {
		// Create html report by platform, device name and os version
		String reportFilePath = HtmlReportDirectory.getReportFolder() + File.separator + "Report.html";
		HtmlReporter.setReporter(reportFilePath);
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		Log.startTestCase(this.getClass().getName());
		HtmlReporter.currentTest = this.getClass().getSimpleName();
		HtmlReporter.createTest(this.getClass().getSimpleName(), "");
	}

	@BeforeMethod
	public void beforeMethod(Method method, Object[] listParameter) throws Exception {
		String methodName = listParameter.length != 0 ? method.getName() + " - " + ArrayUtils.toString(listParameter) : method.getName();
		HtmlReporter.createNode(this.getClass().getSimpleName(), methodName, "");

		Log.info("+++++++++");
		Log.info("+++++++++");
		Log.info("+++++++++ Start testing: " + methodName + " ++++++++++++++");
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {
		String mess = "";
		try {
			switch (result.getStatus()) {
			case ITestResult.SUCCESS:
				mess = String.format("The test [%s] is PASSED", result.getName());
				break;
			case ITestResult.SKIP:
				mess = String.format("The test [%s] is SKIPPED - n%s", result.getName(),
						result.getThrowable().getMessage());
				break;
			case ITestResult.FAILURE:
				mess = String.format("The test [%s] is FAILED - %s", result.getName(),
						result.getThrowable().getMessage());
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
		Log.endTestCase(this.getClass().getName());
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() throws Exception {
		HtmlReporter.flush();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
	}
}
