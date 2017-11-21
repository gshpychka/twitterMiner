
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;

import java.io.IOException;

/**
 * Created by glebu on 02-Feb-17.
 */
class MyStatusListener implements StatusListener {
    private static Logger logger = LogManager.getLogger();
    private int i = 0;
    private TwitterStreamReceiver twitterStreamReceiver;
    private String keyword;

    MyStatusListener(TwitterStreamReceiver twitterStreamReceiver, String keyword) {
        this.twitterStreamReceiver = twitterStreamReceiver;
        this.keyword = keyword;
    }
    public void onStatus(Status status) {
        if(status.getQuotedStatus() == null) {
            twitterStreamReceiver.processTweet(status);
            logger.info(++i + " in this session. Keyword: " + keyword);
        }
    }
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
    public void onException(Exception ex) {
        logger.error(ex);
    }
    public void onStallWarning(StallWarning stallWarning) {}
    public void onScrubGeo(long l, long l1) {}
}
