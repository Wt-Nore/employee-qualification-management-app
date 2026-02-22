package com.example.demo.repository;

import com.example.demo.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository
    extends JpaRepository<Employee, Long> {

  /**
   * 社員を検索条件に基づいて取得する。
   *
   * <p>
   * フリーワード横断検索（部署,等級）
   * </p>
   * <p>
   * 各条件が null の場合は、その条件を無視する
   * </p>
   * <p>
   * 論理削除済み (deleted = true) の場合は、検索対象外とする
   * </p>
   *
   * @param keyword 氏名または社員番号の部分一致
   * @param department 部署の部分一致
   * @param grade 等級の部分一致
   * @return 検索条件に一致する社員
   */
  @Query("""
      SELECT DISTINCT e
      FROM Employee e
      WHERE e.deleted = false
      AND (
        :keyword IS NULL
        OR e.name LIKE CONCAT('%', :keyword, '%')
        OR e.employeeNumber LIKE CONCAT('%', :keyword, '%')
        )
        AND (
        :department IS NULL
        OR e.department LIKE CONCAT('%', :department, '%')
        )
        AND (
        :grade IS NULL
        OR e.grade LIKE CONCAT('%', :grade, '%')
      )
      """)

  List<Employee> searchByCondition(
      @Param("keyword") String keyword,
      @Param("department") String department,
      @Param("grade") String grade
  );

  /**
   * 社員番号で社員を1件取得する。
   *
   * @param employeeNumber 社員番号の完全一致
   * @return 検索条件に一致する社員
   */
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

  List<Employee> findByDeletedFalse();

  /**
   * 社員番号をキーに、社員を1件取得する。
   * <p>
   *   詳細画面表示用のメソッド
   *   qualifications を LEFT JOIN FETCH で同時取得することで、
   *   遅延ロードによる LazyInitializationException を防ぐ。
   * </p>
   *
   * @param employeeNumber 社員番号の完全一致
   * @return 社員　存在しない場合は、Optional.empty()
   */
  @Query("""
    SELECT e
    FROM Employee e
    LEFT JOIN FETCH e.qualifications
    WHERE e.employeeNumber = :employeeNumber
""")
  Optional<Employee> findWithQualificationsByEmployeeNumber(
      @Param("employeeNumber") String employeeNumber);

  private boolean containsIgnoreNull(String value, String target) {
    return value != null && value.contains(target);
  }
}
