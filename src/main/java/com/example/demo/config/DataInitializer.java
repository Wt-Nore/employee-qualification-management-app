package com.example.demo.config;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Qualification;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.QualificationRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initData(
      EmployeeRepository employeeRepository,
      QualificationRepository qualificationRepository) {

    return args -> {
      // --- 資格を投入 ---
      Qualification q1 = new Qualification("Q001", "基本情報技術者");
      Qualification q2 = new Qualification("Q002", "応用情報技術者");
      Qualification q3 = new Qualification("Q003", "ITパスポート");

      qualificationRepository.saveAll(List.of(q1, q2, q3));

      // --- 社員を投入 ---
      Employee e1 = new Employee(
          "ya001", "山田太郎", "工事部", "主任", List.of(q1, q2)
      );
      Employee e2 = new Employee(
          "sa001", "佐藤桂子", "営業部", "係長", List.of(q3)
      );
      Employee e3 = new Employee(
          "su001", "鈴木一郎", "工事部", "係長", List.of()
      );
      Employee e4 = new Employee(
          "ya002", "山田二郎", "工事部", "課員", List.of(q1)
      );

      employeeRepository.saveAll(List.of(e1, e2, e3, e4));
    };
  }
}