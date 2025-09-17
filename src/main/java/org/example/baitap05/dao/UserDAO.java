package org.example.baitap05.dao;

import jakarta.persistence.EntityManager;
import org.example.baitap05.config.JPAConfig;
import org.example.baitap05.entity.User;

import java.util.List;

public class UserDAO extends BaseDAO<User, Integer> {
    
    public UserDAO() {
        super(User.class);
    }
    
    @Override
    protected Integer getId(User entity) {
        return entity.getUserId();
    }
    
    public User findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            List<User> results = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<User> searchByName(String keyword) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.fullName LIKE :keyword ORDER BY u.fullName", User.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<User> findByRole(User.Role role) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.role = :role ORDER BY u.fullName", User.class)
                    .setParameter("role", role)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean existsByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
    
    public User authenticate(String email, String passwordHash) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            List<User> results = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.passwordHash = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", passwordHash)
                    .getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
