package org.qfab.utils;

import java.util.List;

import javax.activation.DataSource;


import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Subqueries;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DBUtils {
	
	public static final SessionFactory sessionFactory;
	public static final ServiceRegistry serviceRegistry;

	static {
		try {
			Configuration config = new Configuration();
			config.configure("hibernate.test.cfg.xml");
			// config.configure();
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
	    	sessionFactory.getCache().evictEntityRegions();
	    	sessionFactory.getCache().evictCollectionRegions();
	    	sessionFactory.getCache().evictDefaultQueryRegion();
	    	s = sessionFactory.openSession();
	      s.flush();
	      s.clear();
	      // Store it in the ThreadLocal variable
	      session.set(s);
	    }else{
	    	s.flush();
	    }
	    return s;
	  }
	  
	  
	  public static void closeSession() throws HibernateException {
		    Session s = DBUtils.currentSession();
		    if (s != null)
			  s.flush();
		      s.clear();
		      s.close();
		    session.set(null);
		  }
	  
	  public static void saveToDB(Object obj){
		    Session session = DBUtils.currentSession();
		    session.beginTransaction();
		    session.saveOrUpdate(obj);
		    session.getTransaction().commit();
		    closeSession();
	  }
	  
		public static void delete(Object obj) {
			Session session = DBUtils.currentSession();
			session.beginTransaction();
			session.delete(obj);
			session.getTransaction().commit();
			closeSession();
		}

		public static void delete(String hql) {
			Session session = DBUtils.currentSession();
			session.beginTransaction();
			Query qry = session.createQuery(hql);
			int rows = qry.executeUpdate();
			System.out.printf("Deleted %d rows?\n", rows);
			session.getTransaction().commit();
			closeSession();
		}
		
	  public static List find(String sql){
		    Session session = DBUtils.currentSession();
		    Query query = session.createQuery(sql);
		    List list = query.list();
		    closeSession();
		    return list;
	  }
	  
	  public static List getRemainingDataSource() {
		  Session session = currentSession();
		  org.hibernate.Criteria criteria = session.createCriteria(DataSource.class)
				  .add(Subqueries.propertyNotIn("data_id", 
						  DetachedCriteria.forClass(org.qfab.domains.DataLocation.class)));
		  List list = criteria.list();
		  closeSession();
		  return list;
	  }
	  
		public static List findBySQLquery(String sql, Class entity) {
			Session session = DBUtils.currentSession();
			Query query = session.createSQLQuery(sql).addEntity(entity);
			List list = query.list();
			closeSession();
			return list;
		}
	  	  
		}


