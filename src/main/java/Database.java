import com.vdurmont.emoji.EmojiParser;

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

    void insertValues(String username, String tweet) throws Exception {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tweets (username, tweet) VALUES (?, ?)");
        statement.setString(1, username);
        statement.setString(2, tweet);
        statement.executeUpdate();
    }



}
