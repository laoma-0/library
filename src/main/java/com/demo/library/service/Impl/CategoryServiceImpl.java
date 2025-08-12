package com.demo.library.service.Impl;

import com.demo.library.model.Category;
import com.demo.library.repository.CategoryRepository;
import com.demo.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        category.setCreateTime(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("分类不存在");
        }
        Category exist = optional.get();
        exist.setName(category.getName());
        exist.setParentId(category.getParentId());
        exist.setDescription(category.getDescription());
        return categoryRepository.save(exist);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id);
        return true;
    }
}
