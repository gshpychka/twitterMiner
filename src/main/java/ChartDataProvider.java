import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class ChartDataProvider {
  private String keyword;
  private String chartData;
  private AverageCalculator averageCalculator;
  private int hours = 24;

  private long start;
  private long finish;

  void populateChartData() {
    SessionFactory hibernateSessionFactory = HibernateSessionFactory.factory;
    Session session = hibernateSessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    long startTime = new AverageCalculator("impeach").getLatestTime(60 * 60 * hours) + 60 * 60 * hours;
    if(new Date().getTime()/1000 - startTime > 60 * 60 * hours) {

      ScrollableResults results = session.createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60 AND startTime >= :startTime ORDER BY startTime ASC").setParameter("startTime", startTime).setFetchSize(10).scroll(ScrollMode.FORWARD_ONLY);

      AverageDataPoint averageDataPoint;


      int dataPointsInThis12hrs = 0;

      BigDecimal newDataPointSum = new BigDecimal(0);
      while (results.next()) {

        averageDataPoint = (AverageDataPoint) results.get(0);

        if (((averageDataPoint.getStartTime() - startTime) / (60 * 60)) < hours) { //less than $hours hours processed
          newDataPointSum = newDataPointSum.add(averageDataPoint.getDataPoint());
          dataPointsInThis12hrs++;
        } else {

          if (dataPointsInThis12hrs > 0) {
            BigDecimal newDataPoint = newDataPointSum.divide(BigDecimal.valueOf(dataPointsInThis12hrs), 2, BigDecimal.ROUND_HALF_UP);
            DatabaseWriter.persist(new AverageDataPoint(new Timeframe(startTime, 60 * 60 * hours), newDataPoint, "impeach"));
            System.out.println("Recording " + hours + "hrs datapoint " + startTime + ", " + newDataPoint + ", " + newDataPointSum + "/" + dataPointsInThis12hrs);
          } else {
            System.out.println("No datapoints for these " + hours + "hrs");
          }

          startTime += 60 * 60 * hours;
          dataPointsInThis12hrs = 0;
          newDataPointSum = new BigDecimal(0);
        }
      }
      tx.commit();
      session.close();
    }
  }
  private String computeChartData() {
    Session session = HibernateSessionFactory.factory.openSession();
    Transaction tx = session.beginTransaction();

    ScrollableResults results = session.createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60*60*:hours AND startTime > :start AND startTime < :finish AND keyword = :keyword ORDER BY startTime ASC")
        .setParameter("start", start)
        .setParameter("finish", finish)
        .setParameter("keyword", keyword)
        .setParameter("hours", hours)
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
  String getChartData() {

    long latestData = averageCalculator.getLatestTime(60 * 60 * hours);

    if ((new Date().getTime()/1000 - latestData) > (60 * 60 * hours)) {
      populateChartData();
      chartData = computeChartData();
    }
      return chartData;
  }

  ChartDataProvider(String keyword,long start, long finish) {
    this.keyword = keyword;
    this.start = start;
    this.finish = finish;
    this.averageCalculator = new AverageCalculator(keyword);
    populateChartData();
    chartData = computeChartData();
  }

}
