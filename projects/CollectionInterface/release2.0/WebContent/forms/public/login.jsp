<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager</title>	
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script language="javascript">
	function validationForm() {
		if($("#j_username").val().length == 0) {
			$("#requireUsername").show();
			return false;
		} else {
			$("#requireUsername").hide();
		}
		
		if ($("#j_password").val().length == 0) {
			$("#requirePassword").show();
			return false;
		} else {
			$("#requirePassword").hide();
		}
	}
	$(document).ready(function() {
		$('.errMsg').hide();
	});
	</script>
</head>
<body>
	<div id="page" style="width:1050px;text-align:center;min-height:700px">	
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager</h1>
		</div>
		<div id="maintree" style="text-align:left; min-height:300px" class="clearfix">
			<h1 style="text-align:center; padding-top:20px">Login</h1>
			<form id="login_form" method="POST" action='j_security_check'>
		  	<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto;">
		    	<tr>		    	
		      	<td align="right">Email:</td>
		      	<td align="left" colspan="2"><input type="email" required id="j_username" name="j_username" size="22"><br>
		      		<label id="requireUsername" class="errMsg">Please enter a username</label></td>		      	
			    </tr>			    
			    <tr>			    
			      <td align="right" style="vertical-align:top;">Password:</td>
			      <td align="right" colspan="2"><input type="password" required id="j_password" name="j_password" size="22"><br/>
			      <label id="requirePassword" class="errMsg">Please enter a password</label></br>
			      <a href="../public/resetPassword.jsp">Forgot my password</a></td>
			      
			    </tr>
			    <tr>
			    	<td align="right" colspan=2><input type="submit" value="Login"></td>
			    </tr>
			  </table>
			</form>
			<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto;">
				<tr>
					<td align="right"><p>Don't have an account? Create one, it's simple. 
							    	<input onclick="document.location.href='../public/registration.jsp'" type="button" value="Register"></p></td>			    	
			    </tr>
			</table>
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="/common/footer.jsp"/>
		</div>
	</div>
</body>
</html>