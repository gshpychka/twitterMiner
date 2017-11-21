import twitter4j.User;

/**
 * POJO that contains the user data that we want to persist
 */
class UserPOJO {
    private String handle;
    private String name;
    private long userID;
    private int followers;

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserID(long userid) {
        this.userID = userid;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

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
        if (user.getName().length() >= 45) {
            this.handle = user.getName().substring(0, 44);
        } else {
            this.handle = user.getName();
        }
        this.name = user.getScreenName();
        this.userID = user.getId();
        this.followers = user.getFollowersCount();

    }
}
