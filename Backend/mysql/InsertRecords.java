import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsertRecords {
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub
        Connection connect = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Set up connection parameters
            String userName = "coms309";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/Project";
            //Set up connection
            connect = DriverManager.getConnection(dbServer, userName, password);
        } catch (Exception e) {}
    }
}
