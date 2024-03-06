package com.nashtech.automation.report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import com.nashtech.automation.utility.FilePaths;

public class HtmlReportDirectory {

	private static String REPORT_ROOT_FOLDER;
	private static String REPORT_FOLDER;
	private static String SCREENSHOT_FOLDER;
	private static String REPORT_FILE_PATH;

	public static String getRootProject() {
		return System.getProperty("user.dir");
	}

	/**
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getResourcePath(String filename) throws Exception {
		try {
			URL rsc = HtmlReportDirectory.class.getResource(filename);
			return Paths.get(rsc.toURI()).toFile().getAbsolutePath();
		} catch (Exception e) {
			throw e;
		}
	}

	public static void initReportDirectory() throws IOException {
		REPORT_ROOT_FOLDER = getRootProject() + File.separator + "Reports";
		REPORT_FOLDER = REPORT_ROOT_FOLDER + File.separator + "Latest Report";
		checkExistReportAndReName(REPORT_FOLDER);
		SCREENSHOT_FOLDER = REPORT_FOLDER + File.separator + "Screenshots";
		REPORT_FILE_PATH = REPORT_FOLDER + File.separator + "Report.html";
		FilePaths.createDirectory(REPORT_ROOT_FOLDER);
		FilePaths.createDirectory(REPORT_FOLDER);
		FilePaths.createDirectory(SCREENSHOT_FOLDER);
	}

	private static void checkExistReportAndReName(String path) throws IOException {
		if (FilePaths.pathExist(path)) {
			File oldReport = new File(path);
			BasicFileAttributes oldReportAttribute = Files.readAttributes(oldReport.toPath(),
					BasicFileAttributes.class);
			File renameOldReport = new File(getRootProject() + File.separator + "Reports" + File.separator + "Report_"
					+ oldReportAttribute.creationTime().toString().replace(":", "."));
			oldReport.renameTo(renameOldReport);
		}
	}

	/**
	 * @return
	 */
	public static String getReportFolder() {
		return REPORT_FOLDER;
	}

	/**
	 * @return
	 */
	public static String getScreenshotFolder() {
		return SCREENSHOT_FOLDER;
	}

	/**
	 * @return
	 */
	public static String getReportFilePath() {
		return REPORT_FILE_PATH;
	}

}
