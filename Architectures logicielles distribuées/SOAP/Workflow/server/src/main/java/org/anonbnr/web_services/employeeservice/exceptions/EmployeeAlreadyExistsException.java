package org.anonbnr.web_services.employeeservice.exceptions;

public class EmployeeAlreadyExistsException extends EmployeeException {
	public EmployeeAlreadyExistsException() {
		
	}
	
	public EmployeeAlreadyExistsException(String message) {
		super(message);
	}
}
