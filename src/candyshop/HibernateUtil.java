
package candyshop;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {
    
    private static Session session = null;
    
    private HibernateUtil(){
    
    }
    
    public static Session getsession(){
        
        if(session==null){
            Configuration config = new Configuration();
            config.configure("/config/hibernate.cfg.xml");
            ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
            SessionFactory sf = config.buildSessionFactory(sr);
            session = sf.openSession();
        }
        
        return session;
    }
    
}
