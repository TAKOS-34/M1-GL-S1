package org.test.soap.server.exceptions;

public class EmployeeNotFoundException extends EmployeeException {
	public EmployeeNotFoundException() {}
	
	public EmployeeNotFoundException(String message) {
		super(message);
	}
}
