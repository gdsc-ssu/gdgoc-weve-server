package com.weve.repository;

import com.weve.domain.CategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Long> {
}
