package org.example.baitap05.dao;

import jakarta.persistence.EntityManager;
import org.example.baitap05.config.JPAConfig;
import org.example.baitap05.entity.Category;

import java.util.List;

public class CategoryDAO extends BaseDAO<Category, Integer> {
    
    public CategoryDAO() {
        super(Category.class);
    }
    
    @Override
    protected Integer getId(Category entity) {
        return entity.getCategoryId();
    }
    
    public Category findByName(String categoryName) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            List<Category> results = em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :name", Category.class)
                    .setParameter("name", categoryName)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Category> searchByName(String keyword) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c WHERE c.categoryName LIKE :keyword ORDER BY c.categoryName", Category.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean existsByName(String categoryName) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(c) FROM Category c WHERE c.categoryName = :name", Long.class)
                    .setParameter("name", categoryName)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}
