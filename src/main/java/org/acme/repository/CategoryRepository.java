package org.acme.repository;

import java.util.Map;

import org.acme.dto.CategoryDto;
import org.acme.entity.Category;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface CategoryRepository extends PanacheRepository<Category> {
    Map<String, Object> createCategory(CategoryDto CategoryDto);

}
