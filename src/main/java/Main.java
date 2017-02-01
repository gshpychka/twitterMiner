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


        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("m6AVdSoBYmxnSFqDj0e6DnKg5")
                .setOAuthConsumerSecret("0QETuaG1fqpIET7mR5QA8TPP95dvS7dp2rWOFthkFzI5gPsRhl")
                .setOAuthAccessToken("67847124-DYAO5f29ePZhXWJqgpsaRNzA4LmUYHTKHXbMHEu5d")
                .setOAuthAccessTokenSecret("bjzvAB3i5glPJrkmw6CAH6sPeQuHSpiTp3E7Qbf88yNRE");

        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener(){
            Database mysql = new Database("jdbc:mysql://localhost:3306/default?useUnicode=yes&characterEncoding=utf-8", "gshpychka", "gVwx1K77");
            int i =0;
            String username = new String();
            String tweet = new String();
            public void onStatus(Status status) {
                this.username = EmojiParser.removeAllEmojis(status.getUser().getName());
                this.tweet = EmojiParser.removeAllEmojis(status.getText());
                if(!tweet.startsWith("RT @") && ( tweet.contains("Trump") || tweet.contains("Bannon"))) {
                    System.out.println(i + ": " + this.username + ": " + this.tweet);
                    try {
                        this.mysql.insertValues(username, tweet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                i++;
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
            public void onStallWarning(StallWarning stallWarning) {}

            public void onScrubGeo(long l, long l1) {}
        };

        twitterStream.addListener(listener);

        FilterQuery filterQuery = new FilterQuery();
        String[] keywords = new String[]{"Bannon", "Trump"};

        filterQuery.track(keywords);


        twitterStream.filter(filterQuery);

    }
}
