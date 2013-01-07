package org.qfab.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.Person;
import org.qfab.domains.Site;
import org.qfab.managers.DataSourceManager;
import org.qfab.managers.SiteManager;
import org.qfab.utils.PropertyLoader;
import org.qfab.utils.EmailTemplateTool;

/**
 * This Class sends an Email to the workflow's owner to prevent him its workflow is available for publishing
 * @author p.chaumeil
 *
 */

public class EmailManager {
	
	 public static final  Properties props = PropertyLoader.loadProperties("mail.properties");
	 
	 
	  /**
	  * Registration
	  * Send a single email.
	  */
	  public static void sendRegistrationEmail(String firstname, String emailTo, String password){
		System.out.println("send registration email"); 
		//Create Map for email body template
		String template =props.getProperty("registration.body");
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("NAME", new String(firstname));
		mp.put("LOGIN", new String(emailTo));
		mp.put("PASSWORD", new String(password));
		template=EmailTemplateTool.replace(template, mp);
		System.out.println(template);
		
		//Set Email Properties
			
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("toEmail", emailTo);
		System.setProperty("subjectEmail", props.getProperty("registration.subject"));
		System.setProperty("bodyEmail", template);
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = new String(System.getProperty("toEmail"));
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aBody =new String(System.getProperty("bodyEmail"));
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
	    
	  }
	  
	  /**
	  * Reset/forgotten Password
	  * Send a single email.
	  */
	  public static void sendForgotPasswordEmail(String emailTo, String password){
		System.out.println("send forgotten password email"); 
		//Create Map for email body template
		String template =props.getProperty("password.body");
		Map<String, Object> mp = new HashMap<String, Object>();		
		mp.put("LOGIN", new String(emailTo));
		mp.put("PASSWORD", new String(password));
		template=EmailTemplateTool.replace(template, mp);
		System.out.println(template);
		
		//Set Email Properties
			
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("toEmail", emailTo);
		System.setProperty("subjectEmail", props.getProperty("password.subject"));
		System.setProperty("bodyEmail", template);
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = new String(System.getProperty("toEmail"));
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aBody =new String(System.getProperty("bodyEmail"));
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
	    
	  }
	  
	  /**
	  * Site Verification Email to QFAB Admin
	  * Send a single email.
	  */
	  public static void sendSiteVerificationEmail(String emailInitiator, String siteName, String siteDescription, String siteURL, String rights, String siteAffiliation){
		System.out.println("send site email"); 
		//Create Map for email body template
		String template =props.getProperty("site.body");
		Map<String, Object> mp = new HashMap<String, Object>();		
		mp.put("LOGIN", new String(emailInitiator));
		mp.put("SNAME", new String(siteName));
		mp.put("DESCRIPTION", new String(siteDescription));
		mp.put("URL", new String(siteURL));
		mp.put("RIGHTS", new String(rights));
		mp.put("AFFILIATION", new String(siteAffiliation));
		template=EmailTemplateTool.replace(template, mp);
		System.out.println(template);
		
		//Set Email Properties
			
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("toEmail", props.getProperty("site.to.email"));
		System.setProperty("subjectEmail", props.getProperty("site.subject"));
		System.setProperty("bodyEmail", template);
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = new String(System.getProperty("toEmail"));
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aBody =new String(System.getProperty("bodyEmail"));
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
	    
	  }	
	  
	  /**
	  * New Data Owner Email
	  * Send a single email.
	  */
	  public static void sendNewDataOwnerEmail(String emailTo, String firstname, String recordTitle){
		System.out.println("send new Data owner email"); 
		//Create Map for email body template
		String template =props.getProperty("ownership.body");
		Map<String, Object> mp = new HashMap<String, Object>();				
		mp.put("NAME", new String(firstname));
		mp.put("RNAME", new String(recordTitle));			
		template=EmailTemplateTool.replace(template, mp);
		System.out.println(template);
		
		//Set Email Properties
			
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("toEmail", emailTo);
		System.setProperty("subjectEmail", props.getProperty("ownership.subject"));
		System.setProperty("bodyEmail", template);
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = new String(System.getProperty("toEmail"));
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aBody =new String(System.getProperty("bodyEmail"));
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
	    
	  }	
	  
