package com.example.demo.entity;
/**　Javadoc(説明書)に書くべきこと
 * このクラスは何を表すか（主語を用いる）
 * 何を責務・役割として持つか
 * 何をしないか
 */

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



/** Employee　クラスは、一人の社員を表すエンティティクラスです。
 *
 * このクラスは、社員番号や氏名など、
 * 社員を一意に識別するための情報を保持します。
 *
 * 社員の所属部署や等級などの情報は、
 * 後から設定・変更されることを想定しています。
 */

// ----- 学習メモ -----
// ・フィールドとは、そのクラスが持っている情報のこと
// ・Employeeフィールドは、外部から変更させないため、すべて private にする
// ・ID は　DB 側で自動採番する想定のため Long にする
// ・資格情報はこのクラスには持たせない
// ・TODO: 所属部署や等級は、後から設定・変更されることを想定しているため setter を用意する
//
// ・コンストラクタとは、「正しい状態」で生まれさせるための仕組み「この条件を満たさないと、このクラスは作れません」というルールのこと
// ・社員番号と氏名を指定して Employee を生成します
// ・public は「他のクラスから利用してよい」ことを表す
// ・Employee は、Controller や Service から生成されるため public にする
//
// ・getter は 他のクラスなどが「値を見る・取得」することを表す
// ・getterの名前は get + フィールド名(先頭大文字)の形で書く 例：getName()
// ・setter は 他のクラスなどが「値の設定・変更」することを表す 「読み取り専用の窓口」
// ・
// ・
// ・

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

  @ManyToMany(fetch = FetchType.EAGER) // Employee と Qualification をつなぐテーブル
  @JoinTable(
      name = "employee_qualifications",
      joinColumns = @JoinColumn(name = "employee_id"),
      inverseJoinColumns = @JoinColumn(name = "qualification_id")
  )
  private List<Qualification> qualifications = new ArrayList<>();


  /** コンストラクタ
   *
   * 社員番号
   * 氏名
   * 所属部署
   * 等級
   *
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
    this.qualifications = qualifications;
  }

  // getter
  public String getName() {
    return name;
    //public 他のクラス(Controller / HTML から使わせるため)
    //String 返す値の型 name が String だから String
    //getName() getterなので「このEmployee の名前を取得するだけ」 という意味
    //return name; Employee が持っている name を外に渡す
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
    //public...他のクラス(Controller / HTML から使わせるため)
    //void...値を返さない 「変更したら終わり」という意味
    //setDepartment(String department) ...「所属部署を設定する」 という入口
    //引数の department は「新しい所属部署」
    //this.department Employee が持っているフィールドを
    //= department 外から渡された新しい値
    //Employee 自身の所属部署を新しい値に置き換える
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }




}
