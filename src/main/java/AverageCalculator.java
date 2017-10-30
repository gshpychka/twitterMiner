import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;


public class AverageCalculator {
    BigDecimal calculateAverage(ScrollableResults results, String keyword) {
        int precision =2;
        int data=0;
        StatusPOJO statusPOJO;
        int counter=0;

        while (results.next()) {
            statusPOJO = (StatusPOJO) results.get(0);
            if(statusPOJO.getText().contains(keyword)){
                data+=100;
            }
            counter++;
        }
        return new BigDecimal(data,new MathContext(precision)).divide(new BigDecimal(counter),BigDecimal.ROUND_HALF_UP);
    }

    AverageDataPoint calculateDataPoint(Timeframe timeFrame, String keyword) {
        return new AverageDataPoint(timeFrame, calculateAverage(getTweetsForPeriod(timeFrame),keyword),keyword);
    }

    ScrollableResults getTweetsForPeriod(Timeframe timeframe) {
        Session session = HibernateSessionFactory.factory.openSession();
        Transaction tx = session.beginTransaction();
        ScrollableResults results =
                session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp > :startTime AND timestamp < :endTime ORDER BY timestamp ASC")
                .setParameter("startTime", timeframe.getStartTime())
                .setParameter("endTime", timeframe.getEndTime())
                .setFetchSize(10)
                .scroll(ScrollMode.FORWARD_ONLY);
        tx.commit();
        session.close();
        return results;
    }

    void populateDataPoints(int period, String keyword) {
        long startTime = new Date().getTime()/1000; // set to current time

        Session session = HibernateSessionFactory.factory.openSession();
        ScrollableResults datapointResults = session.createQuery("SELECT S FROM AverageDataPoint S WHERE period != :period")
                .setParameter("period", period)
                .setFetchSize(10)
                .scroll(ScrollMode.FORWARD_ONLY);
        Transaction tx = session.beginTransaction();
        while (datapointResults.next()) {
            session.delete(datapointResults.get(0));
        }
        tx.commit();

        datapointResults = session.createQuery("SELECT S FROM AverageDataPoint S ORDER BY startTime DESC")
                .setFetchSize(1)
                .scroll(ScrollMode.FORWARD_ONLY);
        tx = session.beginTransaction();
        if (datapointResults.next()) {
            AverageDataPoint averageDataPoint = (AverageDataPoint) datapointResults.get(0);
            startTime = averageDataPoint.getStartTime();
        }
        tx.commit();
        session.close();

        long currentTime = new Date().getTime()/1000;
        while (startTime < currentTime) {
            DatabaseWriter.persist(calculateDataPoint(new Timeframe(startTime,period), keyword));
            startTime+=period;
        }



    }

}
