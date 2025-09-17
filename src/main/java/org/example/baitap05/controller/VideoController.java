package org.example.baitap05.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.baitap05.entity.Video;
import org.example.baitap05.entity.Category;
import org.example.baitap05.entity.User;
import org.example.baitap05.service.VideoService;
import org.example.baitap05.service.CategoryService;
import org.example.baitap05.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/videos/*")
public class VideoController extends HttpServlet {
    private VideoService videoService;
    private CategoryService categoryService;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
        categoryService = new CategoryService();
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
                    listVideos(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteVideo(request, response);
                    break;
                case "search":
                    searchVideos(request, response);
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
                    addVideo(request, response);
                    break;
                case "edit":
                    editVideo(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            loadFormData(request);
            if (action.equals("add")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/videos/add.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                request.getRequestDispatcher("/WEB-INF/views/admin/videos/edit.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            }
        }
    }
    
    private void listVideos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Video> videos = videoService.getAllVideos();
        List<Category> categories = categoryService.getAllCategories();
        
        request.setAttribute("videos", videos);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/videos/list.jsp").forward(request, response);
    }
    
    private void searchVideos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String categoryIdParam = request.getParameter("categoryId");
        
        List<Video> videos;
        Integer categoryId = null;
        
        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty() && !categoryIdParam.equals("0")) {
            try {
                categoryId = Integer.parseInt(categoryIdParam);
            } catch (NumberFormatException e) {
                categoryId = null;
            }
        }
        
        if ((keyword == null || keyword.trim().isEmpty()) && categoryId == null) {
            videos = videoService.getAllVideos();
        } else {
            videos = videoService.searchVideos(keyword, categoryId);
        }
        
        List<Category> categories = categoryService.getAllCategories();
        
        request.setAttribute("videos", videos);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.getRequestDispatcher("/WEB-INF/views/admin/videos/list.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        loadFormData(request);
        request.getRequestDispatcher("/WEB-INF/views/admin/videos/add.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/videos/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            Video video = videoService.getVideoById(id);
            
            if (video == null) {
                request.setAttribute("error", "Video not found");
                response.sendRedirect(request.getContextPath() + "/admin/videos/list");
                return;
            }
            
            request.setAttribute("video", video);
            loadFormData(request);
            request.getRequestDispatcher("/WEB-INF/views/admin/videos/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/videos/list");
        }
    }
    
    private void addVideo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String url = request.getParameter("url");
        String categoryIdParam = request.getParameter("categoryId");
        String uploaderIdParam = request.getParameter("uploaderId");
        
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setUrl(url);
        
        // Set category
        if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
            try {
                Integer categoryId = Integer.parseInt(categoryIdParam);
                Category category = new Category();
                category.setCategoryId(categoryId);
                video.setCategory(category);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid category selected");
            }
        }
        
        // Set uploader
        if (uploaderIdParam != null && !uploaderIdParam.trim().isEmpty()) {
            try {
                Integer uploaderId = Integer.parseInt(uploaderIdParam);
                User uploader = new User();
                uploader.setUserId(uploaderId);
                video.setUploader(uploader);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid uploader selected");
            }
        }
        
        videoService.saveVideo(video);
        
        response.sendRedirect(request.getContextPath() + "/admin/videos/list?success=added");
    }
    
    private void editVideo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String url = request.getParameter("url");
        String categoryIdParam = request.getParameter("categoryId");
        String uploaderIdParam = request.getParameter("uploaderId");
        
        try {
            Integer id = Integer.parseInt(idParam);
            Video video = videoService.getVideoById(id);
            
            if (video == null) {
                throw new IllegalArgumentException("Video not found");
            }
            
            video.setTitle(title);
            video.setDescription(description);
            video.setUrl(url);
            
            // Set category
            if (categoryIdParam != null && !categoryIdParam.trim().isEmpty()) {
                try {
                    Integer categoryId = Integer.parseInt(categoryIdParam);
                    Category category = new Category();
                    category.setCategoryId(categoryId);
                    video.setCategory(category);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid category selected");
                }
            }
            
            // Set uploader
            if (uploaderIdParam != null && !uploaderIdParam.trim().isEmpty()) {
                try {
                    Integer uploaderId = Integer.parseInt(uploaderIdParam);
                    User uploader = new User();
                    uploader.setUserId(uploaderId);
                    video.setUploader(uploader);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid uploader selected");
                }
            }
            
            videoService.saveVideo(video);
            
            response.sendRedirect(request.getContextPath() + "/admin/videos/list?success=updated");
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid video ID");
        }
    }
    
    private void deleteVideo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/videos/list");
            return;
        }
        
        try {
            Integer id = Integer.parseInt(idParam);
            videoService.deleteVideo(id);
            response.sendRedirect(request.getContextPath() + "/admin/videos/list?success=deleted");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/videos/list?error=invalid_id");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/videos/list?error=" + e.getMessage());
        }
    }
    
    private void loadFormData(HttpServletRequest request) {
        List<Category> categories = categoryService.getAllCategories();
        List<User> users = userService.getAllUsers();
        
        request.setAttribute("categories", categories);
        request.setAttribute("users", users);
    }
}
