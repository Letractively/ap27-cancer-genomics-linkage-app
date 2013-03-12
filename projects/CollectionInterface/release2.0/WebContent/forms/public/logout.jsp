<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RIF-CS Manager - Welcome</title>
<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
<script language="javascript">
</script>
</head>
<body>
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Logged out</h1>
		</div>
		
		<%@ page session="true"%>
		User '<%=request.getRemoteUser()%>' has been logged out.
		<% session.invalidate(); %>
		<br/><br/>
		
		<div id="maintree" style="text-align:center; min-height:300px" class="clearfix">
			<h1 style="padding-top:25px">Thank you for using RIF-CS Manager.</h1>
			<br/>
			<span id="user" style="font-weight:bold;color:#0000FF"><a href="../secure/account.jsp">Login</a></span>
			<br/>
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>