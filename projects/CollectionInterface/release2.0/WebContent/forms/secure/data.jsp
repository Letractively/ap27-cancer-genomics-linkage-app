<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"  %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.qfab.domains.*" %>
<%@ page import="org.qfab.managers.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager - Data RIF-CS</title>
	<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
	<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocationManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataSourceManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataSource.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script>
	<script type='text/javascript' src='../../scripts/data.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script language="javascript">
	function init() {	
		var dataSourceId = <%=request.getParameter("id")%>;
		var actionType = "<%=request.getParameter("type")%>";
		var currentUser = "<%=request.getUserPrincipal().getName()%>";
		
		$("#msg").hide();
		$("#emailMsg").hide();
					
 		if (actionType == "edit") {
 			allowaccess(currentUser, dataSourceId);
			turnOnEditData();
			populateDataSource(currentUser, dataSourceId);
		} else if (actionType == "view" ) {
			populateDataSource(currentUser, dataSourceId);
			//ownerOrHostforDatalocation(currentUser, dataLocationId);
			turnOnReadOnly(true);
		} else if (actionType == "add" && dataSourceId > 0) {	
			getRemainingDataSources(currentUser,dataSourceId);
			//ownerOrHost(currentUser, dataSourceId);
			populateDataSource(currentUser, dataSourceId);
			turnOnReadOnly(false);
		}else if (actionType == "add" && (dataSourceId == null || dataSourceId == 0)) {
			getRemainingDataSources(currentUser,"0");
			populatefieldset(currentUser, "0");
			setInstitutions(currentUser);
			turnOnNewData(currentUser);
		}
		else {
			turnOnNewData(currentUser);
		}	 
	}
	
	function getAdminforDatasource(){
		var e = document.getElementById("urlSelect");
		var dsValue = e.options[e.selectedIndex].value;
		loadDataSource();
		document.getElementById("DsID").value=dsValue;
		var tableOwner = document.getElementById("siteOwnersTable");
		tableOwner.refresh();
		
	}
	</script>
