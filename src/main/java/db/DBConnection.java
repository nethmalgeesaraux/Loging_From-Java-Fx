package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/javafx_login",
                "root",
                "1234"
        );
    }
}
