<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.qfab.managers.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager - Registration</title>
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/PersonManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Person.js'></script>
	<script type='text/javascript' src='../../scripts/common.js'></script>
	<script language="javascript">		
		function saveRegistration(form) {
			alert("inSaveRegistration7");
			var sendEmail = true;
			var firstname=form.firstname.value;
			var lastname=form.lastname.value;
			var email=form.email.value;
			var title=form.selectuserTitle.options[form.selectuserTitle.selectedIndex].text;
			var instit=form.selectlistInstits.value;
			PersonManager.registerNewUser(firstname,lastname,email,title,instit,sendEmail,{     		
				async:false,
	     		callback:function(pers){
	     		}
			});
		}
	</script>
</head>
<body onLoad="init()">	
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Registration</h1>
		</div>
		<div id="maintree" style="text-align:left; min-height:300px" class="clearfix">
			<h1 style="text-align:center; padding-top:20px">Registration</h1>
			<form method="POST" action='welcome.jsp' onsubmit='saveRegistration(this)'>
		  	<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto">
		    	<tr>
			      <td align="right">Title:</td>
			      <td align="left" id="userTitle"></td>
			    </tr>
			    <tr>
			      <td align="right">First Name:</td>
			      <td align="left"><input type="text" required placeholder="Enter your first name" name="firstname" size="30"></td>
			      <!--<td align="left" style="color:red; visibility:collapse; font-size: 8pt; font-weight:bold; padding-top:10px;" name="firstname_help">Please enter a first name</td>-->
			    </tr>
			    <tr>
			      <td align="right">Last Name:</td>
			      <td align="left"><input type="text" required placeholder="Enter your last name" name="lastname" size="30"></td>
			      <!-- <td align="left" style="color:red; visibility:collapse; font-size: 8pt; font-weight:bold; padding-top:10px;" name="lastname_help">Please enter a last name</td> -->
			    </tr>
			    <tr>
			      <td align="right">Email:</td>
			      <td align="left"><input type="email" required placeholder="Enter a valid email address" name="email" size="50"></td>
			      <!-- <td align="left" style="color:red; visibility:collapse; font-size: 8pt; font-weight:bold; padding-top:10px;" name="email_help">Please enter a valid email address</td> -->
			    </tr>
			    <tr style="height:30px">
			      <td align="right">Institute:</td>
			      <td align="left" id="listInstits"></td>
			    </tr>
			    <tr>
			      <td align="left">&nbsp;</td>
			      <td align="left"><input type="submit" value="Register" /><input type="reset" value="Cancel" onclick="window.history.back(-1);" /></td>
			    </tr>
			  </table>
			</form>		   
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>