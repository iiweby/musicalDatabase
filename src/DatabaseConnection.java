import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://cs.neiu.edu:3306/SP24CS3151_dparedes3?ServerTimezone=UTC&";
                url += "user=SP24CS3151_dparedes3&password=dparedes3703549";
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}