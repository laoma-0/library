package com.demo.library.service;

import com.demo.library.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    boolean deleteCategory(Long id);

    /**
     * 检查指定ID的分类是否存在。
     *
     * @param id 分类ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsById(Long id);

}
