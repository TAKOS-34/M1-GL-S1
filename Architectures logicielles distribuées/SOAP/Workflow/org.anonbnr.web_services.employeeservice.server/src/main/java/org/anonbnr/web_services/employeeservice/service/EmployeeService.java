package org.anonbnr.web_services.employeeservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.anonbnr.web_services.employeeservice.exceptions.EmployeeAlreadyExistsException;
import org.anonbnr.web_services.employeeservice.exceptions.EmployeeNotFoundException;
import org.anonbnr.web_services.employeeservice.model.Employee;

@WebService
public interface EmployeeService {
    
    @WebMethod
    int count(); // Compter le nombre d'employés
    
    @WebMethod
    List<Employee> getEmployees(); // Récupérer la liste des employés
    
    @WebMethod
    Employee addEmployee(String name) throws EmployeeAlreadyExistsException; // Ajouter un employé
    
    @WebMethod
    Employee getEmployee(int id) throws EmployeeNotFoundException; // Récupérer un employé par identifiant
    
    @WebMethod
    Employee updateEmployee(int id, String name) throws EmployeeNotFoundException; // Modifier un employé
    
    @WebMethod
    boolean deleteEmployee(int id) throws EmployeeNotFoundException; // Supprimer un employé
}