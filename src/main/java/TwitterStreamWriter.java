import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by glebu on 02-Feb-17.
 */
class TwitterStreamWriter {
    private Database mysql;
    private TwitterStream twitterStream;
    TwitterStreamWriter(TwitterApiToken token, Database mysql) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(token.getConsumerKey())
                .setOAuthConsumerSecret(token.getConsumerSecret())
                .setOAuthAccessToken(token.getAccessToken())
                .setOAuthAccessTokenSecret(token.getAccessTokenSecret());

        this.mysql = mysql;

        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new MyStatusListener(mysql, twitterStream);
        this.twitterStream.addListener(listener);
    }

    void trackKeywords(String[] keywords) {
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keywords);
        twitterStream.filter(filterQuery);

    }
}


