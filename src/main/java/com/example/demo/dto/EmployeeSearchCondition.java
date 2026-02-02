package com.example.demo.dto;

public class EmployeeSearchCondition {

  private String keyword;
  private String department;
  private String grade;
  private String qualificationName;

  public boolean isEmpty() {
    return keyword == null
        && department == null
        && grade == null
        && qualificationName == null;
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
    this.qualificationName = qualificationName;
  }
}