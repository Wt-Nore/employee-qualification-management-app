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
  //資格
  Qualification q1 = new Qualification("Q001", "基本情報技術者");
  Qualification q2 = new Qualification("Q002", "応用情報技術者");
  Qualification q3 = new Qualification("Q003","ITパスポート");

  // 社員一覧

  private final List<Employee> employees = List.of(
      new Employee("E001", "山田太郎", "工事部","主任",List.of()),
      new Employee("E002", "佐藤桂子", "営業部", "係長",List.of(q1, q2,q3)),
      new Employee("E003", "鈴木一郎", "工事部", "係長",List.of(q1,q3))
  );


  private final EmployeeRepository employeeRepository;
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }


 // public Optional<Employee> findEmployee(){
 //   return employees.stream().findFirst();
 // }

  //　社員を全員取得する　社員一覧画面用
  public List<Employee> getAllEmployees() {
    // return employees; //Repository に移したことで、終了
    return employeeRepository.findAll(); //Repisitoryにすることで、findAll()でJpaRepositoryが用意
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

  //　資格IDで社員を複数人探す　社員詳細画面用
  public List<Employee> findEmployeesByQualificationId(String qualificationId) {
    return employees.stream()
        .filter(employee ->
            employee.getQualifications().stream()
                .anyMatch(q -> q.getQualificationId().equals(qualificationId))
        )
        .toList();
  }

  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber) {
    return employeeRepository.findByEmployeeNumber(employeeNumber);
  }

  //　資格を取得する　資格一覧画面用
  public List<Qualification> getAllQualifications(){
    return List.of(q1,q2,q3);
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
    return employeeRepository.findByNameContaining(keyword);
    }
  }

