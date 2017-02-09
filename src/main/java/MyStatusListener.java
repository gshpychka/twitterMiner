
import twitter4j.*;

import java.io.IOException;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {
    private int RETWEET_CCOUNT;
    private int TWEET_COUNT;
    private int i =0;
    private int gc = 0;
    private TwitterStreamReceiver twitterStreamReceiver;
    private String keyword;

    MyStatusListener(TwitterStreamReceiver twitterStreamReceiver, String keyword) {
        this.twitterStreamReceiver = twitterStreamReceiver;
        this.keyword = keyword;
    }
    public void onStatus(Status status) {
        if(status.getQuotedStatus() == null) {
            twitterStreamReceiver.processTweet(status);
            System.out.println(++i + " total. Original tweets: " + TWEET_COUNT + " ("+ (TWEET_COUNT * 100 )/i +"%). Retweet count: " + RETWEET_CCOUNT + " (" + (RETWEET_CCOUNT * 100)/i + "%). Keyword: " + keyword);
            gc++;
        }
        if (gc > 5000) {
            System.gc();
            gc=0;
        }
    }
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
    public void onException(Exception ex) {
        ex.printStackTrace();
    }
    public void onStallWarning(StallWarning stallWarning) {}
    public void onScrubGeo(long l, long l1) {}
    public int getRETWEET_CCOUNT() {
        return RETWEET_CCOUNT;
    }
    public void setRETWEET_CCOUNT(int RETWEET_CCOUNT) {
        this.RETWEET_CCOUNT = RETWEET_CCOUNT;
    }
    public int getTWEET_COUNT() {
        return TWEET_COUNT;
    }
    public void setTWEET_COUNT(int TWEET_COUNT) {
        this.TWEET_COUNT = TWEET_COUNT;
    }
}
