import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;


public class AverageCalculator {
    private Logger logger;
    private String keyword;

    public AverageCalculator(String keyword) {
        this.keyword = keyword;
        //this.session = HibernateSessionFactory.factory.openStatelessSession();
        this.logger = LogManager.getLogger();
    }

    void correctCorruptTimestamps() {
        Session session = HibernateSessionFactory.factory.openSession();
        Transaction tx = session.beginTransaction();
        logger.trace("creating the query...");
        ScrollableResults results = session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp < 1486166400").setFetchSize(10).scroll(ScrollMode.FORWARD_ONLY);
        StatusPOJO status;

//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
//        twitter.addRateLimitStatusListener(new MyRateLimitStatusListener());
//
        long correctTime;

        while (results.next()) {
            status = (StatusPOJO) results.get(0);
            correctTime=status.getTimestamp()/1000;
            logger.debug("Deleting status with a timestamp of " + status.getTimestamp());
            status.setTimestamp(correctTime);
            session.delete(status);
//            try {
//
//                correctTime = twitter.showStatus(status.getTweetID()).getCreatedAt().getTime()/1000;
//                logger.trace("Correcting the timestamp. Wrong time: "+status.getTimestamp() + ", correct time: " + correctTime);
//                status.setTimestamp(correctTime);
//                DatabaseWriter.persist(status);
//            } catch (TwitterException e) {
//                logger.error(e.getErrorMessage());
//                DatabaseWriter.delete(status);
//            }


        }
        logger.debug("End of main()");
        tx.commit();
        session.close();
    }

    public void populateDataPoints(int period) {
        int precision = 4;
        Session session = HibernateSessionFactory.factory.openSession();
        logger.trace("Here we go");
        ScrollableResults allTweets = session.createQuery("SELECT S FROM StatusPOJO S ORDER BY timestamp ASC")
                .setFetchSize(100)
                .setReadOnly(true)
                .scroll(ScrollMode.FORWARD_ONLY);
        Transaction tx = session.beginTransaction();
        MathContext mathContext = new MathContext(precision);
        long startTime = 1486166400;
        int data=0;
        int counter=0;
        StatusPOJO currentTweet;
        logger.trace("About to enter the loop, startTime="+startTime);
        BigDecimal datapoint;
        Timeframe timeframe = new Timeframe(startTime, period);
        while (allTweets.next()) {
            logger.trace("Inside the loop");
            currentTweet = (StatusPOJO) allTweets.get(0);
            if (currentTweet.getTimestamp() < (startTime + period)) {
                logger.trace("Tweet is from the current minute");
                if (currentTweet.getText().contains(keyword)) {
                    data+=100;
                    logger.trace("Tweet contains keyword, data="+data);
                }
                counter++;
                logger.trace("Counter incremented, value is "+counter);
            } else {
                logger.trace("Minute finished");

                logger.trace("Saving datapoint, "+ data + "/" + counter);
                if(counter==0){
                    counter=1;
                    logger.debug("Counter was zero, setting to 1");
                }
                datapoint =  BigDecimal.valueOf(data).divide(BigDecimal.valueOf(counter),2,RoundingMode.HALF_UP);
                timeframe.setStartTime(startTime);
                logger.debug("The datapoint is " + datapoint);
                DatabaseWriter.persist(new AverageDataPoint(timeframe,datapoint,keyword));
                startTime+=period;
                data = 0;
                counter = 0;
                logger.trace("Next startTime is "+startTime);
                }
            }
        tx.commit();
        session.close();
    }
}
