import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {
    private String username;
    private String tweet;
    private Database mysql;

    private int i =0;




    MyStatusListener(Database mysql, TwitterStream twitterStream) {
        this.mysql = mysql;


    }
    public void onStatus(Status status) {
        this.username = EmojiParser.removeAllEmojis(status.getUser().getName());
        this.tweet = EmojiParser.removeAllEmojis(status.getText());
        if(!status.isRetweet() && ( tweet.contains("Trump") || tweet.contains("Bannon"))) {
            System.out.println(i + ": " + this.username + ": " + this.tweet);
            try {
                this.mysql.insertValues(username, tweet);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
