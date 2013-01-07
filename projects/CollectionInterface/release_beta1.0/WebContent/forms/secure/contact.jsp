<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.qfab.domains.*"%>
<%@ page import="org.qfab.managers.*"%>
<%@ page import="org.qfab.utils.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager - My Account</title>
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/EmailManager.js'></script>
	
<!-- 	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Person.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/PersonManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocationManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/RifcsIO.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocation.js'></script> -->
	
	
	<script type='text/javascript' >
	
	function sendToOwner(){
		var datasourceId = getParam('datasource');
		var siteId = getParam('site');
		var firstname = dwr.util.getValue("first_name");
		var lastname = dwr.util.getValue("last_name");
		var email = dwr.util.getValue("email");		
		var telephone = dwr.util.getValue("telephone");		
		var comments = dwr.util.getValue("comments");
		EmailManager.sendRequestToOwner(datasourceId,siteId,firstname,lastname,email,telephone,comments, {callback:function(check){	
			alert(check);
			alert("Your form has been submitted");
			window.history.back();
		}
		});
		
	}
	
	function getParam(name)
	{
	 name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	 var regexS = "[\\?&]"+name+"=([^&#]*)";
	 var regex = new RegExp( regexS );
	 var results = regex.exec( window.location.href );
	 if( results == null )
	  return "";
	else
	 return results[1];
	}
	
	</script>
</head>
<body>
<div id="page" 
	style="width: 1050px; text-align: center; min-height: 700px">
	<div id="header" style="height: 100px;" class="clearfix">
		<jsp:include page="../../common/header.jsp" />
		<h1>RIF-CS Manager - My Account</h1>
		<jsp:include page="../../common/navigation.jsp" />
		<jsp:include page="../../common/user.jsp" />
	</div>
	<div id="maintree" style="text-align:left; min-height:300px; padding-left:60px; padding-right:60px;" class="clearfix">
		<h1>Email Owner</h1>
		<table width="600px" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:550px;">
		<tr>
			<td class="labelCol">Subject:</td>
			<td class="fieldCol">
		<%
			int dataSourceID = Integer.parseInt(request.getParameter("datasource"));
			DataSource ds = DataSourceManager.getDataSourceById(dataSourceID);
			out.println(String.format("Regarding Data Source <b>'%s'</b>.",
				ds.getName())); 
		%>	
		</td>
		</tr>
		<tr>
		 <td class="labelCol">
		  <label for="first_name">First Name *</label>
		 </td>
		 <td class="fieldCol">
		  <input  type="text" id="first_name" name="first_name" maxlength="50" size="30">
		 </td>
		</tr>
		 
		<tr>
		 <td class="labelCol">
		  <label for="last_name">Last Name *</label>
		 </td>
		 <td class="fieldCol">
		  <input  type="text" id="last_name" name="last_name" maxlength="50" size="30">
		 </td>
		</tr>
		<tr>
		 <td class="labelCol">
		  <label for="email">Email Address *</label>
		 </td>
		 <td class="fieldCol">
		  <input  type="text" id="email" name="email" maxlength="80" size="30">
		 </td>
		 
		</tr>
		<tr>
		 <td class="labelCol">
		  <label for="telephone">Telephone Number</label>
		 </td>
		 <td class="fieldCol">
		  <input  type="text" id="telephone" name="telephone" maxlength="30" size="30">
		 </td>
		</tr>
		<tr>
		 <td class="labelCol">
		  <label for="comments">Comments *</label>
		 </td>
		 <td class="fieldCol">
		  <textarea  name="comments" id="comments" maxlength="1000" cols="40" rows="15"></textarea>
		 </td>
		 
		</tr>
		<tr>
		 <td colspan="2" align="right">
		  <input type="button" value="Send" onclick="sendToOwner()">
		 </td>
		</tr>
		</table>
	</div>
	<div id="footer" style="width:950px" class="clearfix">
		<jsp:include page="../../common/footer.jsp"/>
	</div>
</div>
</body>
</html>