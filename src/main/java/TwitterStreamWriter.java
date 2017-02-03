import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.sql.SQLException;

/**
 * Created by glebu on 02-Feb-17.
 */
class TwitterStreamWriter {
    private String keyword = "";
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
        StatusListener listener = new MyStatusListener(this);
        this.twitterStream.addListener(listener);
    }

    String getKeyword() {
        return keyword;
    }

    void trackKeyword(String keyword) {
        this.keyword = keyword;
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keyword);
        twitterStream.filter(filterQuery);


    }
    void writeTweet(Status status){
        if(!status.isRetweet())
            mysql.writeTweet(status, keyword);
        else
            mysql.writeRetweet(status, keyword);
    }


}


