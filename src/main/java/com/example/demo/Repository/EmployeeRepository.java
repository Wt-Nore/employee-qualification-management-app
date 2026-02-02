package com.example.demo.Repository;

import com.example.demo.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository
    extends JpaRepository<Employee, Long> {

  // フリーワード横断検索
  // DISTINCT... ManyToMany JOIN で同じ社員が重複するのを防ぐため
  // LEFT JOIN... 資格を持っていない社員も検索対象に含めるため
  // OR... 入力欄で「どれかに一致すればヒット」させるため
  // AND... 入力欄で「Condition + Condition」で
  @Query("""
      SELECT DISTINCT e
      FROM Employee e
      LEFT JOIN e.qualifications q
      WHERE
        (:keyword IS NULL
        OR e.name LIKE CONCAT('%', :keyword, '%')
        OR e.employeeNumber LIKE CONCAT('%', :keyword, '%')
        )
        AND (:department IS NULL
      OR e.department LIKE CONCAT('%', :department, '%')
        )
        AND (:grade IS NULL
      OR e.grade LIKE CONCAT('%', :grade, '%')
      )
      AND (:qualificationName IS NULL
        OR q.qualificationName LIKE CONCAT('%', :qualificationName, '%')
        )
      """)

  //AND (:department IS NULL OR e.department = :department)
  //      OR e.department LIKE CONCAT('%', :department, '%')
  //OR e.department = :department
  //OR e.grade LIKE CONCAT('%', :grade, '%')
  //AND (:grade IS NULL OR e.grade = :grade)
  //OR e.grade = :grade

  List<Employee> searchByCondition(
      @Param("keyword") String keyword,
      @Param("department") String department,
      @Param("grade") String grade,
      @Param("qualificationName") String qualificationName
  );


  Optional<Employee> findByEmployeeNumber(String employeeNumber);
}
