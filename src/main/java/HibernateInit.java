import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by glebu on 16-Feb-17.
 */
public class HibernateInit {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    HibernateInit() {
        this.session = sessionFactory.openSession();
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
