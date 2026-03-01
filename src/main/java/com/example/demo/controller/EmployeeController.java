package com.example.demo.controller;

import com.example.demo.dto.EmployeeForm;
import com.example.demo.dto.EmployeeSearchCondition;
import com.example.demo.dto.EmployeeSearchResult;
import com.example.demo.service.EmployeeService;
import java.util.Optional;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 社員に関する画面リクエストを処理するController。
 * 社員情報の登録・編集・一覧表示を扱うクラス。
 *
 * <p>
 *   画面表示やフォーム送信を受け取り、
 *   必要に応じて Service へ処理を委譲する。
 *
 *   新規登録及び編集機能は、共通フォームを使用し、
 *   formMode によって表示を切り替える。
 *
 *   役割
 *   URL を受ける
 *   Service を呼ぶ
 *   Model に詰める
 *   画面名を返す
 * </p>
 */
@Controller
public class EmployeeController {

  private final EmployeeService employeeService;
  public EmployeeController (EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  //１社員一覧画面、２社員詳細画面、３社員新規登録画面、４新規登録処理、５社員情報編集画面、６社員情報更新処理、７社員情報削除処理

  /**
   * 社員一覧画面を表示する。
   *
   * <p>
   *   フリーワードを受け取り、検索条件に変換し、
   *   検索条件を「資格あり／なし」に分類して画面へ渡す。
   * </p>
   *
   * <p>
   *   ADMINのみ、編集モードに切り替え可能。
   * </p>
   *
   * <ul>
   * <li>keyword が指定された場合、検索条件を生成して社員を検索する。</li>
   * <li>ADMINユーザーかつ mode=edit の場合、編集モードを有効化する。</li>
   * </ul>
   *
   * @param keyword 検索キーワード（社員名など、未指定または空の場合は全件表示）
   * @param mode 編集モード指定用パラメータ（"edit" の場合に編集モード有効）
   * @param authentication 認証情報（ロール判定に使用）
   * @param model 画面に社員一覧と検索条件を渡すための Model
   * @return 社員一覧画面 html を返す（employee-list）
   */
  @GetMapping("/employees")
  public String list(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String mode,
      Authentication authentication,
      Model model) {

    // 編集モード判定
    boolean isAdmin = authentication != null &&
        authentication.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    boolean editMode = isAdmin && "edit".equals(mode);
    model.addAttribute("editMode", editMode);

    // 検索処理
    EmployeeSearchCondition condition =
        employeeService.buildConditionFromKeyword(keyword);

    EmployeeSearchResult result =
        employeeService.search(condition);

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

  /**
   * 社員追加画面を表示する（管理者専用機能）。
   *
   * <p>
   *   空の Employee オブジェクトを生成し、フォームへバインドする。
   * </p>
   *
   * @param model 画面へ値を渡すためのModel
   * @return 社員追加登録画面 html を返す(employee-form)
   */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/employees/new")
  public String showCreateForm(Model model) {
    model.addAttribute("employeeForm", new Employee());
    model.addAttribute("formMode", "create");
    return "employee-form";
  }

  /**
   * 社員情報を新規登録する（管理者専用機能）。
   *
   * <p>
   *   フォームから送信された Employee を保存し、登録後は社員一覧画面へリダイレクトする。
   * </p>
   *
   * @param employee フォームへの入力値がバインドされた Employee
   * @return 社員一覧画面へのリダイレクト
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/employees")
  public String createEmployee(@ModelAttribute Employee form) {
    employeeService.createEmployee(form);
    return "redirect:/employees";
  }

  /**
   * 社員情報編集画面を表示する（管理者専用機能）。
   * <p>
   *   指定された社員IDの情報を取得し、
   *   編集フォームへ渡して表示する。
   * </p>
   * @param id 編集対象の社員ID
   * @param model 画面へ社員情報を渡す Model
   * @return 社員編集フォーム画面(employee-form)
   */
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/employees/{id}/edit")
  public String editEmployee(@PathVariable Long id, Model model) {
    Employee employee = employeeService.findById(id);

    Employee form = new Employee();
    form.setId(employee.getId());
    form.setEmployeeNumber(employee.getEmployeeNumber());
    form.setName(employee.getName());
    form.setDepartment(employee.getDepartment());
    form.setGrade(employee.getGrade());

    model.addAttribute("employeeForm", form);
    model.addAttribute("formMode", "edit");

    return "employee-form";
  }

  /**
   * 社員情報を更新処理する（管理者専用機能）。
   * @param id
   * @param employee
   * @return
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/employees/{id}")
  public String updateEmployee(
      @PathVariable Long id,
      @ModelAttribute Employee form) {

    employeeService.updateEmployee(id, form);

    return "redirect:/employees";
  }

  /**
   * 社員情報を削除する（管理者専用機能）。
   *
   * <p>
   *   削除対象社員の deleted フラグを true に設定する。
   * </p>
   * @param employeeNumber 削除対象の社員番号
   * @return 社員一覧画面へのリダイレクト
   */
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/employees/{employeeNumber}/delete")
  public String deleteEmployee(@PathVariable String employeeNumber) {
    employeeService.deletedEmployee(employeeNumber);
    return "redirect:/employees";
  }
}