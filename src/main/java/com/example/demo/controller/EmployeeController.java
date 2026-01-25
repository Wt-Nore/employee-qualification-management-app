package com.example.demo.controller;

import com.example.demo.entity.Qualification;
import com.example.demo.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class EmployeeController {
// EmployeeController...社員を制御するクラス
  /**
   * EmployeeController...社員を制御するクラス
   * 役割
   * URL を受ける
   * Service を呼ぶ
   * Model に詰める
   * 画面名を返す
   */

  private final EmployeeService employeeService;
  @Autowired
  public EmployeeController (EmployeeService employeeService) {
    this.employeeService = employeeService;
  }


  /**
   * showEmployeesList...社員一覧を表示するメソッド
   * URL /employees を追加する
   *
  // @GetMapping("/employees")
  // public String showEmployeesList(@RequestParam(required = false) String keyword, Model model){

    System.out.println("keyword = " + keyword);

  //  List<Employee> employees = employeeService.getAllEmployees();
  //  model.addAttribute("employees", employees);
    // 社員一覧を画面に渡す

    return "employee-list";
    // 社員一覧画面を表示する
  }
  */

  /**
   * showEmployees...社員全件を表示する。
   * <p>keyword が指定されている場合は、そのキーワードに一致する社員を検索し、
   * 指定されていない場合は全社員を表示する。</p>
   * @param keyword 検索キーワード（社員名など、未指定または空の場合は全件表示）
   * @param model 画面に社員一覧と検索条件を渡すための Model
   * @return 社員一覧画面（employee-list）
   */
  @GetMapping("/employees") //リクエストがあったら keyword に応じた結果を、画面に社員一覧を渡すためのModelオブジェクト
  public String showEmployees(
      @RequestParam(required = false) String keyword,
      Model model) {

    // if ... keyword の null / blank を整理
    if (keyword != null && keyword.isBlank()) {
      keyword = null;
    }

    // URL に /employees , /keyword を追加する
    List<Employee> employees = employeeService.search(keyword);
    model.addAttribute("employees", employees);
    model.addAttribute("keyword", keyword);

    // @return html "employee-list" 社員一覧画面を返す
    return "employee-list";
  }




  //！！　Serviceに社員一覧を作らせる getAllployeeService() メソッドを追加すること！！

  /**
      @PathVariable String employeeNumber,
      Model model
  ) {
    // employees...社員一覧画面を用意する
    // showEmployeesList...社員一覧を表示する処理メソッド


    List<Employee> employees = List.of(
        new Employee("E001", "山田太郎"),
        new Employee("E002", "佐藤桂子"),
        new Employee("E003", "鈴木一郎")
        // List<Employee>...社員一覧
        // new とは、Employeeという社員情報の設計図を使って、今ここに社員を一人作ってください　という命令
        // 現在はDBがないので、上記の様に仮の社員を手打ちしている　将来はDBからEmployeeを取得したい

    );

    // 社員一覧を画面に渡す
    model.addAttribute("employees", employees);

    // 社員一覧画面を表示する
    return "employee-list";
  }
*/

  /**
   * 社員詳細画面を表示するメソッド
   */
  @GetMapping("/employees/{employeeNumber}")
  public String showEmployeesDetail(
      @PathVariable String employeeNumber,
      @RequestParam(required = false) String keyword,
      Model model
  ) {

    // 社員一覧を用意する
    //List<Employee> employees = List.of(
    //  new Employee("E001", "山田太郎"),
    //  new Employee("E002", "佐藤桂子"),
    //  new Employee("E003", "鈴木一郎")
    //);

    // まだ社員は見つかっていない状態
    //  Employee foundEmployee = null;

    // 社員一覧から該当社員を探す
    // for (Employee employee : employees) {
    //  if (employee.getEmployeeNumber().equals(employeeNumber)) {

    //    foundEmployee = employee;

    //    break; //見つかったらループ終了
    //   }
    // }

    // 社員が見つからなかった場合
    // if (foundEmployee == null) {
    //   return "employee-not-found";
    // }

    // 見つかった場合、該当社員を画面に渡す
    // model.addAttribute("employee", foundEmployee);
    // 社員詳細画面を表示する Controller → HTMLの employee-detail に渡す
    //return "employee-detail";
    //  }
    //}

    Optional<Employee> employeeOpt =
        employeeService.findEmployeeByEmployeeNumber(employeeNumber);

    if (employeeOpt.isPresent()) {
      Employee employee = employeeOpt.get();

    //  System.out.println("employeeNumber = " + employee.getEmployeeNumber());
    //  System.out.println("qualifications size = " + employee.getQualifications().size());

      System.out.println("employeeNumber = " + employee.getEmployeeNumber());
      System.out.println("qualifications size = " + employee.getQualifications().size());

      model.addAttribute("employee", employee);

  } else {//
      model.addAttribute("message", "該当社員は見つかりませんでした");//
    }

    model.addAttribute("keyword", keyword); // 検索ワードを Model に渡す


    /**
    if (employeeOpt.isEmpty()) {
      model.addAttribute("message", "該当社員は見つかりませんでした");
      return "employee-not-found";
    }

    model.addAttribute("employee", employeeOpt.get());
    model.addAttribute("keyword", keyword);

     */

      return "employee-detail";
      // } else {
      //   return "employee-not-found";


  }


  /**
   * 資格検索画面を表示するメソッド
   */
  @GetMapping("/employees/qualification/{qualificationId}")
  public String showEmployeesByQualification(
      @PathVariable String qualificationId,
      Model model) {

    List<Employee> employees =
        employeeService.findEmployeesByQualificationId(qualificationId);

    //　デバッグ用
    System.out.println("employees size = " + employees.size());
    if (employees.isEmpty()) {
      System.out.println("該当社員は0人です");
    } else {
      System.out.println("該当社員は" + employees.size() + "人です");
    }


    // 画面表示用
    if (employees.isEmpty()) {
      model.addAttribute("message","該当者は見つかりませんでした");
    }
    model.addAttribute("qualificationId", qualificationId);
    model.addAttribute("employees", employees);

    return "employee-list";
  }

  /**
   * Javadocコメント　フォーマット
   * @アノテーションの上に書く
   * 何をするメソッドか（概要）
   * 詳細説明
   * 引数・戻り値　@param / @return で説明
   */

  /**
   * showQualifications...資格一覧画面を表示する。
   * <p>URL /qualifications へのアクセスを受け取り、全ての資格情報を取得して、画面に渡す。</p>
   * @param model 画面に資格一覧を渡すためのModelオブジェクト
   * @return html "qualification-list" 資格一覧画面を返す
   */
  @GetMapping("/qualifications")
  public String showQualificationList(Model model){
    List<Qualification> qualifications = employeeService.getAllQualifications();
    model.addAttribute("qualifications", qualifications);

    return "qualification-list";
  }

}



