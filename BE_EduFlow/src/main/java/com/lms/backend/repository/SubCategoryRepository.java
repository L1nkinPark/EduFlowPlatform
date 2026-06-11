package com.lms.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lms.backend.model.entity.SubCategory;

import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{
    Optional<SubCategory> findBySubCategoryName(String name);
}
