import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;


public class AverageCalculator {
    private StatelessSession session = HibernateSessionFactory.factory.openStatelessSession();
    private Logger logger;
    private String keyword;

    public AverageCalculator(String keyword) {
        this.keyword = keyword;
        this.session = HibernateSessionFactory.factory.openStatelessSession();
        this.logger = LogManager.getLogger();
    }

    protected void finalize() {
        session.close();
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

    BigDecimal calculateAverage(ScrollableResults results, String keyword) {
        logger.trace("Entering calculateAverage()");
        int precision = 4;
        int data=0;
        StatusPOJO statusPOJO;
        int counter=0;

        while (results.next()) {
            logger.debug("calculateAverage() has a next result");
            statusPOJO = (StatusPOJO) results.get(0);
            if(statusPOJO.getText().contains(keyword)){
                data+=100;
                logger.debug("Tweet contains the keyword \"" + keyword + "\"");
            }
            counter++;
        }
        if (counter == 0) {
            counter = 1;
            logger.warn("Counter was zero. This means no tweets for the period. Should not happen");
        }
        logger.trace("Exiting calculateAverage()");
        results.close();
        return new BigDecimal(data,new MathContext(precision)).divide(new BigDecimal(counter),BigDecimal.ROUND_HALF_UP);
    }

    AverageDataPoint calculateDataPoint(Timeframe timeFrame, String keyword) {
        logger.trace("Inside of calculateDataPoint(). The timeframe is " + timeFrame.getStartTime() + " to " + timeFrame.getEndTime() + ", the keyword is " + keyword);
        ScrollableResults tweets = getTweetsForPeriod(timeFrame);
        AverageDataPoint returnValue = new AverageDataPoint(timeFrame, calculateAverage(tweets,keyword),keyword);
        session.getTransaction().commit();
        tweets.close();
        logger.trace("Transaction committed");
        return returnValue;
    }

    ScrollableResults getTweetsForPeriod(Timeframe timeframe) {
        logger.trace("Entering getTweetsForPeriod(). The timeframe is: " + timeframe.getStartTime() + " to " + timeframe.getEndTime());
        session.beginTransaction();
        logger.trace("Transaction begun");
        ScrollableResults results = session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp >= :startTime AND timestamp < :endTime")
                .setParameter("startTime", timeframe.getStartTime())
                .setParameter("endTime", timeframe.getEndTime())
                .setFetchSize(10)
                .setReadOnly(true)
                .scroll(ScrollMode.FORWARD_ONLY);
        logger.trace("Got the results, returning");
        return results;
    }

    void populateDataPoints(int period) {
        logger.trace("Entering populateDataPoints()");

        long startTime;
        Transaction tx;

        //Session session = HibernateSessionFactory.factory.openSession();
        ScrollableResults datapointResults = session.createQuery("SELECT S FROM AverageDataPoint S WHERE period != :period")
                .setParameter("period", period)
                .setFetchSize(10)
                .scroll(ScrollMode.FORWARD_ONLY);
        tx = session.beginTransaction();
        logger.trace("Transaction begun");
        logger.trace("Got the initial test results, checking if there are any datapoints with a wrong period");
        while (datapointResults.next()) {
            AverageDataPoint dataPoint = (AverageDataPoint)datapointResults.get(0);
            session.delete(dataPoint);
            logger.trace("Deleting a datapoint with a start time of " + dataPoint.getStartTime() + " and a period of" + dataPoint.getPeriod() + ", should be " + "period");
        }

        tx.commit();
        logger.trace("Transaction committed");

        datapointResults = session.createQuery("SELECT S FROM AverageDataPoint S ORDER BY startTime DESC")
                .setFetchSize(1)
                .setMaxResults(1)
                .scroll(ScrollMode.FORWARD_ONLY);
        tx = session.beginTransaction();
        logger.trace("Transaction begun");
        logger.trace("All the records in the table, if any, should have correct periods.");
        if (datapointResults.next()) {
            AverageDataPoint averageDataPoint = (AverageDataPoint) datapointResults.get(0);
            startTime = averageDataPoint.getStartTime();
            logger.debug("The latest startTime in the table is " + startTime);
        } else {
            //startTime = getFirstTweetEver().getTimestamp();
            startTime = 1486166400;
            logger.debug("No records in the table, set the startTime to the very first value of " + startTime);
        }
        tx.commit();
        logger.trace("Transaction committed");
        //session.close();

        long currentTime = new Date().getTime()/1000;
        while (startTime < currentTime) {
            logger.debug("About to calculate and record a data point with the startTime of "+ startTime + ", current time is " + currentTime +", difference is " + (currentTime-startTime));
            DatabaseWriter.persist(calculateDataPoint(new Timeframe(startTime,period), keyword));
            startTime+=period;
        }
        logger.trace("Exiting populateDataPoints()");
    }

    StatusPOJO getFirstTweetEver() {
        StatelessSession session = HibernateSessionFactory.factory.openStatelessSession();
        logger.trace("Entering getFirstTweetEver()");
        Transaction tx = session.beginTransaction();
        ScrollableResults results =
                session.createQuery("SELECT S FROM StatusPOJO S ORDER BY timestamp ASC")
                .setFirstResult(0)
                .setMaxResults(1)
                .setFetchSize(0)
                .setReadOnly(true)
                .scroll(ScrollMode.FORWARD_ONLY);
        logger.trace("Got the results");


        if (results.next()) {
            StatusPOJO statusPOJO = (StatusPOJO) results.get(0);
            tx.commit();
            logger.trace("Committed the transaction");
            results.close();
            session.close();
            return statusPOJO;
        } else {
            tx.commit();
            logger.trace("Committed the transaction");
            results.close();
            session.close();
            throw new HibernateException("No tweets in the database");
        }
    }

}
