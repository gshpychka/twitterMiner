import org.apache.logging.log4j.LogManager;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;

public class MyRateLimitStatusListener implements RateLimitStatusListener {

    public void onRateLimitStatus(RateLimitStatusEvent event) {
    }

    @Override
    public void onRateLimitReached(RateLimitStatusEvent event) {
        try {
            int secondsUntilReset = event.getRateLimitStatus().getSecondsUntilReset();
            LogManager.getLogger().trace("Rate limit reached, going to wait for " + secondsUntilReset/60 + "m" + secondsUntilReset%60 + "s");
            synchronized (this) {
                Thread.sleep((long) ((secondsUntilReset+2) * 1000));
            }
        } catch (InterruptedException e) {
            LogManager.getLogger().error("Waiting for the ratelimit interrupted.");
        }
    }
}
