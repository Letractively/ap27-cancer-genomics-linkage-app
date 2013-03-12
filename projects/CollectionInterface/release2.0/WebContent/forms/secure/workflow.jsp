<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*"  %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.qfab.domains.*" %>
<%@ page import="org.qfab.managers.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
<title>RIF-CS Manager - Data RIF-CS</title>
<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/WorkflowManager.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/Workflow.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script> 
<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/Site.js'></script>           		
<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
<script type='text/javascript' src='/CollectionInterface/dwr/interface/EmailManager.js'></script>	
<script type='text/javascript' src='/CollectionInterface/dwr/interface/RifcsIO.js'></script>
<script type='text/javascript' src='../../scripts/site.js'></script>
<script type='text/javascript' src='../../scripts/workflow.js'></script>
<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
         		
<script language="javascript">
function init() {	
	var workflowId = <%=request.getParameter("id")%>;
	var actionType = "<%=request.getParameter("type")%>";
	var currentUser = "<%=request.getUserPrincipal().getName()%>";
	
	$("#msg").hide();		
	$("#emailMsg").hide();
	$("#siteMsg").hide();
	$('.errMsg').hide();
				
	if (actionType == "edit") {			
		populateWorkflow(currentUser, workflowId);
	} else if (actionType == "publish") {
		populateWorkflowWithErrors(currentUser, workflowId);			
	} else {
		populateSiteForm(currentUser);				
	}		
	
}
</script>
</head>
<body onload="init()">
	<div id="page">
		<div id="header">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Workflow RIF-CS</h1>
			<jsp:include page="../../common/navigation.jsp"/>
			<jsp:include page="../../common/user.jsp"/>
		</div>
		<div id="maintree">
		<h1>Workflow RIF-CS Details</h1>
		<p><span style ="display: inline-block; width: 800px;"id="msg"></span></p>
		<form method="POST" action="">
		  	<table id="rootTable" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:600px;">
		    	<tr>
	    			<td>
	    				<h2 style="margin: 0; padding:0">Workflow information</h2>
	    			</td>
	    			<td>
	    				&nbsp;
	    			</td>
	    		</tr> 
		    	<tr>
		    		<td class="labelCol" style="width:250px"><label id="urllabel">URL to your workflow @Galaxy:</label></td>
		    		<td class="fieldCol" style="width:350px">
		    			<input required="" type="text" id="url" name="url">
		    		</td>		      			
		    	</tr>
		    	<tr>			    		
		    		<td class="labelCol" style="width:250px"><label id="namelabel">Name of the workflow:</label></td>
      			<td class="fieldCol" style="width:350px"><input required="" type="text" id="wfname" name="wfname" size="35"></td>
		    	</tr>
		    	<tr>
		    		<td class="labelCol" style="width:250px">Description:</td>
		    		<td class="fieldCol" style="width:350px"><textarea style="width:250px;"id="description" name="description" rows="5" cols="60" ></textarea></td>			    			      			
		    	</tr>		    	
		    	<tr>		    		
		    		<td class="labelCol" style="width:250px">Access Rights:</td>
		    		<td class="fieldCol" style="width:350px"><input type="text" id="rights" name="rights" size="35"></td>	      			
		    	</tr>
		    	
		    	<tr class="addSubjectCodeRow">
     				<td align=left colspan="2">
     				<p style="margin-top:10px;"><strong>ANZSRC (FOR) or (SEO) associated with the workflow </strong>
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
					<td><label id="anzforlabel"><strong>ANZSRC FOR</strong></label></td>
					<td><label id="anzseolabel"><strong>ANZSRC SEO</strong></label></td>		    		
				</tr>
				<tr>
					<td id="columnFor">		    			      							      			
	      			<select name="selectionFOR" id="selectionFOR" size="7" multiple="multiple" >
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
	    		<tr><td>&nbsp;</td></tr>
	    		</table>
	    		<table id="rootTable" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:600px;">
	    		<tr>
	    			<td>
	    				<h2 style="margin: 0; padding:0">Dataset information</h2>
	    			</td>
	    			<td>
	    				&nbsp;
	    			</td>
	    		</tr> 
	    		<tr>
	    			<td>
	    				<strong>INPUT DATASET</strong>
	    			</td>
	    			<td>
