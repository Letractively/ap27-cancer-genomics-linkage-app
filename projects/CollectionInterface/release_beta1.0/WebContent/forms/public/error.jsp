<%@ page language="java"%>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>RIF-CS Manager - Error</title>
</head>
<body>
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager</h1>
		</div>
   	<div id="maintree" style="text-align:left" class="clearfix">      
 		<hr></hr>
 		<div id="summary" style="width:900px;text-align:center;align:center;margin:0 auto;">
			<h1>Login failed. Please try again.</h1>
			<p><a href="../secure/accounts.jsp">Back to Login</a></p>	
		</div>
	</div>	
	 <div id="footer" style="width:950px" class="clearfix">
            <jsp:include page="/common/footer.jsp"/>
        </div>	
</div>
</body>
</html>
