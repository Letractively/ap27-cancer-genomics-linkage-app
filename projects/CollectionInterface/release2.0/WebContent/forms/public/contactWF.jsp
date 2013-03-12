<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.qfab.domains.*"%>
<%@ page import="org.qfab.managers.*"%>
<%@ page import="org.qfab.utils.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RIF-CS Manager - My Account</title>
<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/EmailManager.js'></script>
<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
<script type='text/javascript' src='../../scripts/contactWF.js'></script>

</head>
<body onload="init();">
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager</h1>
			<%
				if (request.getUserPrincipal() != null) {
					String email = request.getUserPrincipal().getName();					
					request.setAttribute( "userEmail", email);
				}
			 %>
			<c:if test="${userEmail != null}">
				<jsp:include page='../../common/navigation.jsp'/>
			</c:if>
			<div id="user">
			<%
 				if (request.getUserPrincipal() != null) {
				String username = request.getUserPrincipal().getName();
			%>
		<b>Logged in as: <span id="user" style="font-weight:bold; color:#0000FF; padding-right: 20px;"><%=username%></b></span> 
		<a href="${pageContext.request.contextPath}/forms/public/logout.jsp">Logout</a>
<%	
 } else {
%>
	<!-- <span id="user" style="font-weight:bold; color:#0000FF"></span><a href="../upload/file.jsp">Login</a> -->
<span id="user"><a href="../secure/account.jsp">Login</a></span>
<%	
 }
%>
</div>
		</div>
		<div id="maintree" style="text-align:left; min-height:300px; padding-left:60px; padding-right:60px;" class="clearfix">
       <%
       boolean error = false;
       Workflow wf = new Workflow();
       if(request.getParameter("id") == null){     	   
    	   error = true;    	  
       }else{
    	   int workflowID = Integer.parseInt(request.getParameter("id"));
           wf = WorkflowManager.getWorkflowById(workflowID); 
           if (wf == null){
        	   error = true;
           }else{
        	   if(wf.getIsHosted()){
        		   error = true;
        	   }
           }         
       }       
       if(error){
       %>
    	   <div id="summary" style="width:900px;text-align:center;align:center;margin:0 auto;">
			<h1>No Workflow found. Please double check the web address.</h1>
			<p>If you are certain you have the correct web address, but are encountering an error, please contact the site administration!</p>
			<p><a href="../../index.jsp">Home</a></p>	
		</div>
      <% 
       }else{   	   
    	   
        %>
        <div id="summary" style="width:900px;text-align:center;align:center;margin:0 auto;">
        <h1> Workflow: <% out.println(wf.getName()); %></h1>
        <p><span style ="display: inline-block; width: 800px;" id="msg"></span></p>
        <p>Please use the contact form below to contact the owner of the workflow for details.</p>
        </div>       
         <table width="600px" cellpadding="2" border="0" cellspacing="0"
            style="margin-left: auto; margin-right: auto; width: 550px;">
            <tr>
               <td class="labelCol">Subject:</td>
               <td class="fieldCol">
     			<%
     			 out.println(String.format("Regarding Workflow <b>'%s'</b>.", wf.getName()));
     			%>
               </td>
            </tr>
            <tr>
               <td class="labelCol"><label id="first_name_label" for="first_name">First Name *</label></td>
               <td class="fieldCol"><input type="text" id="first_name" name="first_name" maxlength="50" size="30"></td>
            </tr>
            <tr>
               <td class="labelCol"><label id="last_name_label" for="last_name">Last Name *</label></td>
               <td class="fieldCol"><input type="text" id="last_name" name="last_name" maxlength="50" size="30"></td>
            </tr>
            <tr>
               <td class="labelCol"><label id="email_label" for="email">Email Address *</label></td>
               <td class="fieldCol"><input type="text" id="email" name="email" maxlength="80" size="30"></td>
            </tr>
            <tr>
               <td class="labelCol"><label for="telephone">Telephone Number</label></td>
               <td class="fieldCol"><input type="text" id="telephone" name="telephone" maxlength="30" size="30"></td>
            </tr>
            <tr>
               <td class="labelCol"><label id="comments_label" for="comments">Comments *</label></td>
               <td class="fieldCol"><textarea name="comments" id="comments" maxlength="1000" cols="40" rows="15"></textarea></td>
            </tr>
            <tr>
               <td colspan="2" align="right"><input type="button" value="Send" onclick="sendToOwner(<%=request.getParameter("id")%>)"></td>
            </tr>
         </table>
         <%
       }
         %>
      </div>
      <div id="footer" style="width: 950px" class="clearfix">
         <jsp:include page="../../common/footer.jsp" />
      </div>
   </div>
</body>
</html>