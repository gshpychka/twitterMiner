
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.logging.Level;

/**
 *  Writes the tweet to the database using Hibernate
 */
class DatabaseWriter {
    private SessionFactory sessionFactory;
    DatabaseWriter(){
        //java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    void writeTweet(StatusPOJO status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(new UserPOJO(status.getUser()));
        session.saveOrUpdate(status);
        transaction.commit();
        session.close();
    }

    void persist(Object object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(object);
        transaction.commit();
        session.close();
    }
}
