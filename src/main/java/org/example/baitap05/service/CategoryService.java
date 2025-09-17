package org.example.baitap05.service;

import org.example.baitap05.dao.CategoryDAO;
import org.example.baitap05.entity.Category;

import java.util.List;

public class CategoryService {
    private CategoryDAO categoryDAO;
    
    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }
    
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }
    
    public Category getCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }
    
    public Category getCategoryByName(String name) {
        return categoryDAO.findByName(name);
    }
    
    public List<Category> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCategories();
        }
        return categoryDAO.searchByName(keyword.trim());
    }
    
    public Category saveCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        // Check for duplicate name (only if it's a new category or name changed)
        if (category.getCategoryId() == null) {
            if (categoryDAO.existsByName(category.getCategoryName())) {
                throw new IllegalArgumentException("Category name already exists");
            }
        } else {
            Category existingCategory = categoryDAO.findById(category.getCategoryId());
            if (existingCategory != null && 
                !existingCategory.getCategoryName().equals(category.getCategoryName()) &&
                categoryDAO.existsByName(category.getCategoryName())) {
                throw new IllegalArgumentException("Category name already exists");
            }
        }
        
        return categoryDAO.save(category);
    }
    
    public void deleteCategory(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        
        Category category = categoryDAO.findById(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found");
        }
        
        categoryDAO.delete(id);
    }
    
    public long getCategoryCount() {
        return categoryDAO.count();
    }
    
    public boolean categoryExists(Integer id) {
        return categoryDAO.findById(id) != null;
    }
}
