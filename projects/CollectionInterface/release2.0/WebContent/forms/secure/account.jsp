<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" type="text/css" media="all" href="../../css/jquery-ui.css" />
	<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-ui.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Person.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataOwner.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataOwnerManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/PersonManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script>	
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/RifcsIO.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocation.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocationManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Workflow.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/WorkflowManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataSourceManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataSource.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/EmailManager.js'></script>
	<script type='text/javascript' src='../../scripts/account.js'></script>
	<script type='text/javascript' >
		$(document).ready(function (){
			$( "#tabs" ).tabs(); 
			$(".errMsg").hide();
			$(".confirmMsg").hide();
			
			var username = "<%=request.getUserPrincipal().getName()%>";			
			populatePage(username);
			window.scrollTo(0,0);
		});
		
		
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
		<div id="maintree">
		<div id="tabs" style="margin-bottom:50px;">
			<ul>				
				<li><a href="#t-1">Data Collections</a></li>
				<li><a href="#t-2">Workflows</a></li>
				<li><a href="#t-3">Personal Details</a></li>
				<li><a href="#t-4">Site Admin</a></li>
			</ul>
			

			<div id="t-1">
			<div style="top-margin: 0; width: 950px; min-height: 450px; float: center;">
				<%
					List<DataOwner> owneddatas = new ArrayList<DataOwner>();
					System.out.println("We load the tab");
					if (request.getUserPrincipal() != null) {
						String username = request.getUserPrincipal().getName();
						System.out.println(username);

						try {
							PersonManager pm = new PersonManager();
							Person person = pm.getPersonByEmail(username);
							owneddatas = pm.returnOwnedData(person);
							request.setAttribute("owneddatas", owneddatas);
							request.setAttribute("username", username);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				%>

				<h1>Data Collections</h1>
				<input type="hidden" id="userEmail" name="userEmail" value="<%=request.getUserPrincipal().getName()%>">
				<br><br><h3>You can manage the following Data sources</h3>
				<div id="listOwned" style="width: 900px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
					<h3>select one Data source  for hosting information</h3>
					<display:table name="owneddatas" pagesize="10" class="table" id="owneddata">
						<display:column title="Data Source" sortName="dataSource.name">
						<a  href="javascript:populatesitetable(${owneddata.dataSource.id},'${username}')">${owneddata.dataSource.name}</a></display:column>
						<display:column property="dataSource.url" title="URL" escapeXml="true" maxLength="25"></display:column>
						<%-- <display:column property="site.name" title="Site" escapeXml="true"></display:column>
						<display:column property="site.institution.name" title="Institution" escapeXml="true" maxLength="25"></display:column> --%>
						<display:column title="Action" style="text-align:right;">
							<c:choose>
								<c:when	test="${owneddata.isOwner == true}">
									<input type="button" id="edit"
							 	style="background: url(../../images/pencil.png) no-repeat; 
							 		background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
							 	title="Edit data record" 
							 	onclick="location.href='data.jsp?id=${owneddata.dataSource.id}&type=edit'"></input>
							 	
							 		<input type="button" id="stopadmin"
								style="background: url(../../images/stopOwning.png) no-repeat; 
									background-size:28px 28px;HEIGHT: 32px; WIDTH: 32px;"
								title="Stop Administrating this Data source" 
								onclick="stopadmin(${owneddata.id});"></input>
								
								<input type="button" id="stopadminandremove"
								style="background: url(../../images/recycle_bin.png) no-repeat; 
									background-size:28px 28px;HEIGHT: 32px; WIDTH: 32px;"
								title="Stop Administrating this Data source and remove it from your list" 
								onclick="stopadminandremove(${owneddata.id});"></input>
								</c:when>
								<c:otherwise>
									<input type="button" id="view"
							 	style="background: url(../../images/binoc.png) no-repeat; 
							 		background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
							 	title="View data record" 
							 	onclick="location.href='data.jsp?id=${owneddata.dataSource.id}&type=view'"></input>
							 	<input type="button" id="contactadmin"
								style="background: url(../../images/Places-mail-message-icon.png) no-repeat; 
								background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
								title="Contact admin of data record"
								onclick="location.href='contact.jsp?datasource=${owneddata.dataSource.id}'">
								<input type="button" id="stopadminandremove"
								style="background: url(../../images/recycle_bin.png) no-repeat; 
									background-size:28px 28px;HEIGHT: 32px; WIDTH: 32px;"
								title="Remove this Data source from your list" 
								onclick="stopadminandremove(${owneddata.id});"></input>
								</c:otherwise>
							</c:choose>	 	
						</display:column>
						<display:setProperty name="paging.banner.item_name" value="data source" />
						<display:setProperty name="paging.banner.items_name" value="data sources" />
					</display:table>
				</div>
				
				<div id=datasitesBlock style="width: 500px; text-align: left; align: center; margin: 0 auto; margin-top: 10px">
				<br> <label id="sitesTitle" style="font-weight: bold"></label><label> is hosted by the following site(s):</label>
					<table id="sites" class="tableusers">
						<thead>
						<th>Site</th>
						<th>Intitute</th>
						<th>URL</th>
						<th>Actions</th>
						</thead>
						<tbody id=sitesbody>
						</tbody>
						</table>
					</div>
					<br>
				</div>
				</div>
				<div id="t-2">
				<div style="top-margin: 0; width: 950px; min-height: 450px; float: center;">
				<h1>Workflows</h1>
				<%
					List<Workflow> ownedworkflows = new ArrayList<Workflow>();
					List<Workflow> workflows = new ArrayList<Workflow>();
					if (request.getUserPrincipal() != null) {
						String username = request.getUserPrincipal().getName();
						System.out.println(username);

						try {
							PersonManager pm = new PersonManager();
							Person person = pm.getPersonByEmail(username);
							workflows = pm.returnOwnedWorkflow(person);
							if(workflows != null){
								for(int i=0; i< workflows.size(); i++){
									if(workflows.get(i).getIsHosted()){
										ownedworkflows.add(workflows.get(i));
									}
								}
							}else{
								ownedworkflows = workflows;
							}
							request.setAttribute("ownedworkflow", ownedworkflows);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				%>
				
				<div id="listOwned" style="width: 900px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
					<h3>You are the <b>owner</b> of the following workflows</h3>
					<display:table name="ownedworkflow" pagesize="10" class="table" id="ownedworkflow" requestURI="#t-2">
						<display:column property="name" title="Workflow" escapeXml="true" 
							sortable="true" defaultorder="ascending" maxLength="30"></display:column>
						<display:column property="url" title="URL" escapeXml="true" maxLength="25"></display:column>
						<display:column property="site.name" title="Site" escapeXml="true"></display:column>
						<display:column property="doi" title="DOI" escapeXml="true" maxLength="25"></display:column>						
						<display:column title="Action" style="text-align:right;">
							<c:choose>
								<c:when
									test="${ownedworkflow.isWfPublished != true}"> 
 									<input type="button" id="publishwf${ownedworkflow.id}" 
										style="background: url(../../images/Publish.png) no-repeat;
											background-size:28px 28px; height: 32px; width: 32px;" 
										title="Publish to RDA"
										onclick="publishwf(${ownedworkflow.id})"></input>
								</c:when>
							</c:choose>
							<input type="button" id="edit"
							 	style="background: url(../../images/pencil.png) no-repeat; 
							 		background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
							 	title="Edit workflow record" 
							 	onclick="location.href='workflow.jsp?id=${ownedworkflow.id}&type=edit'"></input>
							<input type="button" id="stopownwf"
								style="background: url(../../images/recycle_bin.png) no-repeat; 
									background-size:28px 28px;HEIGHT: 32px; WIDTH: 32px;" 
								title="Stop hosting workflow record" 
								onclick="stopHostingWf(${ownedworkflow.id});"></input>
						</display:column>

						<display:setProperty name="paging.banner.item_name" value="data source" />
						<display:setProperty name="paging.banner.items_name" value="data sources" />
					</display:table>				
				</div>
				</div>
				</div>
				<div id="t-3">
				<div style="top-margin: 0; width: 950px; min-height: 500px; float: center;">
			
				<div id="left" style="top-margin: 0; width: 475px; float: left;">
					<h1>Personal Details</h1>
					<table cellpadding="2" border="0" cellspacing="0" style="margin-left: auto; margin-right: auto">
						<tr>
							<td align="right">Title:</td>
							<td align="left" id="userTitle"></td>
						</tr>
						<tr>
							<td align="right">First Name:</td>
							<td align="left"><input id="firstName" type="text"
								name="firstName" size="30" /></td>
						</tr>
						<tr>
							<td align="right">Last Name:</td>
							<td align="left"><input type="text" name="lastName"
								size="30" /></td>
						</tr>
						<tr>
							<td align="right">Email:</td>
							<td align="left"><input id="personemail" type="email" name="email" size="30" /></td>
						</tr>
						<tr style="padding-top:15px">
							<td align="right" style="vertical-align:top;"><label>Associated Institutions:</label></td>
							<td align=left>
								<select name="selectionInstits" id="selectionInstits" size="7" multiple="multiple"/><br/>
							 	<input id="deleteInstit" type="button" value="Delete" onclick="removeSelection();" />
							</td>
						</tr>
						<tr>
							<td align="right">Institutions:<br />
							<td align="left" id="listIntits"></td>
							<td><input id="addInputs" type="button"	value="Add" onclick="addInstits();" /></td>
						</tr>
					</table>
					<input type="button" value="Update Details" onclick="saveDetails();" />
				</div>
				<div id="right" style="width: 475px; float: left; top-margin: 0;">
					<h1>Password</h1>
					<table>
						<tr>
							<td align="right">Enter Current Password:</td>
							<td><input type="password" id="currentPassword" name="currentPassword" size="30"/></td>
						</tr>
						<tr>
							<td align="right">Enter <strong>NEW</strong> Password:</td>
							<td><input type="password" id="newPassword" name="newPassword" size="30"/></td>
						</tr>
						<tr>
							<td align="right">Confirm new password:</td>
							<td><input type="password" size="30" id="confirmPassword" name="confirmPassword" onkeyup="matchPasswords();" />
							</td>
						</tr>
						<tr>
							<td align="right">
								<label id="pwdMatchErr" class="errMsg">Passwords do not match</label>
								<label id="pwdResetErr" class="errMsg">Current Password does not match</label>
								<label id="pwdResetMsg" class="confirmMsg">Password Updated</label>
							</td>
							<td align="right">
								<input type="button" id="resetPasswordBTN" name="resetPasswordBTN" value="Change Password" 
									onclick="resetPassword('<%=request.getUserPrincipal().getName()%>')"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
			</div>
				<div id="t-4">
					<div id="listsites"
						style="width: 500px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
						<%
							if (request.getUserPrincipal() != null) {
								String username = request.getUserPrincipal().getName();
								try {
									PersonManager pm = new PersonManager();
									Person person = pm.getPersonByEmail(username);
									List<Site> usersites = new ArrayList<Site>(
											person.getAdminSites());
									System.out
											.println("List of User Sites " + usersites.size());
									request.setAttribute("usersites", usersites);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						%>
						<p><span style ="display: inline-block; width: 500px;"id="msg"></span></p>
						<label id="adminSitesHelp">Please select a site to view the list of Administrators:</label>
						<display:table name="usersites" pagesize="10" class="table"
							id="usersite">
							<display:column title="Site"><a  href="javascript:populateusertable(${usersite.id})">${usersite.name}</a></display:column>
							<display:column property="url" title="URL"	escapeXml="true" maxLength="25"></display:column>
							<display:column property="institution.name" title="Institution"
								escapeXml="true" maxLength="25"></display:column>
							<display:column title="Action" style="text-align:center;width:90px" >
							<c:choose>
							<c:when test="${usersite.isVerified != true}">
									<input type="button" id="sendverif${usersite.id}"
									title="Site not Verified.Send an Email for Verification"
									style='background: url(../../images/sendverif.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;'
									onclick="resendsiteVerification('<%=request.getUserPrincipal().getName()%>',${usersite.id});">
									</input>
							</c:when>
							</c:choose>
								<input type="button" id="stop${usersite.id}"
									title="Stop Administrating this Website"
									style='background: url(../../images/recycle_bin.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;'
									onclick="removefromsite(${usersite.id});"></input>
							</display:column>
							<display:setProperty name="paging.banner.item_name"
								value="site" />
							<display:setProperty name="paging.banner.items_name"
								value="sites" />
						</display:table>
					</div>
					<br>
					<div id="adminsitesBlock">
					<div id="extraSiteInfo" style="width: 500px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
					<table id="tableinfosite" class="tableinfosite" cellpadding="2" border="1" cellspacing="0" style="width: 500px;table-layout:fixed">
						<tr>
							<th align="right" style="width:90px">Name</td>
							<td align="left" id="siteName" style="width:410px"></td>
						</tr>
						<tr>
							<th align="right" style="width:90px">Description</td>
							<td align="left" id="siteDesc" style="width:410px"></td>
						</tr>
						<tr>
							<th align="right" style="width:90px">Rights</td>
							<td align="left" id="siteRights" style="width:410px"></td>
						</tr>
						<tr>
							<th align="right" style="width:90px">URL</td>
							<td align="left" id="siteURL" style="width:410px"></td>
						</tr>
						<tr>
							<th align="right" style="width:90px">Institution</td>
							<td align="left" id="siteInstit" style="width:410px"></td>
						</tr>
						<tr>
							<th align="right" style="width:90px">Status</td>
							<td align="left" id="siteVerif" style="width:410px"></td>
						</tr>
					</table>
					
					* Please Contact QFAB at support@qfab.org if you desire to change information about this site<br>
					<br><br>					
					
					</div>
					<label>Administrator of the site: </label><label id="userTitle1" style="font-weight:bold"></label>
						<div id="listusers"
							style="width: 500px; text-align: left; align: center; margin: 0 auto; margin-top: 10px">
							<table id="users" class="tableusers">
								<thead>
									<th>Name</th>
									<th>Email</th>
									<th>Action</th>
								</thead>
								<tbody id=usersbody>
								</tbody>
							</table>
					</div>
					<br>
					<div id="tableadmins">
						<label>Add new Administrator to site: </label><label
							id="userTitle2" style="font-weight:bold"></label><br /> <label>Email: </label><input
							type="email" name="adminSiteEmail" id="adminSiteEmail"
							title="Separate multiple emails with: a comma(,), a semicolon(;) or a space( )." />
						<input id="addAdminBtn" type="button" title="Add Site Admin"
							value="Add Site Admin" onclick="addAdmin()"/> <br /> <label
							id="emailMsg" class="errMsg"></label> <br />
						<br />
					</div>
					</div>
					<br>
				</div>

			</div>
			</div>
		<div class="blockUI" id="blockUI">
		<div class="blockUI blockOverlay" style="z-index: 1000; border: medium none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(0, 0, 0);-ms-filter:'progid:DXImageTransform.Microsoft.Alpha(Opacity=80)'; filter: alpha(opacity=80); opacity: 0.8; position: fixed;" >
		</div>
		<div class="blockUI blockMsg blockPage" style="z-index: 1011; position: fixed;  top: 50%;  left: 50%;  margin-top: -19px;  margin-left: -220px; ">		
            <div>
            <img src="../../images/ajax-loader.gif"/>		
			</div>		
		</div>
		</div>	
			<div id="footer" style="width: 950px" class="clearfix">
				<jsp:include page="../../common/footer.jsp" />
			</div>
		</div>
	</div>
</body>
</html>