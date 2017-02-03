
import twitter4j.Status;
import java.sql.*;

/**
 * Created by glebu on 01-Feb-17.
 */
class DatabaseWriter {
    private PreparedStatement statement;
    private String url = "jdbc:mysql://localhost:3306/twitter?character_set_server=utf8mb4&character_set_connection=utf8mb4&characterEncoding=utf-8&character_set_results=utf8mb4";
    private String username = "gshpychka";
    private String password = "gVwx1K77";
    private Connection connection;
    private RetweetWriter retweetWriter;


    DatabaseWriter(){
        this.connection = getConnection();
        this.retweetWriter = new RetweetWriter(this);
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
            System.out.println("\nError while writing tweet into users\n");;
        }
        try {
            statement = connection.prepareStatement("INSERT INTO tweets_" + keyword.toLowerCase() + " (userID, tweetID, tweetText, unixTimestamp) VALUES (?, ?, ?, ?)");
            statement.setString(1, Long.toString(status.getUser().getId()));
            statement.setString(2, Long.toString(status.getId()));
            statement.setString(3, status.getText());
            statement.setString(4, Long.toString(status.getCreatedAt().getTime()));
            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // A tweet with this tweetID is already in the DB. Ignore.
            //This shouldn't happen, but it does. Probably a bug in the API or twitter4j.
            System.out.println("-------------------------------------");
            System.out.println("\nWeird. Duplicate tweet. Ignoring \n");
            System.out.println("-------------------------------------");
        } catch (SQLException e) {
            System.out.println("\n=====================================================\nError while writing tweet into tweets\nQuery was: " + statement.toString());;
        }

    }
    void writeRetweetMultithreaded(Status status, String keyword) {
        retweetWriter.setKeyword(keyword);
        retweetWriter.retweetsQueue.add(status);
        if(retweetWriter.retweetsQueue.size() >= 200)
            retweetWriter.write();
    }



    void writeRetweet(Status status, String keyword) {
        try {
            statement = connection.prepareStatement("UPDATE tweets_" + keyword.toLowerCase() + " SET retweets = ? WHERE tweetID = ?");
            statement.setLong(1, status.getRetweetCount());
            statement.setLong(2, status.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n--------------------=============------------------\nRetweet exception: "+ e.getMessage() + "\nQuery was: " + statement.toString());
        }
    }
}
