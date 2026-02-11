package com.example.demo.dto;

/**
 * 社員検索に使用する検索条件DTO。
 *
 * <p>
 *   フリーワード解析後の、検索条件を保持する。
 *   各フィールドが null の場合は、その条件は未指定とみなす。
 * </p>
 */
public class EmployeeSearchCondition {

  private String keyword;
  private String department;
  private String grade;
  private String qualificationName;

  /**
   * 検索条件がすべて未指定かどうかを判定する。
   *
   * @return すべて未指定の場合 true
   */
  public boolean isEmpty() {
    return isBlank(keyword)
        && isBlank(department)
        && isBlank(grade)
        && isBlank(qualificationName);
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  // --- getter / setter ---

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getQualificationName() {
    return qualificationName;
  }

  public void setQualificationName(String qualificationName) {
    this.qualificationName = normalize(qualificationName);
  }

  private String normalize(String value) {
    return (value == null || value.isBlank()) ? null : value;
  }
}