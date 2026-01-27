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
}

  /**
   * Javadocコメント　フォーマット
   * @アノテーションの上に書く
   * 何をするメソッドか（概要）
   * 詳細説明
   * 引数・戻り値　@param / @return で説明
   */