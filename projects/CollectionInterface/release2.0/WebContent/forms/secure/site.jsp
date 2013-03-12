<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RIFCS Manager - Site</title>
<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
				<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
        		<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>        		
         		<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
         		<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script> 
         		<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
         		<script type='text/javascript' src='/CollectionInterface/dwr/interface/Site.js'></script>           		
        		<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
        		<script type='text/javascript' src='../../scripts/site.js'></script>
        		<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>

        		
			<script type='text/javascript' >
			function init(){
				var username = "<%=request.getUserPrincipal().getName()%>";
				populatePage(username);	
				
				$('.errMsg').hide();
			}
			function validate(form){
				name=form.name.value;
				description=form.description.value;
				url=form.url.value;
				rights=form.rights.value;
				valid=validateForm(name, description, url, rights);
				return valid;
			}
			</script>
</head>
<body onload="init()">
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Site</h1>
			<jsp:include page="../../common/navigation.jsp"/>
			<jsp:include page="../../common/user.jsp"/>
		</div>
		<div id="maintree">
			<h1>New Site</h1>
			<form name="siteForm" method="POST" action='verifySite.jsp' onsubmit="return validate(this);">
			
			  	<table style="width:550px; margin:0 auto;">			  		
			  		<tr>
			    		<td class="labelCol">Site Name:</td>
			    		<td class="fieldCol"><input type="text" name="name" size="35"><br/>
			    			<label class="errMsg" id="requiredname">Please enter a Site Name.</label></td>
			    	</tr>
			    	<tr>
			    		<td class="labelCol">Description:</td>
			    		<td class="fieldCol"><textarea name="description" rows="4" cols="34"></textarea><br/>
			    			<label class="errMsg" id="requireddescription">Please enter a Description.</label></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="labelCol">URL:</td>
			    		<td class="fieldCol"><input type="text" name="url" size="35"><br/>
			    			<label class="errMsg" id="requiredurl">Please enter a URL.</label></td>
			    	</tr>
			    	<tr>
			    		<td class="labelCol">Rights:</td>
			    		<td class="fieldCol"><input type="text" name="rights" size="35"><br/>
			    			<label class="errMsg" id="requiredrights">Please enter Rights.</label></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="labelCol">Institute/Affiliation:</td>
			    		<td class="fieldCol" id="affiliation"><select id="selectaffiliation" name="selectaffiliation"></select></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="labelCol">Email:</td>
			    		<td class="fieldCol"><input type="text" name="email" size="35" readonly ></input></td>		      			
			    	</tr>
			    	<tr>
			    		<td colspan="2" align="right"><input type="submit" value="Send Site for Verification" /></td>
			    	</tr>
			  	</table>
			</form>
		</div>				
		<div id="footer" style="width:950px; margin-top:10px;" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>