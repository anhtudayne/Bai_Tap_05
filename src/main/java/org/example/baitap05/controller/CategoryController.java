package org.example.baitap05.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.baitap05.entity.Category;
import org.example.baitap05.service.CategoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/categories/*")
public class CategoryController extends HttpServlet {
    private CategoryService categoryService;
    
    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        String action = pathInfo != null ? pathInfo.substring(1) : "list";
        
        try {
            switch (action) {
                case "list":
                case "":
                    listCategories(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        String action = pathInfo != null ? pathInfo.substring(1) : "";
        
        try {
            switch (action) {
                case "add":
                    addCategory(request, response);
                    break;
                case "edit":
                    editCategory(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            if (action.equals("add")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/categories/add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/categories/edit.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            }
        }
    }
    
    private void listCategories(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String search = request.getParameter("search");
        List<Category> categories;
        
        if (search != null && !search.trim().isEmpty()) {
            categories = categoryService.searchCategories(search);
            request.setAttribute("search", search);
        } else {
            categories = categoryService.getAllCategories();
        }
        
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/categories/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/categories/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/categories/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            Category category = categoryService.getCategoryById(id);
            
            if (category == null) {
                request.setAttribute("error", "Category not found");
                response.sendRedirect(request.getContextPath() + "/admin/categories/list");
                return;
            }
            
            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/views/admin/categories/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/categories/list");
        }
    }
    
    private void addCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setDescription(description);
        
        categoryService.saveCategory(category);
        
        response.sendRedirect(request.getContextPath() + "/admin/categories/list?success=added");
    }
    
    private void editCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String categoryName = request.getParameter("categoryName");
        String description = request.getParameter("description");
        
        try {
            Integer id = Integer.parseInt(idParam);
            Category category = categoryService.getCategoryById(id);
            
            if (category == null) {
                throw new IllegalArgumentException("Category not found");
            }
            
            category.setCategoryName(categoryName);
            category.setDescription(description);
            
            categoryService.saveCategory(category);
            
            response.sendRedirect(request.getContextPath() + "/admin/categories/list?success=updated");
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid category ID");
        }
    }
    
    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/categories/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            categoryService.deleteCategory(id);
            response.sendRedirect(request.getContextPath() + "/admin/categories/list?success=deleted");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/categories/list?error=invalid_id");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/categories/list?error=" + e.getMessage());
        }
    }
}
