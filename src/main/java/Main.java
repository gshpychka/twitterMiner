import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.util.Locale;

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
            int i =0;
            public void onStatus(Status status) {
                if(!status.getText().startsWith("RT @")) {
                    System.out.println(i + ": " + status.getUser().getName() + " : " + status.getText());
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
