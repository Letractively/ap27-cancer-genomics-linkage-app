<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
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
			<h1>RIF-CS Manager - Welcome</h1>
		</div>
		<div id="maintree">
			<h1 style="padding-top:25px">Welcome to RIF-CS Manager.</h1>
			<p>Your password has been sent to: <c:out value="${param.email}"/>  </p>
			<p>
			<span id="user" style="font-weight:bold;color:#0000FF"><a href="../secure/account.jsp">Login</a></span>
			</p>
			
			<p>The table below displays which GVL-EA instance hosts the corresponding published data sources:</p>
		
		<!-- 
		<table style="horizontal-align:center" border="1" cellspacing="0" cellpadding="10">
			<tr>
				<th></th>
				<th colspan="3">GVL Site</th>
			</tr>
			<tr>
				<th>Data library</th>
				<th>GVL @ UQ</th>
				<th>GVL @ Garvan</th>
				<th>GVL @ VLSCI</th>
			</tr>
			<tr>
				<td>ICGC Open Access</td>
				<td>a</td>
				<td>b</td>
				<td>c</td>
			</tr>
			<tr>
				<td>1000 Genomes (Trios from Pilot 2)</td>
				<td>d</td>
				<td>e</td>
				<td>f</td>
			</tr>
			<tr>
				<td>Drugbank</td>
				<td>g</td>
				<td>h</td>
				<td>i</td>
			</tr>
			<tr>
				<td>...</td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>...</td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
		</table>		 -->
			
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>