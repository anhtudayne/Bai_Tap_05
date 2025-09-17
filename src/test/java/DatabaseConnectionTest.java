import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/video_portal?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        String username = "vovantu";
        String password = "vovantu123!!!";
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Test connection
            System.out.println("Testing database connection...");
            Connection connection = DriverManager.getConnection(url, username, password);
            
            System.out.println("✅ Database connection successful!");
            
            // Test basic query
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db_name");
            
            if (rs.next()) {
                System.out.println("✅ Connected to database: " + rs.getString("db_name"));
            }
            
            // Check if tables exist
            rs = stmt.executeQuery("SHOW TABLES");
            System.out.println("\n📋 Tables in database:");
            while (rs.next()) {
                System.out.println("  - " + rs.getString(1));
            }
            
            connection.close();
            System.out.println("\n✅ Database test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Database connection failed:");
            e.printStackTrace();
            
            System.err.println("\n🔧 Troubleshooting steps:");
            System.err.println("1. Make sure MySQL server is running");
            System.err.println("2. Verify database 'video_portal' exists");
            System.err.println("3. Check username/password: " + username);
            System.err.println("4. Run the database.sql script if tables don't exist");
        }
    }
}
