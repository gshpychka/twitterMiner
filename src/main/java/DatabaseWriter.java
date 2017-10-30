
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.logging.Level;

/**
 *  Writes the tweet to the database using Hibernate
 */
class DatabaseWriter {
    static private SessionFactory sessionFactory = HibernateSessionFactory.factory;
    DatabaseWriter(){

    }

    static void writeTweet(StatusPOJO status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(new UserPOJO(status.getUser()));
        session.saveOrUpdate(status);
        transaction.commit();
        session.close();
    }

    static void persist(Object object) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(object);
        transaction.commit();
        session.close();
    }
}
