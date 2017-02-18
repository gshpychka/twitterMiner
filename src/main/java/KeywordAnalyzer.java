import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 *  Analyzes the occurence of totalCounter keyword in the tweets
 */
class KeywordAnalyzer implements Runnable {
    private String keyword;
    private SessionFactory sessionFactory;
    private Session session;
    static BigDecimal totalAverage = new BigDecimal("1000").setScale(2,BigDecimal.ROUND_HALF_UP);
    KeywordAnalyzer(String keyword) {
        this.keyword = keyword;
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    private ScrollableResults getTweetsSince(long startTime, Session session) {
        return session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp > :startTime ORDER BY timestamp ASC")
                .setParameter("startTime", startTime)
                .setFetchSize(10) //need to tweak this
                .scroll(ScrollMode.FORWARD_ONLY);
    }

    public void run() {
        int data=0;
        int minuteCounter=0;
        BigDecimal minuteAverage;
        List<BigDecimal> averages = new ArrayList<>();
        StatusPOJO statusPOJO;
        long analysisPeriod = 60*60; //24 hours
        long startTime = new Date().getTime()/1000 - analysisPeriod;
        int persistCounter;
        while (true) {
            persistCounter=1;
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            ScrollableResults results = getTweetsSince(startTime,session);
            while (results.next()) {
                statusPOJO = (StatusPOJO) results.get(0);
                assert((statusPOJO.getTimestamp() - startTime) > 0);
                if ((statusPOJO.getTimestamp() - startTime) < 60 ) {
                    if (statusPOJO.getText().contains(this.keyword)) {
                        data += 100;
                    }
                    minuteCounter++;
                } else {
                    if (minuteCounter == 0)
                        minuteAverage = new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP);
                    else
                        minuteAverage = new BigDecimal(Double.toString((double)data / minuteCounter)).setScale(2,BigDecimal.ROUND_HALF_UP);
                    data = 0;
                    minuteCounter = 0;

                    session.save(new DataPointAverage(startTime, minuteAverage));
                    persistCounter++;
                    if (persistCounter % 100 == 0) {
                        session.flush();
                        session.clear();
                        System.out.println("Cleared session");
                    }
                    startTime += 60;

                    if (averages.size() < (analysisPeriod / 60)) {
                        averages.add(minuteAverage);
                    } else if (averages.size() == (analysisPeriod / 60)) {
                        if (totalAverage.intValue()!=1000) {
                            totalAverage = totalAverage.subtract(averages.get(0).divide(new BigDecimal(averages.size()),2,BigDecimal.ROUND_HALF_UP)).add(minuteAverage.divide(new BigDecimal(averages.size()),2,BigDecimal.ROUND_HALF_UP));
                            // ^ totalAverage = totalAverage - firstElement/averages.size() + newElement/averages.size()
                            averages.remove(0);
                            averages.add(minuteAverage);
                            System.out.println("Minute: " + minuteAverage);
                        }
                    } else {
                        averages.remove(0);
                        System.out.println("error, more than ");
                    }
                }

                session.evict(statusPOJO);
            }
            tx.commit();
            session.close();

            if (totalAverage.intValue() == 1000) {
                BigDecimal sum = new BigDecimal("0").setScale(3,BigDecimal.ROUND_HALF_UP);
                for (BigDecimal bd : averages) {
                    sum = sum.add(bd);
                }
                totalAverage = sum.divide(new BigDecimal(averages.size()),2,BigDecimal.ROUND_HALF_UP);
                System.out.println("After for loop: " + totalAverage.toString());
            }
            while ((new Date().getTime() / 1000 - startTime) < 60) {}
            System.out.println(totalAverage.toString());
        }
    }
}
