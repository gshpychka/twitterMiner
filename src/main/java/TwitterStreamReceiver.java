import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Created by glebu on 02-Feb-17.
 */
class TwitterStreamReceiver {
    private String keyword = "";
    private DatabaseWriter databaseWriter;
    private TwitterStream twitterStream;
    private MyStatusListener listener;
    TwitterStreamReceiver(TwitterApiToken token, DatabaseWriter databaseWriter, String keyword) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(token.getConsumerKey())
                .setOAuthConsumerSecret(token.getConsumerSecret())
                .setOAuthAccessToken(token.getAccessToken())
                .setOAuthAccessTokenSecret(token.getAccessTokenSecret());

        this.databaseWriter = databaseWriter;
        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        listener = new MyStatusListener(this, keyword);
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
            status = status.getRetweetedStatus();
            listener.setRETWEET_CCOUNT(listener.getRETWEET_CCOUNT() + 1);
        }
        databaseWriter.writeTweet(new StatusPOJO(status, keyword));
        listener.setTWEET_COUNT(listener.getTWEET_COUNT() + 1);
    }


}


