import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MusicLibrary {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM Booking";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("guestNo");
                String price = rs.getString("price");
                System.out.println("ID: " + id + ", Name: " + price);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}