	  /**
	  * New Data Owner Email
	  * Send a single email.
	  */
	  public static void sendCoOwnerEmail(String emailTo, String firstname, String recordTitle){
		System.out.println("send co-owner email"); 
		//Create Map for email body template
		String template =props.getProperty("coowner.body");
		Map<String, Object> mp = new HashMap<String, Object>();				
		mp.put("NAME", new String(firstname));
		mp.put("RNAME", new String(recordTitle));			
		template=EmailTemplateTool.replace(template, mp);
		System.out.println(template);
		
		//Set Email Properties
			
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("toEmail", emailTo);
		System.setProperty("subjectEmail", props.getProperty("coowner.subject"));
		System.setProperty("bodyEmail", template);
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = new String(System.getProperty("toEmail"));
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aBody =new String(System.getProperty("bodyEmail"));
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
	    
	  }
	  
	  public static Boolean sendRequestToOwner(Integer datasourceId, Integer siteId, String firstname,String lastname, String email,String telephone,String comment){
			System.out.println("EmailManager.sendRequestToOwner called @ " + new java.util.Date());
			DataSource datasource = new DataSource();
			Site site = new Site();
			//DataLocation datalocation = new DataLocation();
			//Set Email Properties
			datasource = DataSourceManager.getDataSourceById(datasourceId);
			site = SiteManager.getSiteById(siteId);
			List<Person> listAdmin = DataSourceManager.getAdminsForDataSourceinSite(datasource, site);
			System.setProperty("fromName", firstname+" "+lastname);
			System.setProperty("fromEmail", email);
			System.setProperty("subjectEmail", "Request for DataSource "+datasource.getName()+" for the Site "+site.getName());
			System.setProperty("bodyEmail", comment);
			
		    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
		    String aSubject=new String(System.getProperty("subjectEmail"));
		    String aBody =new String(System.getProperty("bodyEmail"));
		    
		    for (Person person : listAdmin) {
		    	System.out.println("We send the Email to" + person.getEmail());
		    	System.setProperty("toEmail",person.getEmail());
		    	String aToEmailAddr = new String(System.getProperty("toEmail"));
		    	postEmail(aToEmailAddr, aFromEmailAddr, aSubject, aBody);
			}
		    return true;
		  }
		
		
	  public static void postEmail(String to, String from, String subject, String body){
	    Session session = Session.getDefaultInstance( fMailServerConfig, null );
	    MimeMessage message = new MimeMessage( session );
	    try {
	      //the "from" address may be set in code, or set in the
	      //config file under "mail.from" ; here, the latter style is used
	      //message.setFrom( aFromEmailAddr );
	      //message.addRecipient(
	        //Message.RecipientType.TO, new InternetAddress(aToEmailAddr)
	      //);
	      message.setRecipients(Message.RecipientType.TO,to);
	      message.setFrom(new InternetAddress(from,System.getProperty("fromName")));
	      message.setSubject( subject );
	      message.setText( body );
	      Transport.send( message );
	    }
	    catch (MessagingException ex){
	      System.err.println("Cannot send email. " + ex);
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	  }
	  /**
	  * Allows the config to be refreshed at runtime, instead of
	  * requiring a restart.
	  */
	  public static void refreshConfig() {
	    fMailServerConfig.clear();
	    fetchConfig();
	  }

	  // PRIVATE //

	  private static Properties fMailServerConfig = new Properties();

	  static {
	    fetchConfig();
	}
	  
	  /**
	   * Open a specific text file containing mail server
	   * parameters, and populate a corresponding Properties object.
	   */
	   private static void fetchConfig() {
	     InputStream input = null;
	     try {
	       //If possible, one should try to avoid hard-coding a path in this
	       //manner; in a web application, one should place such a file in
	       //WEB-INF, and access it using ServletContext.getResourceAsStream.
	       //Another alternative is Class.getResourceAsStream.
	       //This file contains the javax.mail config properties mentioned above.
	       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	       System.out.println("We send an email");
	       input=classLoader.getResourceAsStream("mail.properties");
	       fMailServerConfig.load(input);
	     }
	     catch ( IOException ex ){
	       System.err.println("Cannot open and load mail server properties file.");
	     }
	     finally {
	       try {
	         if ( input != null ) input.close();
	       }
	       catch ( IOException ex ){
	         System.err.println( "Cannot close mail server properties file." );
	       }
	     }
	   }
	 }
