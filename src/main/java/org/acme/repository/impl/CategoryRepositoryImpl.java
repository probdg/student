package org.acme.repository.impl;

import java.util.Map;

import org.acme.dto.CategoryDto;
import org.acme.entity.Category;
import org.acme.repository.CategoryRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public Map<String, Object> createCategory(CategoryDto req) {
        Category category = new Category();
        category.setName(req.name());
        Category.persist(category);
        return Map.of("id", category.getId(), "name", category.getName());
    }

}
