package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * 一人の社員を表すエンティティクラス。
 *
 * <p>
 *   社員番号・氏名・所属部署・等級を保持する。
 *   Qualification とは多対多の関連を持つ。
 * </p>
 */
@Entity //JPAがこのクラスをDBテーブルとして扱う
public class Employee {

  @Id //主キー テーブルのIDをDBが自動で番号を振る
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private Long id; //社員ID_システム内部の識別子 【DBが決める】
  @Column(nullable = false, unique = true) // nullable = false ...禁止 unique = true ...重複禁止
  private String employeeNumber; //社員番号_業務上の識別子 【必須・変更不可】
  private String name; //社員氏名 初期設計 【必須・変更不可】
  private String department; //所属部署 変更される 【後から変更される】
  private String grade; //等級 変更される 【後から変更される】

  protected Employee() {}
  // protected = JPA用

  @ManyToMany(fetch = FetchType.LAZY) // Employee と Qualification をつなぐテーブル
  @JoinTable(
      name = "employee_qualifications",
      joinColumns = @JoinColumn(name = "employee_id"),
      inverseJoinColumns = @JoinColumn(name = "qualification_id")
  )
  private List<Qualification> qualifications = new ArrayList<>();

  /**
   * 社員を生成する
   *
   * @param employeeNumber 社員番号
   * @param name 社員の氏名
   * @param department 所属部署
   * @param grade 等級
   * @param qualifications 保有資格一覧
   */
  public Employee(String employeeNumber,
      String name,
      String department,
      String grade,
      List<Qualification> qualifications) {
    //社員番号と氏名が無いと作れないので、引数として受け取る
    this.employeeNumber = employeeNumber;
    //この Employee 自身の Employeeが持つフィールド = 引数として渡された値
    this.name = name;
    this.department = department;
    this.grade = grade;
    this.qualifications = (qualifications != null)
        ? qualifications : new ArrayList<>();
  }

  // getter
  public String getName() {
    return name;
  }

  public String getEmployeeNumber() {
    return employeeNumber;
  }

  public String getGrade() {
    return grade;
  }

  public String getDepartment() {
    return department;
  }

  public List<Qualification> getQualifications() {
    return qualifications;
  }

  //setter
  public void setDepartment(String department) {
    this.department = department;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }
}
