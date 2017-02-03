import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;

import java.util.Date;

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
       // this.mysql = mysql;
        this.writer = writer;
    }
    public void onStatus(Status status) {
        this.username = EmojiParser.removeAllEmojis(status.getUser().getName());
        this.tweet = EmojiParser.removeAllEmojis(status.getText());
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
