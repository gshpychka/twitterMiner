import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;

/**
 * Created by glebu on 01-Feb-17.
 * The main class.
 */
public class Main {
    public static void main (String[] args) throws TwitterException, IOException {

        TwitterApiToken token = new TwitterApiToken("m6AVdSoBYmxnSFqDj0e6DnKg5",
                "0QETuaG1fqpIET7mR5QA8TPP95dvS7dp2rWOFthkFzI5gPsRhl",
                "67847124-DYAO5f29ePZhXWJqgpsaRNzA4LmUYHTKHXbMHEu5d",
                "bjzvAB3i5glPJrkmw6CAH6sPeQuHSpiTp3E7Qbf88yNRE");

        Database mysql = new Database("jdbc:mysql://localhost:3306/twitter?character_set_server=utf8mb4&character_set_connection=utf8mb4&characterEncoding=utf-8&character_set_results=utf8mb4", "gshpychka", "gVwx1K77");


        TwitterStreamWriter twitterWriterTrump = new TwitterStreamWriter(token, mysql);
        TwitterStreamWriter twitterWriterBannon = new TwitterStreamWriter(token, mysql);

        twitterWriterTrump.trackKeyword("Trump");
        //twitterWriterBannon.trackKeyword("Bannon");

    }
}
