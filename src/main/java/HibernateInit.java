import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Created by glebu on 16-Feb-17.
 */
class HibernateInit {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    HibernateInit() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.session = sessionFactory.openSession();
        this.transaction = session.beginTransaction();
    }

    Session getSession() {
        return session;
    }

    Transaction getTransaction() {
        return transaction;
    }
}
