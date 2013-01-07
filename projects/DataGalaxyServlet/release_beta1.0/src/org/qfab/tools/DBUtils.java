package org.qfab.tools;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.qfab.domain.DataCollection;

public class DBUtils {
	
	public static final SessionFactory sessionFactory;
	public static final ServiceRegistry serviceRegistry;
	
	
	static{
		try{
			Configuration config = new Configuration();
			config.configure("hibernate.cfg.xml");
			
		    serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();        
		    sessionFactory = config.buildSessionFactory(serviceRegistry);
		}catch (Throwable ex) {
		      // Make sure you log the exception, as it might be swallowed
		      System.err.println("Initial SessionFactory creation failed." + ex);
		      throw new ExceptionInInitializerError(ex);
		    }
	}
	
	  public static final ThreadLocal session = new ThreadLocal();

	  public static Session currentSession() throws HibernateException {
	    Session s = (Session) session.get();
	    // Open a new Session, if this thread has none yet
	    if (s == null) {
	      s = sessionFactory.openSession();
	      // Store it in the ThreadLocal variable
	      session.set(s);
	    }
	    return s;
	  }
	  
	  public static void closeSession() throws HibernateException {
		    Session s = (Session) session.get();
		    if (s != null)
		      s.close();
		    session.set(null);
		  }
	  
	  public static void saveToDB(Object obj){
		    Session session = DBUtils.currentSession();
		    session.beginTransaction();
		    session.save(obj);
		    session.getTransaction().commit();
		    closeSession();
	  }
	  
	  public static List find(String sql){
		    Session session = DBUtils.currentSession();
		    List list = session.createQuery(sql).list();
		    closeSession();
		    return list;
	  }
		}