<!-- 	    				<span style="font-size: 1.3em; line-height: 1.3em;"><strong>Output dataset</strong></span> -->
						<strong>OUTPUT DATASET</strong>
	    			</td>
	    		</tr> 
	    		<tr>
		    		<td align="left">
		    			<label id="urlTopLBL">URL to your input dataset:</label><a class="tooltip" href="#"><img class="help" src="../../images/blue-help-icon.png" height="16" width="16" /><span class="custom help"><img class="tooltip" id="img" src="../../images/Help.png" alt="Help" height="48" width="48" /><em>Info</em>Publish the history of your workflow with Galaxy and just provide the link to your history!<br /></span></a>
		    		</td>
		    		<td align="left">
		    			<label id="urlTopLBL">URL to your output dataset:</label><a class="tooltip" href="#"><img class="help" src="../../images/blue-help-icon.png" height="16" width="16" /><span class="custom help"><img class="tooltip" id="img" src="../../images/Help.png" alt="Help" height="48" width="48" /><em>Info</em>Publish the history of your workflow with Galaxy and just provide the link to your history!<br /></span></a>
		    		</td>		    				      			
		    	</tr>
		    	<tr>
		    		<td class="fieldCol">
		    			<input type="text" id="indataseturl" name="indataseturl">
		    		</td>
		    		<td class="fieldCol">
		    			<input type="text" id="outdataseturl" name="outdataseturl">
		    		</td>		      			
		    	</tr>
		    	<tr>			    		
		    		<td align="left">Name of the input dataset:</td>
		    		<td align="left">Name of the output dataset:</td>      				
		    	</tr>
		    	<tr>			    		
		    		<td class="fieldCol"><input type="text" id="indatasetname" name="indatasetname" size="35"></td>
      				<td class="fieldCol"><input type="text" id="outdatasetname" name="outdatasetname" size="35"></td>
		    	</tr>
		    	<tr>
		    		<td align="left">Description:</td>
		    		<td align="left">Description:</td>
		    				    			      			
		    	</tr>
		    	<tr>
		    		<td class="fieldCol"><textarea style="width:250px;" id="indatasetdescription" name="indatasetdescription" rows="5" cols="40" ></textarea></td>	
		    		<td class="fieldCol"><textarea style="width:250px;"id="outdatasetdescription" name="outdatasetdescription" rows="5" cols="40" ></textarea></td>			    			      			
		    	</tr>		    	
		    	<tr>		    		
		    		<td align="left">Access Rights:</td>
		    		<td align="left">Access Rights:</td>
		    		  			
		    	</tr>
		    	<tr>		    		
		    		<td class="fieldCol"><input type="text" id="indatasetrights" name="indatasetrights" size="35"></td>	    
		    		<td class="fieldCol"><input type="text" id="outdatasetrights" name="outdatasetrights" size="35"></td>	      			
		    	</tr>		    	
			</table>

			<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:600px;">
				<tr>
	    			<td colspan="3">
	    				<h2 style="margin: 0; padding:0">Hosting information</h2>
	    			</td>	    			
	    		</tr> 
				<tr>
				<td align="right" style="vertical-align:top; width:200px;">
					<label id="sitelabel">Hosted at Site:</label>
				</td>
				<td align="left" id="listSites" style="width:300px;">
					<select id="site" name="site" onchange="checkVerified()">
     					<option value="0">&lt;Select a Site&gt;</option>
     				</select>
				</td>
				<td align="left" style="width:100px;"><input id="createSiteBtn" type="button" style="background: url(../../images/add-new-site.png) no-repeat; background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;" title="Create a new site" onclick="showSiteForm();" />
				<input id="verifyagain" type="button" onclick="sendVerificationAgain('<%=request.getUserPrincipal().getName()%>');" style="background: url(../../images/sendverif.png) no-repeat; background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px; display:none;" title="Site is still not verified. Send site again for verification." onclick="sendVerfifyEmail"></input>
				</td>					
				</tr>	
				<tr><td colspan="3">&nbsp;</td></tr>			
				<tr>
	    			<td colspan="3">
	    				<h2 style="margin: 0; padding:0">Cite My Data Service</h2>
	    			</td>
	    			
	    		</tr> 	    			
	    		<tr>
	    			<td colspan="3" align="left" valign="top">
	    			<p>
	    				The Cite My Data service will allow research organisations to assign Digital Object Identifiers (DOIs) to research datasets or collections. The DOI system supports the citation of research data in scholarly communications and research data collaborations. See <a href="http://ands.org.au/cite-data" target="_blank">ANDS Cite My Data Service</a> for more information.
	    			</p>	    			
	    			</td>	    			
	    		</tr> 
	    		<tr>
	    			<td class="labelCol" style="width:200px">Assign DOI:</td>
	    			<td colspan="2" align="left">
	    				<input type="checkbox" value="1" name="doi" id="doi" />
	    			</td>
	    		</tr>				
				<tr><td colspan="3">&nbsp;</td></tr>
				<tr>
	    			<td colspan="3" align="center">
	    				<input id="saving" type="button" value="Save" onClick="save(<%=request.getParameter("id")%>,'<%=request.getUserPrincipal().getName()%>', false);" >
	    				<input id="publishing" type="button" value="Publish to RDA" onClick="save(<%=request.getParameter("id")%>,'<%=request.getUserPrincipal().getName()%>', true);">
						<input id="republishing" type="button" value="Save & Publish to RDA" style="display:none;" onClick="save(<%=request.getParameter("id")%>,'<%=request.getUserPrincipal().getName()%>', true);">
	    			</td>
  			 	</tr>
			</table>
	    </form>
	   </div>
	   <div class="blockUI" id="blockUI" style="display: none;">
		<div class="blockUI blockOverlay" style="z-index: 1000; border: medium none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(0, 0, 0);-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=80)'; filter: alpha(opacity=80); opacity: 0.8; position: fixed;" >
		</div>
		<div class="blockUI blockMsg blockPage" style="z-index: 1011; position: fixed;  top: 50%;  left: 50%;  margin-top: -250px;  margin-left: -300px; ">		
            <div style="width: 600px; height: 500px; border: 1px solid #000000; background: #FFFFFF" >
            <img src="../../images/winclose.gif" onclick="document.getElementById('blockUI').style.display = 'none'; refreshSites();" style="display:block; float:right;"/>
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