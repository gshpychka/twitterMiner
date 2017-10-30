import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

class HibernateSessionFactory {
    static final SessionFactory factory = new Configuration().configure().buildSessionFactory();
}
