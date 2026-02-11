package com.example.demo.dto;

import com.example.demo.entity.Employee;
import java.util.List;

/**
 * 社員の検索結果を保持するDTO。
 *
 * <p>
 *   検索結果を「資格を持っている社員」と
 *   「資格を持っていない社員」に分類して保持する。
 * </p>
 */
public class EmployeeSearchResult {

  private final List<Employee> hasQualification;
  private final List<Employee> hasNotQualification;

  public EmployeeSearchResult(
      List<Employee> hasQualification,
      List<Employee> hasNotQualification) {

    this.hasQualification = hasQualification;
    this.hasNotQualification = hasNotQualification;
  }

  public List<Employee> getHasQualification() {
    return hasQualification;
  }

  public List<Employee> getHasNotQualification() {
    return hasNotQualification;
  }
}