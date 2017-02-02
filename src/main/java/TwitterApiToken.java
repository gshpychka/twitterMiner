/**
 * Created by glebu on 02-Feb-17.
 */
class TwitterApiToken {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;
    TwitterApiToken(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    String getConsumerSecret() {

        return consumerSecret;
    }

    String getAccessToken() {

        return accessToken;
    }

    String getConsumerKey() {

        return consumerKey;
    }
}
