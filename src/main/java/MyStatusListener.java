
import twitter4j.*;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {
    private String username;
    private String tweet;
   // private Database mysql;
    private int i =0;
    private TwitterStreamWriter writer;



    MyStatusListener(TwitterStreamWriter writer) {
        this.writer = writer;
    }
    public void onStatus(Status status) {
        this.username = status.getUser().getName();
        this.tweet = status.getText();
        long time = status.getCreatedAt().getTime();
        if(status.getQuotedStatus() == null) {

            System.out.println(i + ": @" + status.getUser().getScreenName() + ": "+ tweet +", Posted at: " + time);

            this.writer.writeTweet(status);

            i++;
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
