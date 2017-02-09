import twitter4j.User;

/**
 * Created by glebu on 09-Feb-17.
 */
class UserPOJO {
    private String handle;
    private String name;
    private long userID;

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    private int followers;

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    public long getUserID() {
        return userID;
    }

    public int getFollowers() {
        return followers;
    }
    UserPOJO(){}
    UserPOJO(User user) {
        this.handle = user.getName();
        this.name = user.getScreenName();
        this.userID = user.getId();
        this.followers = user.getFollowersCount();

    }
}
