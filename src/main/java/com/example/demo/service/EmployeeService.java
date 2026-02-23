package com.example.demo.service;

import com.example.demo.repository.EmployeeRepository;
import com.example.demo.dto.EmployeeSearchCondition;
import com.example.demo.dto.EmployeeSearchResult;
import com.example.demo.entity.Employee;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 社員に関する業務処理を担当するサービスクラス。
 *
 * <p>
 *   Controller から呼び出され、
 *   社員情報の登録・更新・削除などの業務ロジックを管理する。
 * </p>
 */
@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  /**
   * フリーワード入力を解析し、
   * EmployeeSearchCondition に変換する。
   *
   * <p>
   *   入力された文字列をスペースで分割し、
   *   部署・等級・資格名・氏名などに分類する。
   *   現在は文字列ルールによる暫定判定を行っている。
   * </p>
   *
   * @param keyword 検索キーワード
   * @return 解析後の検索結果
   */
  public EmployeeSearchCondition buildConditionFromKeyword(String keyword) {
    EmployeeSearchCondition condition = new EmployeeSearchCondition();
      if (keyword == null || keyword.isBlank()) {
    return condition;
  }
    String[] tokens = keyword.split("[\\s　]+"); // 半角&全角スペース対応
    for (String token : tokens) {
      if (looksLikeDepartment(token)) {
        condition.setDepartment(token);
      } else if (looksLikeGrade(token)) {
        condition.setGrade(token);
      } else if (looksLikeQualification(token)) {
        condition.setQualificationName(token);
      } else {
        condition.setKeyword(token); // 名前など
      }
    }
    return condition;
}

  public Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber) {
    //return employeeRepository.findByEmployeeNumber(employeeNumber);
    return employeeRepository.findWithQualificationsByEmployeeNumber(employeeNumber);
  }

  // TODO 将来的に部署マスタで判定する
  private boolean looksLikeDepartment(String token) {
    if (token.length() <= 1) {
      return false;
    }
    return token.contains("工事")
        || token.contains("営業");
  }

  // TODO 将来的に等級マスタで判定する
  private boolean looksLikeGrade(String token) {
    return token.equals("主任")
        || token.equals("係長")
        || token.equals("課員");
  }

  // TODO 将来的に資格マスタで判定する
  private boolean looksLikeQualification(String token) {
    return token.contains("情報")
        || token.contains("基本")
        || token.contains("応用")
        || token.contains("IT")
        || token.contains("パスポート");
  }

  /**
   * 検索条件に基づいて社員を検索する。
   *
   * <p>
   *   資格条件が指定されている場合は、
   *   Service層で「資格を持っている社員」と
   *   「資格を持っていない社員」に分類して返す。
   * </p>
   *
   * <p>
   *   検索条件がない場合は、
   *   検索結果をすべて「資格を持っている社員」として扱う。
   * </p>
   *
   * @param condition 検索条件
   * @return 資格あり／なしに分類された検索結果
   */
  public EmployeeSearchResult search(EmployeeSearchCondition condition) {

    // ① 資格を除いた条件で基準となる社員一覧を検索）
    // 資格条件は Repository では使用せず、 Service側 で判定する
    List<Employee> baseEmployees =
        employeeRepository.searchByCondition(
            condition.getKeyword(),
            condition.getDepartment(),
            condition.getGrade());

    // ② 資格条件がない場合
    if (condition.getQualificationName() == null) {
      return new EmployeeSearchResult(baseEmployees, List.of());
    }

    // ③ 資格条件がある場合：Serviceで分割
    String target = condition.getQualificationName();

    List<Employee> has = new ArrayList<>();
    List<Employee> hasNot = new ArrayList<>();

    for (Employee e : baseEmployees) {
      boolean match = e.getQualifications().stream()
          .anyMatch(q -> containsIgnoreNull(q.getQualificationName(), target));

      if (match) {
        has.add(e);
      } else {
        hasNot.add(e);
      }
    }

    return new EmployeeSearchResult(has, hasNot);
  }

  private boolean containsIgnoreNull(String value, String target) {
    return value != null && value.contains(target);
  }

  /**
   * 社員を新規登録する。
   *
   * <p>
   *   Controller から受け取った Employee をデータベースに登録する。
   * </p>
   *
   * @param employee 登録対象の社員
   */
  public void createEmployee(Employee employee){
    employeeRepository.save(employee);
  }

  /**
   * 社員を論理削除する。
   *
   * <p>
   *   deleted フラグを true に設定することで、
   *   データは残したまま一覧・検索対象外とする。
   * </p>
   *
   * @param employeeNumber 社員番号
   */
  public void deletedEmployee(String employeeNumber) {
    Employee employee = employeeRepository
        .findByEmployeeNumberAndDeletedFalse(employeeNumber)
        .orElseThrow(() -> new IllegalArgumentException("社員が存在しません"));

    employee.setDeleted(true);

    employeeRepository.save(employee);
  }
}