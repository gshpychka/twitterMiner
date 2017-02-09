
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import javax.persistence.PersistenceException;
import java.util.logging.Level;

/**
 * Created by glebu on 01-Feb-17.
 */
class DatabaseWriter {
    private SessionFactory sessionFactory;
    private Transaction transaction;
    DatabaseWriter(){
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        this.sessionFactory = new Configuration().configure( ).buildSessionFactory();
    }

    void writeTweetHibernate(StatusPOJO status){
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.save(status);
        session.save(new UserPOJO(status.getUser()));
        try {
            transaction.commit();
        } catch (PersistenceException e) {

        }
        try {
            if(session.isDirty()) session.flush();
        } catch (ConstraintViolationException e) {
        }
        session.close();
    }

    void writeRetweetHibernate(StatusPOJO status){
        Session session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        transaction.commit();
        StatusPOJO statusOLD=session.get(StatusPOJO.class, status.getTweetID());
        session.close();
        if (statusOLD == null) {
            writeTweetHibernate(status);
        } else {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(status);
            transaction.commit();
            session.close();
        }
    }
}
