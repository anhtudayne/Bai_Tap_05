package org.example.baitap05.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.baitap05.service.CategoryService;
import org.example.baitap05.service.UserService;
import org.example.baitap05.service.VideoService;

import java.io.IOException;

@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
    private CategoryService categoryService;
    private UserService userService;
    private VideoService videoService;
    
    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
        userService = new UserService();
        videoService = new VideoService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        String action = pathInfo != null ? pathInfo.substring(1) : "dashboard";
        
        switch (action) {
            case "dashboard":
            case "":
                showDashboard(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get statistics for dashboard
        long categoryCount = categoryService.getCategoryCount();
        long userCount = userService.getUserCount();
        long videoCount = videoService.getVideoCount();
        
        request.setAttribute("categoryCount", categoryCount);
        request.setAttribute("userCount", userCount);
        request.setAttribute("videoCount", videoCount);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
