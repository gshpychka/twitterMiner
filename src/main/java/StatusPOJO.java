import twitter4j.Status;

/**
 * Created by glebu on 08-Feb-17.
 */
class StatusPOJO{

    private long tweetID;
    private long userID;
    private int retweets;
    private int favorites;
    private long timestamp;
    private String text;
    private boolean isRetweet;

    StatusPOJO(Status status) {
        this.tweetID = status.getId();
        this.userID = status.getUser().getId();
        this.retweets = status.getRetweetCount();
        this.favorites = status.getFavoriteCount();
        this.timestamp = status.getCreatedAt().getTime();
        this.text = status.getText();
        this.isRetweet = status.isRetweet();
    }
}
