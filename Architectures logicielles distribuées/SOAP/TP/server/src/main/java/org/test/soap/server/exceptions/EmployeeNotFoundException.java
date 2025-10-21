package org.anonbnr.web_services.employeeservice.exceptions;

public class EmployeeNotFoundException extends EmployeeException {
	public EmployeeNotFoundException() {
		
	}
	
	public EmployeeNotFoundException(String message) {
		super(message);
	}
}
