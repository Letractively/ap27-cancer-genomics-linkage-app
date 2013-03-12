<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
</head>
<body>
	<div  id="navi">
	<ul>
		<li><a href="${pageContext.request.contextPath}/forms/secure/account.jsp">My Account</a></li>
		<li><a href="${pageContext.request.contextPath}/forms/secure/data.jsp?type=add" >New Data Record</a></li>
		<li><a href="${pageContext.request.contextPath}/forms/secure/workflow.jsp">New Workflow Record</a></li> 
		<li><a href="${pageContext.request.contextPath}/forms/secure/site.jsp">New Site</a></li>
	</ul>
	</div>
</body>
</html>