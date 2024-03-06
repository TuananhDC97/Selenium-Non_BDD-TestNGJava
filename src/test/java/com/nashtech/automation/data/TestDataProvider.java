package com.nashtech.automation.data;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

public class TestDataProvider {
	
	@DataProvider(name="1")
	private  Object[] displayEmployeesData() {
		List<Employee> employees = new ArrayList<>();
		String[] keys = {"First Name","Last Name", "Age", "Email", "Salary", "Department"};
		String[] values1 = {"Cierra","Vega","39","cierra@example.com","10000","Insurance"};
		String[] values2 = {"Alden","Cantrell","45","alden@example.com","12000","Compliance"};
		String[] values3 = {"Kierra","Gentry","29","kierra@example.com","2000","Legal"};
		employees.add(new Employee(keys,values1));
		employees.add(new Employee(keys,values2));
		employees.add(new Employee(keys,values3));
		return new Object[] {employees};
	}
	
	@DataProvider(name="2")
	private Object[] createEmployeeDP() {
		String[] keys = {"First Name","Last Name", "Age", "Email", "Salary", "Department"};
		String[] values = {"Hien","Nguyen","25","hien.nguyen@example.com","10000","NashTech"};
		String[] values2 = {"Hai","Nguyen","26","hien.nguyen@example.com","15000","NashTech"};
		Employee newEmployee = new Employee(keys,values);
		Employee newEmployee2 = new Employee(keys,values2);
		return new Object[] {newEmployee,newEmployee2};
	}
	
	@DataProvider(name="3")
	private Object[][] updateAndDeleteEmployeeDP() {
		String employeeName = "Kierra";
		String[] keys = {"First Name","Last Name", "Age", "Email", "Salary", "Department"};
		String[] values = {"Kierra Updated","Gentry Updated","30","kierra.updated@gmail.com","2500","Legal Updated"};
		Employee updateEmployee = new Employee(keys,values);
		return new Object[][]{{employeeName,updateEmployee}};
	}
}