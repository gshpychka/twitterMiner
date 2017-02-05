
import twitter4j.Status;
import java.sql.*;

/**
 * Created by glebu on 01-Feb-17.
 */
class DatabaseWriter {
    private String url = "jdbc:mysql://localhost:3306/twitter?character_set_server=utf8mb4&character_set_connection=utf8mb4&characterEncoding=utf-8&character_set_results=utf8mb4";
    private String username = "gshpychka";
    private String password = "gVwx1K77";
    private Connection connection;
    private MultithreadWriter multithreadWriter;

    DatabaseWriter(){
        this.connection = getConnection();
        this.multithreadWriter = new MultithreadWriter();
    }

    private Connection getConnection() {
        System.out.println("Connecting database...");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    void writeTweet(Status status, String keyword){
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO users (userID, handle, name, followers) VALUES (?, ?, ?, ?)");
            statement.setString(1, Long.toString(status.getUser().getId()));
            statement.setString(2, status.getUser().getScreenName());
            statement.setString(3, status.getUser().getName());
            statement.setString(4, Long.toString(status.getUser().getFollowersCount()));
            multithreadWriter.executeStatement(statement);
        } catch (SQLException e) {
            System.out.println("\nError while creating query for inserting into users\n");
        }
        try {
            statement = connection.prepareStatement("INSERT INTO tweets_" + keyword.toLowerCase() + " (userID, tweetID, tweetText, unixTimestamp) VALUES (?, ?, ?, ?)");
            statement.setString(1, Long.toString(status.getUser().getId()));
            statement.setString(2, Long.toString(status.getId()));
            statement.setString(3, status.getText());
            statement.setString(4, Long.toString(status.getCreatedAt().getTime()));
            multithreadWriter.executeStatement(statement);
        } catch (SQLException e) {
            System.out.println("\n=====================================================\nError while writing tweet into tweets\n");
        }

    }
    void writeRetweet(Status status, String keyword) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("UPDATE tweets_" + keyword.toLowerCase() + " SET retweets = ?, favorites = ? WHERE tweetID = ?");
            statement.setLong(1, status.getRetweetCount());
            statement.setLong(2, status.getFavoriteCount());
            statement.setLong(3, status.getId());
            multithreadWriter.executeStatement(statement);
        } catch (SQLException e) {
            System.out.println("\n--------------------=============------------------\nRetweet exception: "+ e.getMessage());
        }
    }
}
