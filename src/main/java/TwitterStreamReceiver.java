import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Created by glebu on 02-Feb-17.
 */
class TwitterStreamReceiver {
    private String keyword = "";
    private DatabaseWriter databaseWriter;
    private TwitterStream twitterStream;

    TwitterStreamReceiver(TwitterApiToken token, DatabaseWriter databaseWriter, String keyword) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(token.getConsumerKey())
                .setOAuthConsumerSecret(token.getConsumerSecret())
                .setOAuthAccessToken(token.getAccessToken())
                .setOAuthAccessTokenSecret(token.getAccessTokenSecret());

        this.databaseWriter = databaseWriter;
        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new MyStatusListener(this);
        this.twitterStream.addListener(listener);
        trackKeyword(keyword);
        this.keyword = keyword;
    }

    private void trackKeyword(String keyword) {
        this.keyword = keyword;
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track(keyword);
        twitterStream.filter(filterQuery);
    }

    void processTweet(Status status){
        if(status.isRetweet()) {
           databaseWriter.writeRetweetHibernate(new StatusPOJO(status.getRetweetedStatus()));
           MyStatusListener.RETWEET_CCOUNT++;
        } else {
            databaseWriter.writeTweetHibernate(new StatusPOJO(status));
            MyStatusListener.TWEET_COUNT++;
        }
    }


}


