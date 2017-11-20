
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class ChartDataProvider {
  public static void main(String[] args) {
    SessionFactory hibernateSessionFactory = HibernateSessionFactory.factory;
    Session session = hibernateSessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    ScrollableResults results = session
        .createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60")
        .setFetchSize(10)
        .scroll(ScrollMode.FORWARD_ONLY);
    AverageDataPoint averageDataPoint;
    List<BigDecimal> dataPoints = new ArrayList<>();
    long startTime = 1486166400;
    int dataPointsInThis12hrs = 0;
    BigDecimal newDataPoint;
    BigDecimal newDataPointSum = new BigDecimal(0);
    while (results.next()) {
      averageDataPoint = (AverageDataPoint) results.get(0);
      if (((averageDataPoint.getStartTime() - startTime) / (60 * 60 * 12)) < 12) { //less than 12 hours processed
        dataPoints.add(averageDataPoint.getDataPoint());
      } else {
        for (BigDecimal value : dataPoints) {
          dataPointsInThis12hrs++;
          newDataPointSum = newDataPointSum.add(value);
        }
        dataPoints.clear();
        if(dataPointsInThis12hrs > 0){
          newDataPoint = newDataPointSum.divide(BigDecimal.valueOf(dataPointsInThis12hrs), 2, BigDecimal.ROUND_HALF_UP);
          DatabaseWriter.persist(new AverageDataPoint(new Timeframe(startTime, 60 * 60 * 12), newDataPoint, "impeach"));
          System.out.println("Recording 12hrs datapoint " + startTime + ", " + newDataPoint + ", " + newDataPointSum + "/" + dataPointsInThis12hrs);
        } else {
          System.out.println("No datapoints for this 12hrs");
        }

        startTime+=60*60*12;
        dataPointsInThis12hrs=0;
        newDataPointSum = new BigDecimal(0);
      }
    }



  }


  static String getChartData(long start, long finish) {
    Session session = HibernateSessionFactory.factory.openSession();
    Transaction tx = session.beginTransaction();
    String chartData = "";
    ScrollableResults results = session
        .createQuery("SELECT D FROM AverageDataPoint D WHERE period = 60*60*12 AND startTime > :start AND startTime < :finish ORDER BY startTime ASC")
        .setParameter("start", start)
        .setParameter("finish", finish)
        .setFetchSize(10)
        .scroll(ScrollMode.FORWARD_ONLY);
    AverageDataPoint averageDataPoint;
    int i = 0;
    while (results.next()) {
      averageDataPoint = (AverageDataPoint) results.get(0);
      chartData = chartData.concat("[new Date(" + averageDataPoint.getStartTime()*1000 + "), " + averageDataPoint.getDataPoint() + "],");
      i++;
    }
    chartData = chartData.substring(0, chartData.length() - 1);
    return chartData;

  }

}
