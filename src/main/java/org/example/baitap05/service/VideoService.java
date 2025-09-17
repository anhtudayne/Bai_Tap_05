package org.example.baitap05.service;

import org.example.baitap05.dao.VideoDAO;
import org.example.baitap05.dao.CategoryDAO;
import org.example.baitap05.dao.UserDAO;
import org.example.baitap05.entity.Video;
import org.example.baitap05.entity.Category;
import org.example.baitap05.entity.User;

import java.util.List;

public class VideoService {
    private VideoDAO videoDAO;
    private CategoryDAO categoryDAO;
    private UserDAO userDAO;
    
    public VideoService() {
        this.videoDAO = new VideoDAO();
        this.categoryDAO = new CategoryDAO();
        this.userDAO = new UserDAO();
    }
    
    public List<Video> getAllVideos() {
        return videoDAO.findAllWithDetails();
    }
    
    public Video getVideoById(Integer id) {
        return videoDAO.findByIdWithDetails(id);
    }
    
    public List<Video> searchVideos(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllVideos();
        }
        return videoDAO.searchByTitle(keyword.trim());
    }
    
    public List<Video> searchVideos(String keyword, Integer categoryId) {
        if (keyword == null) keyword = "";
        return videoDAO.searchByTitleAndCategory(keyword.trim(), categoryId);
    }
    
    public List<Video> getVideosByCategory(Integer categoryId) {
        return videoDAO.findByCategory(categoryId);
    }
    
    public List<Video> getVideosByUploader(Integer uploaderId) {
        return videoDAO.findByUploader(uploaderId);
    }
    
    public List<Video> getVideosWithPagination(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return videoDAO.findPaginated(offset, pageSize);
    }
    
    public Video saveVideo(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null");
        }
        
        if (video.getTitle() == null || video.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Video title cannot be empty");
        }
        
        if (video.getUrl() == null || video.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Video URL cannot be empty");
        }
        
        // Validate category exists
        if (video.getCategory() == null || video.getCategory().getCategoryId() == null) {
            throw new IllegalArgumentException("Category must be specified");
        }
        
        Category category = categoryDAO.findById(video.getCategory().getCategoryId());
        if (category == null) {
            throw new IllegalArgumentException("Selected category does not exist");
        }
        video.setCategory(category);
        
        // Validate uploader exists
        if (video.getUploader() == null || video.getUploader().getUserId() == null) {
            throw new IllegalArgumentException("Uploader must be specified");
        }
        
        User uploader = userDAO.findById(video.getUploader().getUserId());
        if (uploader == null) {
            throw new IllegalArgumentException("Selected uploader does not exist");
        }
        video.setUploader(uploader);
        
        return videoDAO.save(video);
    }
    
    public void deleteVideo(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Video ID cannot be null");
        }
        
        Video video = videoDAO.findById(id);
        if (video == null) {
            throw new IllegalArgumentException("Video not found");
        }
        
        videoDAO.delete(id);
    }
    
    public long getVideoCount() {
        return videoDAO.count();
    }
    
    public boolean videoExists(Integer id) {
        return videoDAO.findById(id) != null;
    }
}
