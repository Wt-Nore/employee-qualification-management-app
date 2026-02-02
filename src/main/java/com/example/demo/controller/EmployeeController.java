package com.example.demo.controller;

import com.example.demo.dto.EmployeeSearchCondition;
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

    EmployeeSearchCondition condition = employeeService.buildConditionFromKeyword(keyword);
    //condition.setKeyword(
    //    (keyword != null && keyword.isBlank()) ? null : keyword
    //); // keyword の null / blank を整理

    // URL に /employees , /keyword を追加する

    //List<Employee> employees = employeeService.searchByKeyword(keyword); //DTOを作る際に、.search に戻す
    List<Employee> employees = employeeService.search(condition);

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

    Optional<Employee> employeeOpt =
        employeeService.findEmployeeByEmployeeNumber(employeeNumber);

    if (employeeOpt.isPresent()) {
      Employee employee = employeeOpt.get();

      System.out.println("employeeNumber = " + employee.getEmployeeNumber());
      System.out.println("qualifications size = " + employee.getQualifications().size());

      model.addAttribute("employee", employee);

  } else {//
      model.addAttribute("message", "該当社員は見つかりませんでした");//
    }

    model.addAttribute("keyword", keyword); // 検索ワードを Model に渡す

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