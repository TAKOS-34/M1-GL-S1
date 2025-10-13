package org.anonbnr.web_services.employeeservice.service;

import java.util.List;

import javax.jws.WebService;

import org.anonbnr.web_services.employeeservice.exceptions.EmployeeAlreadyExistsException;
import org.anonbnr.web_services.employeeservice.exceptions.EmployeeNotFoundException;
import org.anonbnr.web_services.employeeservice.model.Employee;
import org.anonbnr.web_services.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

@WebService(endpointInterface = "org.anonbnr.web_services.employeeservice.service.EmployeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository; // Injection de dépendance du repository

    @Override
    public int count() {
        return (int) repository.count(); // Compter les employés
    }

    @Override
    public List<Employee> getEmployees() {
        return repository.findAll(); // Récupérer la liste des employés
    }

    @Override
    public Employee addEmployee(String name) throws EmployeeAlreadyExistsException {
        // Vérification d'existence d'un employé par son nom
        if (repository.findAll().stream().anyMatch(e -> e.getName().equals(name))) {
            throw new EmployeeAlreadyExistsException("Error: An employee with name " + name + " already exists");
        }
        return repository.save(new Employee(name)); // Ajouter l'employé
    }

    @Override
    public Employee getEmployee(int id) throws EmployeeNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Error: No employee with ID " + id + " exists")); // Récupérer un employé par identifiant
    }

    @Override
    public Employee updateEmployee(int id, String name) throws EmployeeNotFoundException {
        Employee employee = getEmployee(id);
        employee.setName(name); // Mettre à jour le nom
        return repository.save(employee); // Sauvegarder les modifications
    }

    @Override
    public boolean deleteEmployee(int id) throws EmployeeNotFoundException {
        Employee employee = getEmployee(id);
        repository.delete(employee); // Supprimer l'employé
        return true; // Indiquer la suppression réussie
    }
}
