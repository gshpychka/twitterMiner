import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.logging.Level;

/**
 *  Analyzes the occurence of a keyword in the tweets
 */
class KeywordAnalyzer implements Runnable {
    private String keyword;
    private SessionFactory sessionFactory;
    private String result;
    private int i=0;
    private int a=0;
    KeywordAnalyzer(String keyword) {
        this.keyword = keyword;
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }

    public String getResult() {
        return result;
    }

    public void run() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ScrollableResults results = session.createQuery("SELECT S FROM StatusPOJO S")
                .setFetchSize(100)
                .scroll(ScrollMode.FORWARD_ONLY);

        StatusPOJO statusPOJO;
        while (results.next()) {
            statusPOJO = (StatusPOJO) results.get(0);
                if(statusPOJO.getText().contains(this.keyword)){
                    i++;
                }
                ++a;
                //System.out.println(" Total: " + i + ". Total entries: " + ++a);
                session.evict(statusPOJO);
        }
        transaction.commit();
        session.close();
        result = "\""+keyword+"\" occurs " + i + " times (" + Double.toString((double)((i*100)/a)) + "%). Total tweets analyzed: "+a+".\n";

    }
}
