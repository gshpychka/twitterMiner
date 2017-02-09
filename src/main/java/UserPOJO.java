import twitter4j.User;

/**
 * POJO that contains the user data that we want to persist
 */
class UserPOJO {
    private String handle;
    private String name;
    private long userid;

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserID(long userid) {
        this.userid = userid;
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
        return userid;
    }

    public int getFollowers() {
        return followers;
    }
    UserPOJO(){}
    UserPOJO(User user) {
        this.handle = user.getName();
        this.name = user.getScreenName();
        this.userid = user.getId();
        this.followers = user.getFollowersCount();

    }
}
