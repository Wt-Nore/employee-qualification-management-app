package com.example.demo.Repository;

import com.example.demo.entity.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository
    extends JpaRepository<Employee, Long> {
  List<Employee> findByNameContaining(String keyword);
  Optional<Employee> findEmployeeByEmployeeNumber(String employeeNumber);
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

  // フリーワード横断検索
  // DISTINCT... ManyToMany JOIN で同じ社員が重複するのを防ぐため
  // LEFT JOIN... 資格を持っていない社員も検索対象に含めるため
  // OR... 入力欄で「どれかに一致すればヒット」させるため
  @Query("""
      SELECT DISTINCT e
      FROM Employee e
      LEFT JOIN e.qualifications q
      WHERE
        e.name LIKE %:keyword%
        OR e.department LIKE %:keyword%
        OR e.grade LIKE %:keyword%
        OR e.employeeNumber LIKE %:keyword%
        OR q.qualificationName LIKE %:keyword%
      """)
  List<Employee> searchByKeyword(@Param("keyword") String keyword);
}
