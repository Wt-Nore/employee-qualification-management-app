package com.example.demo.service;

import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Qualification;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  //　社員を全員取得する　社員一覧画面用
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll(); //Repositoryにすることで、findAll()でJpaRepositoryが用意
  }

  /** //　社員番号で社員を一人探す　社員詳細画面用
   // Service が List を検索する Repository に移したことで終了
  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber){
    return employees.stream()
        .filter(e -> e.getEmployeeNumber().equals(employeeNumber))
        .findFirst();
  }
  **/

  public Optional<Employee> findEmployeeById(Long id) {
    return employeeRepository.findById(id);
  }

  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber) {
    return employeeRepository.findByEmployeeNumber(employeeNumber);
  }
  
  /**
   * 社員を検索する。
   * <p>
   * keyword が null の場合は全社員を返し、
   * keyword が指定されている場合は社員名に部分一致する社員を返す。
   * </p>
   *
   * @param keyword 検索キーワード
   * @return 検索条件に一致する社員一覧
   */
  public List<Employee> search(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return employeeRepository.findAll();
    }
    return employeeRepository.searchByKeyword(keyword);
    }
  }

