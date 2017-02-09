import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.logging.Level;

/**
 *  Analyzes the occurence of a keyword in the tweets
 */
class KeywordAnalyzer implements Runnable {
    private String keyword;
    private SessionFactory sessionFactory;
    KeywordAnalyzer(String keyword) {
        this.keyword = keyword;
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    }
    public void run() {
        StatelessSession session = sessionFactory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("FROM StatusPOJO s WHERE s.keyword = 'Bannon'");
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        int i=0;
        int a=0;
        while (results.next()) {
            StatusPOJO statusPOJO = (StatusPOJO) results.get(0);
            i+=statusPOJO.getRetweets();
            System.out.println(statusPOJO.getRetweets() + " retweets. Total: " + i + ". Total entries: " + ++a);

        }
        transaction.commit();
        session.close();
    }
}
