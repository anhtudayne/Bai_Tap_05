package org.example.baitap05.dao;

import jakarta.persistence.EntityManager;
import org.example.baitap05.config.JPAConfig;
import org.example.baitap05.entity.Video;

import java.util.List;

public class VideoDAO extends BaseDAO<Video, Integer> {
    
    public VideoDAO() {
        super(Video.class);
    }
    
    @Override
    protected Integer getId(Video entity) {
        return entity.getVideoId();
    }
    
    public List<Video> findAllWithDetails() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "ORDER BY v.createdAt DESC", Video.class)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public Video findByIdWithDetails(Integer id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            List<Video> results = em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "WHERE v.videoId = :id", Video.class)
                .setParameter("id", id)
                .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Video> searchByTitle(String keyword) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "WHERE v.title LIKE :keyword " +
                "ORDER BY v.createdAt DESC", Video.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Video> searchByTitleAndCategory(String keyword, Integer categoryId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String query = "SELECT v FROM Video v " +
                    "LEFT JOIN FETCH v.category " +
                    "LEFT JOIN FETCH v.uploader " +
                    "WHERE v.title LIKE :keyword";
            
            if (categoryId != null && categoryId > 0) {
                query += " AND v.category.categoryId = :categoryId";
            }
            query += " ORDER BY v.createdAt DESC";
            
            var jpqlQuery = em.createQuery(query, Video.class)
                    .setParameter("keyword", "%" + keyword + "%");
            
            if (categoryId != null && categoryId > 0) {
                jpqlQuery.setParameter("categoryId", categoryId);
            }
            
            return jpqlQuery.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Video> findByCategory(Integer categoryId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "WHERE v.category.categoryId = :categoryId " +
                "ORDER BY v.createdAt DESC", Video.class)
                .setParameter("categoryId", categoryId)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Video> findByUploader(Integer uploaderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "WHERE v.uploader.userId = :uploaderId " +
                "ORDER BY v.createdAt DESC", Video.class)
                .setParameter("uploaderId", uploaderId)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Video> findPaginated(int offset, int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM Video v " +
                "LEFT JOIN FETCH v.category " +
                "LEFT JOIN FETCH v.uploader " +
                "ORDER BY v.createdAt DESC", Video.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
