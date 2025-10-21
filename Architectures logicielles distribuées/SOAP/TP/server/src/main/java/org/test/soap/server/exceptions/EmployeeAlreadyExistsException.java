package org.test.soap.server.exceptions;

public class EmployeeAlreadyExistsException extends EmployeeException {
	public EmployeeAlreadyExistsException() {}
	
	public EmployeeAlreadyExistsException(String message) {
		super(message);
	}
}
