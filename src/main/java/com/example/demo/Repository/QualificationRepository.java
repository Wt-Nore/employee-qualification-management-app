package com.example.demo.Repository;

import com.example.demo.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository
    extends JpaRepository<Qualification, String> {
}