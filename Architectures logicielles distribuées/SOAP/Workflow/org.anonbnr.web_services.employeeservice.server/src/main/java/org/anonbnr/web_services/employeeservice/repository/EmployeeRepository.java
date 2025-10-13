package org.anonbnr.web_services.employeeservice.repository;

import org.anonbnr.web_services.employeeservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de repository pour gérer les employés
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
