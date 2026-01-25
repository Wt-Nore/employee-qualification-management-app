package com.example.demo.Repository;

import com.example.demo.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository
    extends JpaRepository<Employee, Long> {
  List<Employee> findByNameContaining(String keyword);
  Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber);
  Optional<Employee> findByEmployeeNumber(String employeeNumber);
}
