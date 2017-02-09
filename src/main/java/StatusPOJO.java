import twitter4j.Status;
import twitter4j.User;

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
    private User user;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public User getUser() {
        return user;
    }

    public void setTweetID(long tweetID) {
        this.tweetID = tweetID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StatusPOJO(){}
    StatusPOJO(Status status, String keyword) {
        this.tweetID = status.getId();
        this.userID = status.getUser().getId();
        this.retweets = status.getRetweetCount();
        this.favorites = status.getFavoriteCount();
        this.timestamp = status.getCreatedAt().getTime();
        this.text = status.getText();
        this.user = status.getUser();
        this.keyword = keyword;
    }

    public long getTweetID() {
        return tweetID;
    }

    public long getUserID() {
        return userID;
    }

    public int getRetweets() {
        return retweets;
    }

    public int getFavorites() {
        return favorites;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

}
