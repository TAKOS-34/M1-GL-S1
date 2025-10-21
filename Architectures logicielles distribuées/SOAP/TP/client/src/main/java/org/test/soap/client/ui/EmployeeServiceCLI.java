package org.test.soap.client.ui;
import org.test.soap.client.generated.Employee;
import org.test.soap.client.generated.EmployeeAlreadyExistsException_Exception;
import org.test.soap.client.generated.EmployeeNotFoundException_Exception;
import org.test.soap.client.generated.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Command Line Interface (CLI) for the EmployeeService.
 */
@Component
public class EmployeeServiceCLI implements CommandLineRunner {

	@Autowired
	private EmployeeService employeeService;

	@Override
	 public void run(String... args) throws Exception {
	     // Example operations
	     System.out.println("Number of employees: " + employeeService.count());
	     
	     // Add a new employee
	     try {
	         employeeService.addEmployee("John Doe");
	         System.out.println("Added employee: John Doe");
	     } catch (EmployeeAlreadyExistsException_Exception e) {
	         System.err.println("Error: " + e.getMessage());
	     }
	
	     // Get all employees
	     employeeService.getEmployees().forEach(emp -> displayEmployee(emp));
	
	     // Get employee by ID
	     try {
	         Employee employee = employeeService.getEmployee(1);
	         displayEmployee(employee);
	     } catch (EmployeeNotFoundException_Exception e) {
	         System.err.println("Error: " + e.getMessage());
	     }
	
		 try {
	     // Update employee
	     employeeService.updateEmployee(1, "John Smith");
	     System.out.println("Updated employee: John Smith");
		 } catch (EmployeeNotFoundException_Exception e) {
	         System.err.println("Error: " + e.getMessage());
	     }
	
		 try {
	     // Remove employee
	     employeeService.deleteEmployee(6);
	     System.out.println("Deleted employee with ID 1");
	 	} catch (EmployeeNotFoundException_Exception e) {
	         System.err.println("Error: " + e.getMessage());
	     }
		}
	private void displayEmployee(Employee employee) {
		System.out.println("ID: " + employee.getId() + ", Name: " + employee.getName());
	}
}