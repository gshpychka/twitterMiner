
import twitter4j.Status;
import java.sql.*;

/**
 * Created by glebu on 01-Feb-17.
 */
class Database {
    private String url;
    private String username;
    private String password;
    private Connection connection;
    Database(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = getConnection();

    }

    private Connection getConnection() {
        System.out.println("Connecting database...");

        try {
            return DriverManager.getConnection(url, username, password);
            //System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    void executeQuery(String query) throws Exception {
        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate();
    }

    void insertValues(String username, String tweet) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tweets (username, tweet) VALUES (?, ?)");
        statement.setString(1, username);
        statement.setString(2, tweet);
        statement.executeUpdate();
    }

    void writeTweet(Status status, String keyword){
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("INSERT INTO users (userID, handle, name, followers) VALUES (?, ?, ?, ?)");
            statement.setString(1, Long.toString(status.getUser().getId()));
            statement.setString(2, status.getUser().getScreenName());
            statement.setString(3, status.getUser().getName());
            statement.setString(4, Long.toString(status.getUser().getFollowersCount()));
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("\nThis user is already in the database\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = connection.prepareStatement("INSERT INTO tweets_" + keyword.toLowerCase() + " (userID, tweetID, tweetText, unixTimestamp) VALUES (?, ?, ?, ?)");
            statement.setString(1, Long.toString(status.getUser().getId()));
            statement.setString(2, Long.toString(status.getId()));
            statement.setString(3, status.getText());
            statement.setString(4, Long.toString(status.getCreatedAt().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void writeRetweet(Status status, String keyword) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE tweets_" + keyword.toLowerCase() + " SET retweets = retweets + 1 WHERE tweetID = " + status.getRetweetedStatus().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n--------------------------------------\nRetweet exception: "+ e.getMessage() + "\n");
        }
    }



}
