package com.example.demo.service;

import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.dto.EmployeeSearchCondition;
import com.example.demo.entity.Employee;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  //　社員を全員取得する　社員一覧画面用
  public List<Employee> search(EmployeeSearchCondition condition) {
    if (condition.getKeyword() == null ||
        condition.getKeyword().isBlank()) {
      return employeeRepository.findAll(); //Repositoryにすることで、findAll()でJpaRepositoryが用意
    }
    return employeeRepository.searchByKeyword(
        condition.getKeyword()
    );
  }

  /** //　社員番号で社員を一人探す　社員詳細画面用
   // Service が List を検索する Repository に移したことで終了
  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber){
    return employees.stream()
        .filter(e -> e.getEmployeeNumber().equals(employeeNumber))
        .findFirst();
  }
   */

  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber) {
    return employeeRepository.findByEmployeeNumber(employeeNumber);
  }

  /**
   * フリーワード入力で社員を検索する。
   * <p>
   * keyword が null の場合は全社員を返し、
   * keyword が指定されている場合は社員名に部分一致する社員を返す。
   * 入力された keyword を以下の項目に対して OR 条件で検索する。
   * <ul>
   *   li>社員名</li>
   *  *   <li>部署</li>
   *  *   <li>等級</li>
   *  *   <li>社員番号</li>
   *  *   <li>資格名</li>
   * </ul></>
   * </p>
   *
   * @param keyword 検索キーワード
   * @return 検索条件に一致する社員一覧
   */
}