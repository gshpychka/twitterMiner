import twitter4j.Status;
import twitter4j.User;

/**
 * POJO that contain the status data that we want to persist
 */
class StatusPOJO{

    private long tweetid;
    private long userid;
    private int retweets;
    private int favorites;
    private long timestamp;
    private String text;
    private User user;
    private String keyword;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    User getUser() {
        return user;
    }

    public void setTweetID(long tweetid) {
        this.tweetid = tweetid;
    }

    public void setUserID(long userid) {
        this.userid = userid;
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
        this.tweetid = status.getId();
        this.userid = status.getUser().getId();
        this.retweets = status.getRetweetCount();
        this.favorites = status.getFavoriteCount();
        this.timestamp = status.getCreatedAt().getTime();
        this.text = status.getText();
        this.user = status.getUser();
        this.keyword = keyword;
    }

    public long getTweetID() {
        return tweetid;
    }

    public long getUserID() {
        return userid;
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
