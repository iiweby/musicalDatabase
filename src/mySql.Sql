
CREATE TABLE Friend (
  friendID INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  age INT,
  email VARCHAR(255)
);

CREATE TABLE Interest (
  interestID INT PRIMARY KEY AUTO_INCREMENT,
  major VARCHAR(255),
  streamingPlatform VARCHAR(255)
);

CREATE TABLE MusicGenre (
  genreID INT PRIMARY KEY AUTO_INCREMENT,
  genreName VARCHAR(255) NOT NULL,
  favoriteArtist VARCHAR(255),
  favoriteSong VARCHAR(255)
);

CREATE TABLE MatchMade (
  matchID INT PRIMARY KEY AUTO_INCREMENT,
  friendID INT NOT NULL,
  interestID INT NOT NULL,
  FOREIGN KEY (friendID) REFERENCES Friend(friendID),
  FOREIGN KEY (interestID) REFERENCES Interest(interestID));
  
