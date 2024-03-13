# Project Name #

Study Project: Automation Testing with Selenium using Java

# Author #

Le Tuan Anh

# Completion Period # 

October 2022 - January 2023

 # Description # 

This project implements automation testing for DemoQA.com using Selenium WebDriver with Java. It leverages the Page Object Model (POM) for organized and maintainable test code, along with TestNG for managing test cases and generating reports, and ExtentReports for comprehensive test reporting.

 # Technologies Used # 

Selenium WebDriver: Open-source tool for browser automation in Java

Data-Driven Framework: Technique for executing tests with multiple data sets, promoting efficient testing and reusability (e.g., using CSV files, Excel sheets, or database connections).

Java: General-purpose programming language for test scripts

Page Object Model (POM): Design pattern for structuring test code

TestNG: Java testing framework for managing test cases and generating reports

ExtentReports: Library for creating detailed and visual test reports

 # Scope # 

The project focuses on automated testing of both UI (User Interface) elements and API endpoints of DemoQA.com. This allows for comprehensive validation of the application's behavior.

 # Getting Started # 

**1. Prerequisites:**  Ensure you have Node.js and npm (Node Package Manager) installed on your system. You can download them from the official Node.js website (https://nodejs.org/en).

**2. Clone the Repository:** Use Git to clone this repository locally:

`git clone https://github.com/TuananhDC97/Selenium-Non_BDD-TestNGJava.git`

**3. Set Up Dependencies:**

Add the required Selenium and TestNG libraries to your project's build path (instructions specific to your IDE).

**4. Download WebDriver:**

Download the appropriate WebDriver for your browser (e.g., ChromeDriver for Chrome) from https://www.selenium.dev/documentation/webdriver/. Place it in a location accessible to your project.

# Running the Tests #

**1. Execute Tests:**
From your IDE, run the main test class or suite.

**2. View Reports:**
ExtentReports will typically generate an HTML report after execution, providing detailed information on test results.

**3. Folder Structure**

src/main/java/: Contains Java source code for test classes and page objects (modify as needed).

/src/test/java/com/nashtech/automation/tests/: Your package structure for test classes.

Your test classes (e.g., LoginTest.java, ApiTest.java).

/src/test/java/com/nashtech/automation/pages/: Stores page object classes representing UI components.

Your page object classes (e.g., LoginPage.java, ApiPage.java).

src/test/resources/: Holds any test data or resources required by your tests (optional).

/src/main/resources/config/extent-report/extent-config.xml: Configuration file for ExtentReports (optional).

**Page Object Model (POM)**

This project follows the POM for improved organization and maintainability. Test code interacts with page objects representing specific UI components, promoting separation of concerns. Refer to the ``/src/test/java/com/nashtech/automation/pages/`` directory for examples of POM implementation in Java with Selenium.

**Testing APIs**

The project demonstrates testing DemoQA.com's API endpoints using libraries like REST Assured or custom HTTP request methods within your test classes (refer to relevant test files).

**Further Enhancements**

This README.md file provides a basic guide to get you started with the project. Feel free to customize and adapt it further based on your specific project structure and testing needs.
