<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.qfab.managers.PersonManager"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager - Password Reset</title>
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/PersonManager.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script language="javascript">
		function resetPassword(form) {
			PersonManager.getPersonByEmail(form.email.value, { async:false,
				callback:function(person) {
					if (person != null) {
						PersonManager.resetPassword(form.email.value);
						$("#emailConf").text(form.email.value);
						$('.errMsg').hide();
						$("#emailConfirmMsg").show();
					} else {
						$("#emailErr").text(form.email.value);
						$('.confirmMsg').hide();
						$("#emailErrMsg").show();
					}
				}
			});
			return false;
		}
		
		$(document).ready(function() {
			$('.errMsg').hide();
			$('.confirmMsg').hide();
		});
	</script>
</head>
<body>
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Password Reset</h1>
		</div>
		<div id="maintree" style="text-align:left; min-height:300px" class="clearfix">
			<h1>Reset Password</h1>
			<form method="POST" action='../secure/account.jsp' onsubmit='return resetPassword(this)'>
		  	<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto">
		  		<tr>
		  			<td colspan="2">
		  			<label id="emailErrMsg" class="errMsg">Email (<span id="emailErr"></span>) is not registered with this site.</label>
		  			<label id="emailConfirmMsg" class="confirmMsg">A new password has been sent to your email account (<span id="emailConf"></span>). </label>
		  			</td>
		  		</tr>
		    	<tr>
			      <td align="right">Email:</td>
			      <td align="left"><input type="email" required placeholder="Enter a valid email address" name="email" size="50"></td>
			    </tr>
			    <tr>
			      <td align="right" colspan="2"><input type="submit" value="Reset password" /></td>
			    </tr>
			  </table>
			</form>	
			<!-- <h1 style="text-align:center; padding-top:25px;">A new password has been sent to your email account.</h1> -->
			<div align="center">
			<a href="${pageContext.request.contextPath}/forms/public/login.jsp">Back to login</a>
			</div>
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>