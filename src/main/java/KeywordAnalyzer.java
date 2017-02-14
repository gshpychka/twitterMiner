import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 *  Analyzes the occurence of totalCounter keyword in the tweets
 */
class KeywordAnalyzer implements Runnable {
    private String keyword;
    private SessionFactory sessionFactory;
    private String result="";

    //static ExecutorService executor = Executors.newSingleThreadExecutor();
    KeywordAnalyzer(String keyword) {
        this.keyword = keyword;
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    String getResult() {
        return result;
    }

    public void run() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        long startTime = (new Date().getTime()/1000) - 60*60*24*7;
        ScrollableResults results = session.createQuery("SELECT S FROM StatusPOJO S WHERE timestamp > :startTime")
                .setParameter("startTime", startTime) //set start time to be current time - 1 week
                .setFetchSize(10)
                .scroll(ScrollMode.FORWARD_ONLY);

        StatusPOJO statusPOJO;

        int data=0;
        int containsCounter =0;
        int totalCounter = 0;
        int minuteCounter = 0;
        int minuteAverage;
        List<Integer> averages = new ArrayList<>();
        double weekAverage;
        while (results.next()) {
            statusPOJO = (StatusPOJO) results.get(0);
            if((statusPOJO.getTimestamp() - startTime) < 60 && (statusPOJO.getTimestamp() - startTime) > 0){
                if(statusPOJO.getText().contains(this.keyword)){
                    containsCounter++;
                    data+=1000;
                }
                minuteCounter++;
                totalCounter++;
            } else {
                if(minuteCounter == 0)
                    minuteAverage = 0;
                else
                    minuteAverage = data/minuteCounter;
                averages.add(minuteAverage);
                data = 0;
                startTime += 60;
                minuteCounter = 0;
            }
            session.evict(statusPOJO);
        }
        transaction.commit();
        session.close();
        int sum =0;
        for (int i : averages) {
            sum += i;
        }
        weekAverage = (double) sum/averages.size() ;
        System.out.println(" Total: " + containsCounter + ". Total entries: " + totalCounter + ". Week average: " + weekAverage);
        result = "\""+keyword+"\" occurs " + containsCounter + " times (" + Double.toString((double)(containsCounter *100)/ totalCounter) + "%). I am totalCounter work in progress, so excuse the precision. Total tweets analyzed: "+ totalCounter +".\n";
        //System.out.println(result);
    }
}
