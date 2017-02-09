
import twitter4j.*;
import java.io.IOException;

/**
 * Created by glebu on 01-Feb-17.
 * The main class.
 */
public class Main {
    public static void main (String[] args) throws TwitterException, IOException {

        TwitterApiToken token = new TwitterApiToken();

        DatabaseWriter databaseWriter = new DatabaseWriter();
        TwitterStreamReceiver twitterWriterTrump = new TwitterStreamReceiver(token, databaseWriter,"Trump");


    }
}
