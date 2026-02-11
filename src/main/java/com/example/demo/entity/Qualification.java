package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * 資格を表すエンティティクラス
 *
 * <p>
 *   資格IDと資格名を保持する。
 *   Employee　クラスと多対多の関連を持つ。
 *   社員は複数の資格を持てる。
 *   社員が、持っている資格一覧、持っていない資格一覧
 *   資格名から、持っている社員一覧、持っていない社員一覧
 *   を表示することを想定しています。
 * </p>
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