</head>
<body onload="init()">
	<div id="page">
		<div id="header">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Data RIF-CS</h1>
			<jsp:include page="../../common/navigation.jsp"/>
			<jsp:include page="../../common/user.jsp"/>
		</div>
		<div id="maintree">
		<h1>Data RIF-CS Details</h1>
		<input type="hidden" id="userEmail" name="userEmail" value="<%=request.getUserPrincipal().getName()%>">
		<p><span style="display: inline-block; width: 800px;"id="msg"></span></p>
		<form method="POST" action="">
		  	<table id="rootTable" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:550px;">
		    	<tr>
		    		<td class="labelCol"><label id="urlTopLBL"> Choose an existing URL:</label></td>
		    		<td class="fieldCol">
		    		<div class="selDIV">
		    			<select id="urlSelect" name="urlSelect" class="urlSelect"></select>
		    			<input type="button" id="loadDataSourceBtn" value="Load" onclick="getDataSourceId()"/>
		    		</div>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td class="labelCol"><label id="urlChoiceLBL">Or Enter your own </label><label id="urlBottomLBL">URL:</label></td>
		    		<td class="fieldCol">
		    			<input required type="text" id="url" name="url">
		    		</td>		      			
		    	</tr>
		    	<tr>			    		
		    		<td class="labelCol"><label id="fullnameLBL">Full name of data source:</label></td>
      			<td class="fieldCol"><input required="" type="text" id="fullname" name="fullname" size="35"></td>
		    	</tr>
		    	<tr>
		    		<td class="labelCol"><label id="abbrevLBL">Abbreviation of data source:</label></td>
		    		<td class="fieldCol"><input required="" type="text" id="abbrev" name="abbrev" size="35"></td>		      			
		    	</tr>
		    	<tr>		    		
		    		<td class="labelCol"><label id="rightsLBL">Access Rights:</label></td>
		    		<td class="fieldCol"><input type="text" id="rights" name="rights" size="35"></td>
	      			
		    	</tr>
		    	<tr>
		    		<td class="labelCol"><label id="descriptionLBL">Description:</label></td>
		    		<td class="fieldCol"><textarea id="description" name="description" rows="5" cols="60" ></textarea></td>			    			      			
		    	</tr>
		    	<tr class="addSubjectCodeRow">
     				<td align=left colspan="2">
     				<p style="margin-top:10px;"><strong>ANZSRC (FOR) or (SEO) associated with the dataset </strong>
     				<a href="http://www.arc.gov.au/applicants/codes.htm#FOR" target="_blank">Look Up ANZSRC</a></p>
     				<p>Enter the ANZSRC code(s) separated by `space` or `comma`
					and select the Code Category from the list and click "Add":</p>

					<input type="text" name="anzsrc" id="anzsrc" size="35" value="" />
					<select id="codeCat" name="selection" onchange="set(this);">		      			
	      			<option value="anzsrc_for">ANZSRC(FOR)</option>
		      		<option value="anzsrc_seo">ANZSRC(SEO)</option>
		      	</select>		      			
		      	<input id="addInputs" type="button" value="Add" onclick="insertANZ();" />
					</td>     				
     			</tr>
     			<tbody id="subjectCodes">
     			<tr>
					<td><strong>ANZSRC FOR</strong></td>
					<td><strong>ANZSRC SEO</strong></td>		    		
				</tr>
				<tr>
					<td id="columnFor">		    			      							      			
	      			<select name="selectionFOR" id="selectionFOR" size="7" multiple="multiple">
	      			</select>		      			
	      			<br />		      			
	      			<input id="deleteFor" type="button" value="Delete" onclick="removeSelection(this.form.selectionFOR);" />
		    		</td>
		    		<td id="columnSeo">		    		     							      			
	      			<select name="selectionSEO" id="selectionSEO" size="7" multiple="multiple">
      				</select>	      			
	      			<br />
	      			<input id="deleteSeo" type="button" value="Delete" onclick="removeSelection(this.form.selectionSEO);" />		    		
		    		</td>
	    		</tr>
				</tbody>
			</table>
			<div id="ownerInfo" align="left" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:610px;">
			<div id="addowner" style="display:none">
			<label>Who else can edit this data record?</label><br/>
			<label>Email: </label><input type="email" name="coOwnerEmail" id="coOwnerEmail" 
			title="Separate multiple emails with: a comma(,), a semicolon(;) or a space( )." />
			<input id="addEmailBtn" type="button" title="Add Co-owner" value="Add Co-owner" onclick="addDataOwner(<%=request.getParameter("id")%>);" />
			<br/>
			<label id="emailMsg" class="errMsg"></label>
			</div>
			<br/><br/><br/>
			<label>Select the site(s) that host(s) this datasource:</label><br/>
		<table>
		<tr><td>
<fieldset id="site_list" style="overflow:auto;width:250px;height:250px">
    <legend>Sites</legend>
</fieldset>
</td>
<td>
<label>Site not listed?</label><br/>
<input id="addSitebutton" type="button" value="Add a new Site" onclick="addSite()" />    
</td>
</tr>
</table>
<br><br><br>
<input id="savebut" type="button" value="Save Changes" onclick="saveChanges('<%=request.getUserPrincipal().getName()%>',<%=request.getParameter("id")%>);" />

			</div>
			

      </form>
	   </div>
	   <div class="blockUI" id="blockUI" style="display: none;">
		<div class="blockUI blockOverlay" style="z-index: 1000; border: medium none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(0, 0, 0);-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=80)'; filter: alpha(opacity=80); opacity: 0.8; position: fixed;" >
		</div>
		<div class="blockUI blockMsg blockPage" style="z-index: 1011; position: fixed;  top: 50%;  left: 50%;  margin-top: -250px;  margin-left: -300px; ">		
            <div style="width: 600px; height: 500px; border: 1px solid #000000; background: #FFFFFF" >
            <img src="../../images/winclose.gif" onclick="document.getElementById('blockUI').style.display = 'none'; populatefieldset('<%=request.getUserPrincipal().getName()%>',<%=request.getParameter("id")%>);" style="display:block; float:right;"/>
            <jsp:include page="../../common/siteForm.jsp"/>			
			</div>		
		</div>
		</div>	  
		<div id="footer" style="width:950px" class="clearfix">
		<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>