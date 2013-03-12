<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
</head>
<body>
	<div id="maintree">
			<h1>Add New Site</h1>
			<p><span id="emailMsg"></span></p>
			<p><span id="siteMsg"></span></p> 
			<form name="siteForm" id="siteForm" method="POST" action='' >			
			  	<table style="width:450px; margin:0 auto;" >			  		
			  		<tr>
			    		<td class="popUplabelCol">Site Name:</td>
			    		<td class="fieldCol"><input type="text" name="sname" size="35"><br/>
			    			<label class="errMsg" id="requiredname">Please enter a Site Name.</label></td>
			    	</tr>
			    	<tr>
			    		<td class="popUplabelCol">Description:</td>
			    		<td class="fieldCol"><textarea name="sdescription" rows="4" cols="34" style="width:250px"></textarea><br/>
			    			<label class="errMsg" id="requireddescription">Please enter a Description.</label></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="popUplabelCol">URL:</td>
			    		<td class="fieldCol"><input type="text" name="surl" size="35"><br/>
			    			<label class="errMsg" id="requiredurl">Please enter a URL.</label></td>
			    	</tr>
			    	<tr>
			    		<td class="popUplabelCol">Rights:</td>
			    		<td class="fieldCol"><input type="text" name="srights" size="35"><br/>
			    			<label class="errMsg" id="requiredrights">Please enter Rights.</label></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="popUplabelCol">Institute/Affiliation:</td>
			    		<td class="fieldCol" id="affiliation"><select id="selectaffiliation" name="selectaffiliation"></select></td>		      			
			    	</tr>
			    	<tr>
			    		<td class="popUplabelCol">Email:</td>
			    		<td class="fieldCol"><input type="text" name="email" size="35" readonly ></input></td>		      			
			    	</tr>
			    	<tr height="50px">
			    		<td> &nbsp; </td>
			    		<td class="fieldCol"><input type="button" value="Send Site for Verification"  onClick="validateSiteforDataSource(this.form);" /><input type="button" value="Cancel" style="margin-left: 10px;" onClick="document.getElementById('blockUI').style.display = 'none'" /></td>
			    	</tr>
			  	</table>
			</form>
		</div>				
</body>
</html>