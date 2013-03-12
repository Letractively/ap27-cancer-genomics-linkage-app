<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div id="user">
<%
 if (request.getUserPrincipal() != null) {
	String username = request.getUserPrincipal().getName();
%>
<b>Logged in as: <span id="user" style="font-weight:bold; color:#0000FF; padding-right: 20px;
"><%=username%></b></span> 
<a href="${pageContext.request.contextPath}/forms/public/logout.jsp">Logout</a>
<%	
 } else {
%>
<!-- <span id="user" style="font-weight:bold; color:#0000FF"></span><a href="../upload/file.jsp">Login</a> -->
<span id="user"><a href="forms/secure/account.jsp">Login</a></span>
<%	
 }
%>
</div>
</body>
</html>