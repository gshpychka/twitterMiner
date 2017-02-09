
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.logging.Level;

/**
 * Created by glebu on 01-Feb-17.
 */
class DatabaseWriter {
    private SessionFactory sessionFactory;
    DatabaseWriter(){
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        this.sessionFactory = new Configuration().configure( ).buildSessionFactory();
    }

    void writeTweet(StatusPOJO status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(new UserPOJO(status.getUser()));
        session.saveOrUpdate(status);
        transaction.commit();
        session.close();
    }
}
