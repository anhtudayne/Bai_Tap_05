package org.example.baitap05.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.baitap05.entity.User;
import org.example.baitap05.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users/*")
public class UserController extends HttpServlet {
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserService();
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
                    listUsers(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
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
                    addUser(request, response);
                    break;
                case "edit":
                    editUser(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            if (action.equals("add")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/users/add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/users/edit.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            }
        }
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String search = request.getParameter("search");
        String roleParam = request.getParameter("role");
        List<User> users;
        
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search);
            request.setAttribute("search", search);
        } else if (roleParam != null && !roleParam.trim().isEmpty()) {
            try {
                User.Role role = User.Role.valueOf(roleParam);
                users = userService.getUsersByRole(role);
                request.setAttribute("selectedRole", roleParam);
            } catch (IllegalArgumentException e) {
                users = userService.getAllUsers();
            }
        } else {
            users = userService.getAllUsers();
        }
        
        request.setAttribute("users", users);
        request.setAttribute("roles", User.Role.values());
        request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("roles", User.Role.values());
        request.getRequestDispatcher("/WEB-INF/views/admin/users/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            User user = userService.getUserById(id);
            
            if (user == null) {
                request.setAttribute("error", "User not found");
                response.sendRedirect(request.getContextPath() + "/admin/users/list");
                return;
            }
            
            request.setAttribute("user", user);
            request.setAttribute("roles", User.Role.values());
            request.getRequestDispatcher("/WEB-INF/views/admin/users/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users/list");
        }
    }
    
    private void addUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String roleParam = request.getParameter("role");
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(password); // In real app, hash the password
        
        try {
            User.Role role = User.Role.valueOf(roleParam);
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            user.setRole(User.Role.user);
        }
        
        userService.saveUser(user);
        
        response.sendRedirect(request.getContextPath() + "/admin/users/list?success=added");
    }
    
    private void editUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String roleParam = request.getParameter("role");
        
        try {
            Integer id = Integer.parseInt(idParam);
            User user = userService.getUserById(id);
            
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }
            
            user.setFullName(fullName);
            user.setEmail(email);
            
            // Only update password if provided
            if (password != null && !password.trim().isEmpty()) {
                user.setPasswordHash(password); // In real app, hash the password
            }
            
            try {
                User.Role role = User.Role.valueOf(roleParam);
                user.setRole(role);
            } catch (IllegalArgumentException e) {
                // Keep existing role if invalid
            }
            
            userService.saveUser(user);
            
            response.sendRedirect(request.getContextPath() + "/admin/users/list?success=updated");
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID");
        }
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            userService.deleteUser(id);
            response.sendRedirect(request.getContextPath() + "/admin/users/list?success=deleted");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users/list?error=invalid_id");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/users/list?error=" + e.getMessage());
        }
    }
}
