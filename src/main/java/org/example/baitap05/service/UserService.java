package org.example.baitap05.service;

import org.example.baitap05.dao.UserDAO;
import org.example.baitap05.entity.User;

import java.util.List;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    public User getUserById(Integer id) {
        return userDAO.findById(id);
    }
    
    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userDAO.searchByName(keyword.trim());
    }
    
    public List<User> getUsersByRole(User.Role role) {
        return userDAO.findByRole(role);
    }
    
    public User saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Check for duplicate email (only if it's a new user or email changed)
        if (user.getUserId() == null) {
            if (userDAO.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        } else {
            User existingUser = userDAO.findById(user.getUserId());
            if (existingUser != null && 
                !existingUser.getEmail().equals(user.getEmail()) &&
                userDAO.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        
        return userDAO.save(user);
    }
    
    public void deleteUser(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        User user = userDAO.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        userDAO.delete(id);
    }
    
    public User authenticate(String email, String password) {
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        // In a real application, you would hash the password before comparing
        // For simplicity, we're using plain text passwords here
        return userDAO.authenticate(email, password);
    }
    
    public long getUserCount() {
        return userDAO.count();
    }
    
    public boolean userExists(Integer id) {
        return userDAO.findById(id) != null;
    }
    
    public boolean isAdmin(User user) {
        return user != null && user.getRole() == User.Role.admin;
    }
}
