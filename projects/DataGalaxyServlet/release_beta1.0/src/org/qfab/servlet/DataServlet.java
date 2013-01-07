package org.qfab.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.qfab.domain.DataCollection;
import org.qfab.domain.DataLocation;
import org.qfab.tools.DBUtils;
import org.qfab.tools.EmailManager;
import org.qfab.tools.PropertyLoader;
import org.qfab.tools.ToolBox;

/**
 * Servlet Class
 */
@WebServlet("/galaxyservlet")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		
		sb.append("<html>");
		sb.append("<style>");
		sb.append("h1 {");
		sb.append("color: blue;");
		sb.append("}");
		sb.append("</style>");
		sb.append("<head>");
		sb.append("</head>");
		sb.append("<body marginwidth=\"100\" bgcolor=white>");
		sb.append("<form method=POST action=galaxy/galaxyservlet enctype=multipart/form-data>");
		sb.append("<h1 align=center > Welcome on the Data Manager servlet for GVL</h1>");
		sb.append("<center >Description here</center>");
		sb.append("<center > Please go to the <a href=\"http://www.qfab.org\">QFAB website</a> for more information </center>");
		sb.append("</form>");
		sb.append("<p>&nbsp;</p>");
		sb.append("</body>");
		sb.append("</html>");
		
		out.println(sb);
		out.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res){
	try {
	PrintWriter out = res.getWriter();
    res.setContentType("text/plain");
    out.println("<h1>Servlet File Upload Example using Commons File Upload</h1>");
    out.println();
	DiskFileItemFactory  fileItemFactory = new DiskFileItemFactory ();
	ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

		/*
		 * Parse the request
		 */
		List items = uploadHandler.parseRequest(req);
		Iterator itr = items.iterator();
		
		DataCollection datacollection = new DataCollection();
		String url=null;
		String description = null;
		String email = null;
		String abbrev = null;
		String name = null;
		Integer counter = 0;
		Boolean isCorrectParameter = null;
		
		Integer taskid = new Integer(0);		
		while(itr.hasNext()) {
			System.out.println(counter);
			FileItem item = (FileItem) itr.next();
			/*
			 * Handle Form Fields.
			 */
			if(item.isFormField()) {
				out.println("File Name = "+item.getFieldName()+", Value = "+item.getString());
				if(item.getFieldName().equals("url")){
				    url = item.getString();
				    counter++;
				}else if(item.getFieldName().equals("description")){
					description = item.getString();
				    counter++;
				}else if(item.getFieldName().equals("email")){
					email = item.getString();
				    counter++;
				}else if(item.getFieldName().equals("name")){
					name = item.getString();
				    counter++;
				}else if(item.getFieldName().equals("abbrev")){
					abbrev = item.getString();
				    counter++;
				}
			} else {
				out.println("Wrong parameters entered");
				
		}
		}
			if(counter!=5){
				out.println("Some parameters are missing (url,description,email,name,abbrev)");
				
			}else {
				out.println("The Upload is successful");
				out.close();
				
				
				DataLocation datalocation = ToolBox.createDatasource(url,name,abbrev,description,email);
				EmailManager.sendEmail(datalocation);
			}
		
		

		
	}catch(FileUploadException ex) {
		log("Error encountered while parsing the request",ex);
	} catch(Exception ex) {
		log("Error encountered while uploading file",ex);
	}

}
	

}
