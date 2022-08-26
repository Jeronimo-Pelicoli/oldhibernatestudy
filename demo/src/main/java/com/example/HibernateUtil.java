package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    static Session sessionObj;
    static SessionFactory sessionFactoryObj;
 
    // This Method Is Used To Create The Hibernate's SessionFactory Object
    private static SessionFactory buildSessionFactory() {
        // Creating Configuration Instance & Passing Hibernate Configuration File
        Configuration configObj = new Configuration();
        configObj.configure("hibernate.cfg.xml");
 
        // Since Hibernate Version 4.x, ServiceRegistry Is Being Used
        ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties()).build(); 
 
        // Creating Hibernate SessionFactory Instance
        sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
        return sessionFactoryObj;
    }
 
    // Method 1: This Method Used To Create A New User Record In The Database Table
    public static void createRecord() {
        Users userObj;
        try {
            sessionObj = buildSessionFactory().openSession();
            sessionObj.beginTransaction();
            
            for(int j=101; j<=105; j++) {
                // Creating User Data & Saving It To The Database
                userObj = new Users();
                userObj.setId(j);
                userObj.setName("Editor " + j);
                userObj.setCreatedBy("Administrator");
                userObj.setCreatedDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                System.out.println(userObj.getName());
                sessionObj.persist(userObj);
            }
            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch(Exception sqlException) {
            System.out.println("msg" + sqlException.getMessage());
            if(null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }
        System.out.println("\n.......Records Saved Successfully In The Database.......\n");
    }
 
    // Method 2: This Method Used To Update A User Record In The Database Table
    public static void updateRecord() {     
        Users userObj;
        int user_id = 103;
        try {
            sessionObj = buildSessionFactory().openSession();
            sessionObj.beginTransaction();
 
            userObj = (Users) sessionObj.get(Users.class, Integer.valueOf(user_id));
 
            // This line Will Result In A 'Database Exception' & The Data Will Rollback (i.e. No Updations Will Be Made In The Database Table)
            userObj.setName("A Very Very Long String Resulting In A Database Error");
 
            // Committing The Transactions To The Database
            sessionObj.getTransaction().commit();
        } catch(Exception sqlException) {
            if(null != sessionObj.getTransaction()) {
                System.out.println("\n.......Transaction Is Being Rolled Back.......");
                sessionObj.getTransaction().rollback();
            }
            sqlException.printStackTrace();
        } finally {
            if(sessionObj != null) {
                sessionObj.close();
            }
        }
    }
}
