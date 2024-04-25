import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MusicLibraryGUI extends Application {

    private TableView<Song> tableView;

   
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Music Profile Library");

        Button addButton = new Button("Add Profile");
        addButton.setOnAction(e -> addProfileDialog()); 

        Button deleteButton = new Button("Delete Profile");
        deleteButton.setOnAction(e -> deleteProfile());

        Button viewButton = new Button("View Profiles");
        viewButton.setOnAction(e -> viewSongs());

        // Instantiate the tableView
        tableView = new TableView<>();

        // Define columns for the tableView
        TableColumn<Song, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Song, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Song, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Song, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Song, String> majorColumn = new TableColumn<>("Major");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        TableColumn<Song, String> platformColumn = new TableColumn<>("Streaming Platform");
        platformColumn.setCellValueFactory(new PropertyValueFactory<>("platform"));

        TableColumn<Song, String> genreColumn = new TableColumn<>("Favorite Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Song, String> artistColumn = new TableColumn<>("Favorite Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn<Song, String> songColumn = new TableColumn<>("Favorite Song");
        songColumn.setCellValueFactory(new PropertyValueFactory<>("song"));

        // Add columns to the tableView
        tableView.getColumns().add(idColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(ageColumn);
        tableView.getColumns().add(emailColumn);
        tableView.getColumns().add(majorColumn);
        tableView.getColumns().add(platformColumn);
        tableView.getColumns().add(genreColumn);
        tableView.getColumns().add(artistColumn);
        tableView.getColumns().add(songColumn);


        VBox vbox = new VBox();
        vbox.getChildren().addAll(
                addButton,
                deleteButton,
                viewButton,
                tableView
        );
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    

    

    private void viewSongs() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM MusicGenre");
            ResultSet rs = pstmt.executeQuery();

            ObservableList<Song> songs = FXCollections.observableArrayList();

            while (rs.next()) {
                int id = rs.getInt("genreID");
                String title = rs.getString("genreName");
                String artist = rs.getString("favoriteArtist");
                String genre = rs.getString("favoriteSong");
                songs.add(new Song(id, title, id, artist, genre, genre, genre, genre, genre));
            }

            tableView.setItems(songs);

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


    private void deleteProfile() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Profile");
        dialog.setHeaderText("Enter the ID of the profile to delete:");
        dialog.setContentText("Profile ID:");

        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            int profileID = Integer.parseInt(result.get());
            try {
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MusicProfile WHERE id = ?");
                pstmt.setInt(1, profileID);
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Profile with ID " + profileID + " deleted successfully.");
                    viewSongs(); // Refresh the table after deletion
                } else {
                    System.out.println("No profile found with ID " + profileID);
                }
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void addProfileDialog() {
        // Create a dialog for adding profile
        Dialog<Song> dialog = new Dialog<>();
        dialog.setTitle("Add Profile");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create and configure the text fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField majorField = new TextField();
        majorField.setPromptText("Major");
        TextField platformField = new TextField();
        platformField.setPromptText("Streaming Platform");
        TextField genreField = new TextField();
        genreField.setPromptText("Favorite Genre");
        TextField artistField = new TextField();
        artistField.setPromptText("Favorite Artist");
        TextField songField = new TextField();
        songField.setPromptText("Favorite Song");

        // Create layout and add fields
        VBox content = new VBox();
        content.getChildren().addAll(
                nameField,
                ageField,
                emailField,
                majorField,
                platformField,
                genreField,
                artistField,
                songField
        );

        // Set dialog content
        dialog.getDialogPane().setContent(content);

        // Convert the result to a profile when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Song(
                        0, // ID will be generated automatically by the database
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        emailField.getText(),
                        majorField.getText(),
                        platformField.getText(),
                        genreField.getText(),
                        artistField.getText(),
                        songField.getText()
                );
            }
            return null;
        });

        // Show dialog and wait for user input
        dialog.showAndWait().ifPresent(this::addProfile);
    }

    private void addProfile(Song profile) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String queryFriend = "INSERT INTO Friend (name, age, email) VALUES (?, ?, ?)";
            String queryInterest = "INSERT INTO Interest (major, streamingPlatform) VALUES (?, ?)";
            String queryGenre = "INSERT INTO MusicGenre (genreName, favoriteArtist, favoriteSong) VALUES (?, ?, ?)";

            PreparedStatement pstmtFriend = conn.prepareStatement(queryFriend, Statement.RETURN_GENERATED_KEYS);
            pstmtFriend.setString(1, profile.getName());
            pstmtFriend.setInt(2, profile.getAge());
            pstmtFriend.setString(3, profile.getEmail());
            pstmtFriend.executeUpdate();

            PreparedStatement pstmtInterest = conn.prepareStatement(queryInterest, Statement.RETURN_GENERATED_KEYS);
            pstmtInterest.setString(1, profile.getMajor());
            pstmtInterest.setString(2, profile.getPlatform());
            pstmtInterest.executeUpdate();

            PreparedStatement pstmtGenre = conn.prepareStatement(queryGenre, Statement.RETURN_GENERATED_KEYS);
            pstmtGenre.setString(1, profile.getGenre());
            pstmtGenre.setString(2, profile.getArtist());
            pstmtGenre.setString(3, profile.getSong());
            pstmtGenre.executeUpdate();

            System.out.println("Profile added successfully");

            pstmtFriend.close();
            pstmtInterest.close();
            pstmtGenre.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static class Song {
        private final int id;
        private final String name;
        private final int age;
        private final String email;
        private final String major;
        private final String platform;
        private final String genre;
        private final String artist;
        private final String song;

        public Song(int id, String name, int age, String email, String major, String platform, String genre, String artist, String song) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.email = email;
            this.major = major;
            this.platform = platform;
            this.genre = genre;
            this.artist = artist;
            this.song = song;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getEmail() {
            return email;
        }

        public String getMajor() {
            return major;
        }

        public String getPlatform() {
            return platform;
        }

        public String getGenre() {
            return genre;
        }

        public String getArtist() {
            return artist;
        }

        public String getSong() {
            return song;
        }
    }
}