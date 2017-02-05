
import twitter4j.*;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {
    static int RETWEET_CCOUNT = 0;
    static int TWEET_COUNT = 0;
    private int i =0;
    int gc = 0;
    private TwitterStreamReceiver twitterStreamReceiver;
    MyStatusListener(TwitterStreamReceiver twitterStreamReceiver) {
        this.twitterStreamReceiver = twitterStreamReceiver;
    }
    public void onStatus(Status status) {
        if(status.getQuotedStatus() == null) {
            twitterStreamReceiver.processTweet(status);
            System.out.println(++i + " total. Original tweets: " + TWEET_COUNT + " ("+ (TWEET_COUNT * 100 )/i +"%). Retweet count: " + RETWEET_CCOUNT + " (" + (RETWEET_CCOUNT * 100)/i + "%).");
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
}
