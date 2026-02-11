package com.example.demo.controller;

import com.example.demo.dto.EmployeeSearchCondition;
import com.example.demo.dto.EmployeeSearchResult;
import com.example.demo.service.EmployeeService;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 社員に関する画面リクエストを処理するController。
 *
 * <p>
 * 役割
 * URL を受ける
 * Service を呼ぶ
 * Model に詰める
 * 画面名を返す
 * </p>
 */
@Controller
public class EmployeeController {

  private final EmployeeService employeeService;
  public EmployeeController (EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  /**
   * 社員一覧画面を表示する。
   *
   * <p>
   *   フリーワードを受け取り、検索条件に変換し、
   *   検索条件を「資格あり／なし」に分類して画面へ渡す。
   * </p>
   *
   * @param keyword 検索キーワード（社員名など、未指定または空の場合は全件表示）
   * @param model 画面に社員一覧と検索条件を渡すための Model
   * @return 社員一覧画面 html を返す（employee-list）
   */
  @GetMapping("/employees")
  public String search(
      @RequestParam(required = false) String keyword,
      Model model) {

    EmployeeSearchCondition condition = employeeService.buildConditionFromKeyword(keyword);

    EmployeeSearchResult result = employeeService.search(condition);


    //model.addAttribute("employees", employees);
    model.addAttribute("hasQualification", result.getHasQualification());
    model.addAttribute("hasNotQualification", result.getHasNotQualification());
    model.addAttribute("keyword", keyword);

    return "employee-list";
  }

  /**
   * 社員詳細画面を表示する。
   *
   * <p>
   *   指定された社員番号の社員情報を取得し、
   *   存在する場合は詳細画面へ渡す。
   *   存在しない場合はメッセージを表示する
   * </p>
   *
   * @param employeeNumber 社員番号
   * @param keyword 一覧画面から渡された検索キーワード
   * @param model 画面へ値を渡すためのModel
   * @return 社員詳細画面 html を返す(employee-detail)
   */
  @GetMapping("/employees/{employeeNumber}")
  public String showEmployeeDetail(
      @PathVariable String employeeNumber,
      @RequestParam(required = false) String keyword,
      Model model
  ) {

    Optional<Employee> employeeOpt =
        employeeService.findEmployeeByEmployeeNumber(employeeNumber);

    if (employeeOpt.isPresent()) {
      Employee employee = employeeOpt.get();
      model.addAttribute("employee", employee);
  } else {
      model.addAttribute("message", "該当社員は見つかりませんでした");//
    }

    model.addAttribute("keyword", keyword); // 検索ワードを Model に渡す

      return "employee-detail";
      // } else {
      //   return "employee-not-found";
  }
}