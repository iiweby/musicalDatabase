import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MusicLibrary {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        
        int choice;

        do {
            System.out.println("\n Music Profile Library!!");
            System.out.println("1. Add Music Profile");
            System.out.println("2. View Others Favorite Songs");
            System.out.println("3. Update Profiles");
            System.out.println("4. Delete Profile");
            System.out.println("5. Exit"); 
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addProfile();
                    break;
                case 2:
                    viewSongs();
                    break;
                case 3:
                    updateSong();
                    break;
                case 4:
                    deleteSong();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        scanner.close();
    }

    public static void addProfile() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your age:");
        int age = scanner.nextInt();
        scanner.nextLine(); 
        System.out.println("Enter your email:");
        String email = scanner.nextLine();

        System.out.println("Enter your major:");
        String major = scanner.nextLine();
        System.out.println("Enter your favorite streaming platform:");
        String platform = scanner.nextLine();

        System.out.println("Enter your favorite music genre:");
        String genre = scanner.nextLine();
        System.out.println("Enter your favorite artist:");
        String artist = scanner.nextLine();
        System.out.println("Enter your favorite song:");
        String song = scanner.nextLine();

        String queryFriend = "INSERT INTO Friend (name, age, email) VALUES (?, ?, ?)";
        String queryInterest = "INSERT INTO Interest (major, streamingPlatform) VALUES (?, ?)";
        String queryGenre = "INSERT INTO MusicGenre (genreName, favoriteArtist, favoriteSong) VALUES (?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmtFriend = conn.prepareStatement(queryFriend, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement pstmtInterest = conn.prepareStatement(queryInterest, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement pstmtGenre = conn.prepareStatement(queryGenre, Statement.RETURN_GENERATED_KEYS);

        pstmtFriend.setString(1, name);
        pstmtFriend.setInt(2, age);
        pstmtFriend.setString(3, email);
        pstmtFriend.executeUpdate();

        pstmtInterest.setString(1, major);
        pstmtInterest.setString(2, platform);
        pstmtInterest.executeUpdate();

        pstmtGenre.setString(1, genre);
        pstmtGenre.setString(2, artist);
        pstmtGenre.setString(3, song);
        pstmtGenre.executeUpdate();

        System.out.println("Profile added successfully");
    }

    public static void viewSongs() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM MusicGenre");
    
        System.out.println("\nList of Songs:");
        System.out.println("ID\tTitle\tArtist\tGenre");
    
        while (rs.next()) {
            int id = rs.getInt("genreID");
            String title = rs.getString("genreName");
            String artist = rs.getString("favoriteArtist");
            String genre = rs.getString("favoriteSong");
            System.out.println(id + "\t" + title + "\t" + artist + "\t" + genre);
        }
    
        stmt.close();
        conn.close();
    }
    

    public static void updateSong() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the song you want to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
    
        System.out.println("Enter the new title: ");
        String newTitle = scanner.nextLine();
        System.out.println("Enter the new artist: ");
        String newArtist = scanner.nextLine();
        System.out.println("Enter the new genre: ");
        String newGenre = scanner.nextLine();
    
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE MusicGenre SET genreName=?, favoriteArtist=?, favoriteSong=? WHERE genreID=?");
        pstmt.setString(1, newTitle);
        pstmt.setString(2, newArtist);
        pstmt.setString(3, newGenre);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
    
        System.out.println("Song updated successfully.");

        viewSongs();
    
        pstmt.close();
        conn.close();
    }
    
    

    public static void deleteSong() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ID of the profile you want to delete: ");
        int id = scanner.nextInt();
    
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MusicGenre WHERE genreID=?");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    
        System.out.println("Profile deleted successfully.");
    
        pstmt.close();
        conn.close();
    }
    

}