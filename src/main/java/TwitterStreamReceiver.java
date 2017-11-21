import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Receives and processes the twitter stream
 */
class TwitterStreamReceiver {
  private String keyword;
  private TwitterStream twitterStream;
  private MyStatusListener listener;
  private List<Status> statusList = new ArrayList<>();
  private long time;
  private org.apache.logging.log4j.Logger logger;

  TwitterStreamReceiver(String keyword) {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
    listener = new MyStatusListener(this, keyword);
    this.twitterStream.addListener(listener);
    trackKeyword(keyword);
    this.keyword = keyword;
    this.time = new Date().getTime()/1000;
    this.logger = org.apache.logging.log4j.LogManager.getLogger();
  }

  private void trackKeyword(String keyword) {
    this.keyword = keyword;
    FilterQuery filterQuery = new FilterQuery();
    filterQuery.track(keyword);
    twitterStream.filter(filterQuery);
  }

  void processTweet(Status status) {

    if (status.isRetweet()) {
      status = status.getRetweetedStatus();
    } else {
      statusList.add(status);
    }

    if ((new Date().getTime() / 1000 - time) >= 60) { // a minute has passed since we last recorded a data point
      int data = 0;
      int counter = 0;
      for (Status currentTweet : statusList) {
        counter++;
        if (currentTweet.getText().contains("impeach")) {
          data+=100;
        }
      }
      statusList.clear();
      if (counter != 0) {
        BigDecimal datapoint = BigDecimal.valueOf(data).divide(BigDecimal.valueOf(counter), 2, BigDecimal.ROUND_HALF_UP);
        Timeframe timeframe = new Timeframe(time,60);
        time+=60;
        AverageDataPoint averageDataPoint = new AverageDataPoint();
        averageDataPoint.setDataPoint(datapoint);
        averageDataPoint.setKeyword(keyword);
        averageDataPoint.setTimeframe(timeframe);
        logger.debug("The datapoint is " + datapoint);
        DatabaseWriter.persist(averageDataPoint);
      }
    }


    DatabaseWriter.writeTweet(new StatusPOJO(status, keyword));
  }


}


