import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateTables {
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

        Statement statement = null;
        statement = connect.createStatement();

        String drop1 = "Drop table if exists user";
        String drop2 = "Drop table if exists admin";
        String drop3 = "Drop table if exists moderator";
        String drop4 = "Drop table if exists user";
        String drop5 = "Drop table if exists permissions";

        String user = "create table user" + "(uid int, username varchar(20), password varchar(20), phone varchar(10), " +
                "email varchar(20), preferences varchar(50), permissions varchar(50), Primary key(uid));";

        String admin = "create table admin" + "(uid int, username varchar(20), password varchar(20), phone varchar(10), " +
                "email varchar(20), preferences varchar(50), permissions varchar(50), Primary key(uid), " +
                "Foreign key(uid) References user(uid));";

        String moderator = "create table moderator" + "(uid int, username varchar(20), password varchar(20), phone varchar(10), " +
                "email varchar(20), preferences varchar(50), permissions varchar(50), Primary key(uid), " +
                "Foreign key(uid) References user(uid));";

        String normalUser = "create table normalUser" + "(uid int, username varchar(20), password varchar(20), phone varchar(10), " +
                "email varchar(20), preferences varchar(50), permissions varchar(50), Primary key(uid), " +
                "Foreign key(uid) References user(uid));";

        String permissions = "create table permissions" + "(pid int, uid int, Primary key(pid,uid), " +
                "Foreign key(uid) References user(uid), Foreign key(uid) References user(uid));";

        statement.executeUpdate(drop5);
        statement.executeUpdate(drop4);
        statement.executeUpdate(drop3);
        statement.executeUpdate(drop2);
        statement.executeUpdate(drop1);

        statement.executeUpdate(user);
        statement.executeUpdate(admin);
        statement.executeUpdate(moderator);
        statement.executeUpdate(normalUser);
        statement.executeUpdate(permissions);
    }
}
