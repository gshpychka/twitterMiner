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
    private String result="";

    KeywordAnalyzer(String keyword) {
        this.keyword = keyword;
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    String getResult() {
        return result;
    }
    ScrollableResults getTweetsSince(long startTime, HibernateInit hibernate) {
        Session session = hibernate.getSession();
        return session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp > :startTime ORDER BY timestamp ASC")
                .setParameter("startTime", startTime)
                .setFetchSize(10) //need to tweak this
                .scroll(ScrollMode.FORWARD_ONLY);
    }

    public void run() {
        int data=0;
        int minuteCounter=0;
        BigDecimal minuteAverage;
        BigDecimal totalAverage = new BigDecimal("1000").setScale(2,BigDecimal.ROUND_HALF_UP);
        assert totalAverage.intValue()==1000; // debugging
        List<BigDecimal> averages = new ArrayList<>();
        StatusPOJO statusPOJO;
        long analysisPeriod = 60*60*24; //24 hours
        long startTime = new Date().getTime()/1000 - analysisPeriod;
        HibernateInit hibernate;
        while (true) {
            ScrollableResults results = getTweetsSince(startTime, hibernate = new HibernateInit());
            while (results.next()) {
                statusPOJO = (StatusPOJO) results.get(0);
                if((statusPOJO.getTimestamp() - startTime) > 0) {
                    if ((statusPOJO.getTimestamp() - startTime) < 60 ) {
                        if (statusPOJO.getText().contains(this.keyword)) {
                            data += 100;
                        }
                        minuteCounter++;
                    } else {
                        if (minuteCounter == 0)
                            minuteAverage = new BigDecimal("0.0").setScale(2,BigDecimal.ROUND_HALF_UP);
                        else
                            minuteAverage = new BigDecimal((double)data / minuteCounter).setScale(2,BigDecimal.ROUND_HALF_UP);
                        averages.add(minuteAverage);
                        data = 0;
                        startTime += 60;
                        minuteCounter = 0;
                        if (averages.size() < analysisPeriod / 60) {
                            averages.add(minuteAverage);
                        } else if (averages.size() == analysisPeriod / 60) {
                            if (totalAverage.intValue()!=1000) {
                                totalAverage = totalAverage.subtract(averages.get(0).divide(new BigDecimal(averages.size()))).add(minuteAverage.divide(new BigDecimal(averages.size())));
                                // ^ totalAverage = totalAverage - firstElement/averages.size() + newElement/averages.size()
                                averages.remove(0);
                                averages.add(minuteAverage);
                            } else {
                                BigDecimal sum = new BigDecimal("0").setScale(2,BigDecimal.ROUND_HALF_UP);
                                for (BigDecimal bd : averages) {
                                    sum.add(bd);
                                }

                            }
                            System.out.println(totalAverage.toString());
                        } else {
                            averages.remove(0);
                        }
                    }
                }
                hibernate.getSession().evict(statusPOJO);
            }
            hibernate.getTransaction().commit();
            hibernate.getSession().close();
            while ((new Date().getTime() / 1000 - startTime) < 60) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println(" Total: " + containsCounter + ". Total entries: " + totalCounter + ". Week average: " + weekAverage + ". Number of minutes: " + averages.size() + ". The analysis took " + ((new Date().getTime()/1000) - beginTime) + " seconds.");

        //result = "\""+keyword+"\" occurs " + containsCounter + " times (" + Double.toString((double)(containsCounter *100)/ totalCounter) + "%). I am totalCounter work in progress, so excuse the precision. Total tweets analyzed: "+ totalCounter +".\n";
        //System.out.println(result);
    }
}
