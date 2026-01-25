package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/** Qualification　クラスは、資格を表すエンティティクラスです。
 *
 * このクラスは、資格名や資格IDなど、
 * 資格の情報を保持します。
 *
 * 社員は複数の資格を持てます。
 * 社員が、持っている資格一覧、持っていない資格一覧
 * 資格名から、持っている社員一覧、持っていない社員一覧
 * を表示することを想定しています。
 */

@Entity
public class Qualification {

  @Id
  private String qualificationId; // 主キー
  private String qualificationName;

  protected Qualification() {}
  // protected = JPA用

  public String getQualificationId() {
    return qualificationId;
  }

  public String getQualificationName() {
    return qualificationName;
  }

  public Qualification(String qualificationId, String qualificationName){
    //資格IDと資格名を、引数として受け取る
    this.qualificationId = qualificationId;
    //この Employee 自身の Employeeが持つフィールド = 引数として渡された値
    this.qualificationName = qualificationName;
  }
}
