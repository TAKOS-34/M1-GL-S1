package org.test.soap.server.repository;
import org.test.soap.server.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Interface de repository pour gérer les employés
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {}