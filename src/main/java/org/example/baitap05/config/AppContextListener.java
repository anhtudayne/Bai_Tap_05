package org.example.baitap05.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize EntityManagerFactory when application starts
        try {
            JPAConfig.getEntityManagerFactory();
            System.out.println("EntityManagerFactory initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Close EntityManagerFactory when application shuts down
        try {
            JPAConfig.close();
            System.out.println("EntityManagerFactory closed successfully");
        } catch (Exception e) {
            System.err.println("Error closing EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
