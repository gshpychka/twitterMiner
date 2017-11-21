import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


public class AverageCalculator {
  private Logger logger;
  private String keyword;
  private final long beginningTime = 1486166400;

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
      correctTime = status.getTimestamp() / 1000;
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
    final int precision = 2;

    StatelessSession session = HibernateSessionFactory.factory.openStatelessSession();
    logger.trace("Here we go");
    long startTime = getLatestTime(period);
    ScrollableResults allTweets = session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp >= :startTime ORDER BY timestamp ASC")
        .setParameter("startTime",startTime)
        .setFetchSize(100)
        .setReadOnly(true)
        .scroll(ScrollMode.FORWARD_ONLY);
    Transaction tx = session.beginTransaction();

    int data = 0;
    int counter = 0;
    StatusPOJO currentTweet;

    logger.trace("About to enter the loop, startTime=" + startTime);

    BigDecimal datapoint = new BigDecimal(0);
    Timeframe timeframe = new Timeframe(startTime, period);
    AverageDataPoint averageDataPoint = new AverageDataPoint(timeframe, datapoint, keyword);

    while (allTweets.next()) {
      logger.trace("Inside the loop");
      currentTweet = (StatusPOJO) allTweets.get(0);

      if (currentTweet.getTimestamp() < (startTime + period)) {
        logger.trace("Tweet is from the current minute");
        if (currentTweet.getText().contains(keyword)) {
          data += 100;
          logger.debug("Tweet contains keyword, data=" + data);
        }
        counter++;
        logger.trace("Counter incremented, value is " + counter);
      } else {
        logger.trace("Minute finished");
        logger.debug("Saving datapoint, " + data + "/" + counter);
        if (counter > 100) {
          datapoint = BigDecimal.valueOf(data).divide(BigDecimal.valueOf(counter), precision, RoundingMode.HALF_UP);
          timeframe.setStartTime(startTime);
          averageDataPoint.setDataPoint(datapoint);
          averageDataPoint.setKeyword(keyword);
          averageDataPoint.setTimeframe(timeframe);
          logger.debug("The datapoint is " + datapoint);
          DatabaseWriter.persist(averageDataPoint);
          //session.flush();
        } else {
          logger.debug("Less than 100 tweets for this minute");
        }

        startTime += period;
        data = 0;
        counter = 0;
        logger.debug("Next startTime is " + startTime);

      }
      // session.evict(currentTweet);
      // session.clear();
    }
    tx.commit();
    session.close();
    //ChartDataProvider.populateChartData();
  }

  long getLatestTime(int period) {
    StatelessSession session = HibernateSessionFactory.factory.openStatelessSession();
    ScrollableResults results = session.createQuery("SELECT D FROM AverageDataPoint D WHERE period = :period AND keyword = :keyword ORDER BY startTime DESC").setParameter("period", period).setParameter("keyword", this.keyword).setFetchSize(1).setMaxResults(1).setReadOnly(true).scroll(ScrollMode.FORWARD_ONLY);
    if (results.next() && results.get(0) != null) {
      AverageDataPoint dataPoint = (AverageDataPoint) results.get(0);
      session.close();

      return dataPoint.getStartTime();
    } else {
      return beginningTime;
    }
  }

  public static void main(String[] args) {
    AverageCalculator averageCalculator = new AverageCalculator("impeach");

    System.out.println(averageCalculator.getLatestTime(60));
  }
}
