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
    if (condition.isEmpty()) {
      return employeeRepository.findAll(); //Repositoryにすることで、findAll()でJpaRepositoryが用意
    }

    System.out.println("keyword=" + condition.getKeyword());
    System.out.println("department=" + condition.getDepartment());
    System.out.println("grade=" + condition.getGrade());
    System.out.println("qualification=" + condition.getQualificationName());

    return employeeRepository.searchByCondition(
        condition.getKeyword(),
        condition.getDepartment(),
        condition.getGrade(),
        condition.getQualificationName()
    );
  }

  // フリーワードを解析して、 Condition を作る
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
    return employeeRepository.findByEmployeeNumber(employeeNumber);
  }

  // キーワード判定ロジック
  private boolean looksLikeDepartment(String token) {
    if (token.length() <= 1) {
      return false;
    }
    return token.contains("工事")
        || token.contains("営業");
  }

  private boolean looksLikeGrade(String token) {
    return token.equals("主任")
        || token.equals("係長")
        || token.equals("課員");
  }

  private boolean looksLikeQualification(String token) {
    return token.contains("情報")
        || token.contains("基本")
        || token.contains("応用")
        || token.contains("IT")
        || token.contains("パスポート");
  }

  /**
   * フリーワード入力で社員を検索する。
   * → フリーワード入力を解析し、検索条件DTOを構築して社員検索を行う。
   * <p>
   * keyword が null の場合は全社員を返し、
   * keyword が指定されている場合は社員名に部分一致する社員を返す。
   * 入力された keyword を以下の項目に対して OR 条件で検索する。
   * → 入力されたキーワードは、以下の項目に分類され、条件に応じて AND 検索される。
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