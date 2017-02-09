
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
    DatabaseWriter(){
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        this.sessionFactory = new Configuration().configure( ).buildSessionFactory();
    }

    void writeTweet(StatusPOJO status) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        StatusPOJO statusOLD = session.get(StatusPOJO.class, status.getTweetID());
        UserPOJO userOLD = session.get(UserPOJO.class, status.getUserID());
        transaction.commit();
        session.close();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        if (userOLD == null) { //new user
            session.save(new UserPOJO(status.getUser())); //persist the user
        } else { //user already in the database
            session.update(new UserPOJO(status.getUser())); //update the info
        }
        if(statusOLD==null){ //not in the database
            session.save(status);
        } else { //retweeted
            session.update(status);
        }
        transaction.commit();
        session.close();
    }
}
