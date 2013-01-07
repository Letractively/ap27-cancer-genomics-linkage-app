package org.qfab.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;



import org.qfab.domain.DataLocation;
import org.qfab.tools.PropertyLoader;
import org.qfab.tools.EmailTemplateTool;

/**
 * This Class sends an Email to the workflow's owner to prevent him its workflow is available for publishing
 * @author p.chaumeil
 *
 */

public class EmailManager {
	
	  /**
	  * Send a single email.
	  */
	  public static void sendEmail(DataLocation datalocation){
		Properties props = PropertyLoader.loadProperties("mail.properties");	
		System.setProperty("fromEmail", props.getProperty("mail.from"));
		System.setProperty("mailHost",props.getProperty("mail.host"));
		System.setProperty("subjectEmail", props.getProperty("subject.email"));
		System.setProperty("bodyurlEmail", props.getProperty("body.url.email"));
		System.setProperty("cmurl", props.getProperty("cm.url"));
		
	    String aFromEmailAddr =new String(System.getProperty("fromEmail")); 
	    String aToEmailAddr = datalocation.getDataAdmin().getEmail();
	    String password = datalocation.getDataAdmin().getUnencryptedPwd();
	    String aSubject=new String(System.getProperty("subjectEmail"));
	    String aCMurl=new String(System.getProperty("cmurl"));
	    String mailHost=new String(System.getProperty("mailHost"));
	    
	    //if user is already existing (password==null), then just email without login details	    
	    String aBody ="";
	    if(password!=null){
	    	String template =props.getProperty("datasource.newuser.body");
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("DATASOURCE", new String(datalocation.getDataCollection().getName()));
			mp.put("CMURL", new String(aCMurl));
			mp.put("USER", new String(aToEmailAddr));
			mp.put("PASSWORD", new String(password));
			aBody=EmailTemplateTool.replace(template, mp);
		}else{
			String template =props.getProperty("datasource.body");
			Map<String, Object> mp = new HashMap<String, Object>();
			mp.put("DATASOURCE", new String(datalocation.getDataCollection().getName()));
			mp.put("CMURL", new String(aCMurl));			
			aBody=EmailTemplateTool.replace(template, mp);
	    }
	    //Here, no Authenticator argument is used (it is null).
	    //Authenticators are used to prompt the user for user
	    //name and password.
	    Session session = Session.getDefaultInstance( fMailServerConfig, null );
	    MimeMessage message = new MimeMessage( session );
	    try {
	      //the "from" address may be set in code, or set in the
	      //config file under "mail.from" ; here, the latter style is used
	      //message.setFrom( new InternetAddress(aFromEmailAddr) );
	      message.addRecipient(
	        Message.RecipientType.TO, new InternetAddress(aToEmailAddr)
	      );
	      message.setFrom(new InternetAddress(aFromEmailAddr,mailHost));
	      message.setSubject( aSubject );
	      message.setText( aBody );
	     
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
