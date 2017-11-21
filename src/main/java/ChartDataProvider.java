import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ChartDataProvider {
  private String keyword;
  private String chartData = "";
  private AverageCalculator averageCalculator;
  private boolean dataPointsPopulated;

  public static void populateChartData() {
    SessionFactory hibernateSessionFactory = HibernateSessionFactory.factory;
    Session session = hibernateSessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    long startTime = new AverageCalculator("impeach").getLatestTime(60*60*12);

    ScrollableResults results = session.createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60 AND startTime >= :startTime")
        .setParameter("startTime", startTime)
        .setFetchSize(10)
        .scroll(ScrollMode.FORWARD_ONLY);

    AverageDataPoint averageDataPoint;


    int dataPointsInThis12hrs = 0;

    BigDecimal newDataPointSum = new BigDecimal(0);
    while (results.next()) {

      averageDataPoint = (AverageDataPoint) results.get(0);

      if (((averageDataPoint.getStartTime() - startTime) / (60 * 60)) < 12) { //less than 12 hours processed
        newDataPointSum = newDataPointSum.add(averageDataPoint.getDataPoint());
        dataPointsInThis12hrs++;
      } else {

        if (dataPointsInThis12hrs > 0) {
          BigDecimal newDataPoint = newDataPointSum.divide(BigDecimal.valueOf(dataPointsInThis12hrs), 2, BigDecimal.ROUND_HALF_UP);
          DatabaseWriter.persist(new AverageDataPoint(new Timeframe(startTime, 60 * 60 * 12), newDataPoint, "impeach"));
          System.out.println("Recording 12hrs datapoint " + startTime + ", " + newDataPoint + ", " + newDataPointSum + "/" + dataPointsInThis12hrs);
        } else {
          System.out.println("No datapoints for this 12hrs");
        }

        startTime += 60 * 60 * 12;
        dataPointsInThis12hrs = 0;
        newDataPointSum = new BigDecimal(0);
      }
    }
    tx.commit();
    session.close();

  }
  //private String computeChartData
  String getChartData(long start, long finish) {

    //long latestData = averageCalculator.getLatestTime(60*60*12);

    //if ((new Date().getTime()/1000 - latestData) > 60 * 60 * 12) {
      populateChartData();
      Session session = HibernateSessionFactory.factory.openSession();
      Transaction tx = session.beginTransaction();

      ScrollableResults results = session.createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60*60*12 AND startTime > :start AND startTime < :finish AND keyword = :keyword ORDER BY startTime ASC")
          .setParameter("start", start)
          .setParameter("finish", finish)
          .setParameter("keyword", keyword)
          .setFetchSize(10)
          .setReadOnly(true)
          .scroll(ScrollMode.FORWARD_ONLY);

      AverageDataPoint averageDataPoint;
      NewsStories newsStories = new NewsStories();
      chartData = "";
      while (results.next()) {
        averageDataPoint = (AverageDataPoint) results.get(0);
        chartData = chartData.concat("[new Date(" + averageDataPoint.getStartTime() * 1000 + "), " + averageDataPoint.getDataPoint() + ", " + newsStories.getStory(averageDataPoint.getStartTime()) + "],");
      }

      chartData = chartData.substring(0, chartData.length() - 1); //remove the trailing comma
      tx.commit();
      session.close();
    //}
    return chartData;

  }

  ChartDataProvider(String keyword) {
    this.keyword = keyword;
    this.averageCalculator = new AverageCalculator(keyword);
    populateChartData();
  }

}
