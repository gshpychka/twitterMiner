
import twitter4j.*;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {

    private int i =0;
    int gc = 0;
    private TwitterStreamReceiver twitterStreamReceiver;
    MyStatusListener(TwitterStreamReceiver twitterStreamReceiver) {
        this.twitterStreamReceiver = twitterStreamReceiver;
    }
    public void onStatus(Status status) {
        if(status.getQuotedStatus() == null) {
            twitterStreamReceiver.processTweet(status);
            System.out.println(++i);
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
