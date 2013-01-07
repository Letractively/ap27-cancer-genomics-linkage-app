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
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Person.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/PersonManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/InstitutionManager.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/Institution.js'></script>	
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/RifcsIO.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocation.js'></script>
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/DataLocationManager.js'></script>
	<script type='text/javascript' src='../../scripts/account.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script type='text/javascript' >
		$(document).ready(function (){
			$(".errMsg").hide();
			$(".confirmMsg").hide();
			
			var username = "<%=request.getUserPrincipal().getName()%>";			
			populatePage(username);
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
			<div id="section" style="top-margin: 0; width: 950px; min-height: 450px; float: center;">
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
							<td align="left"><input type="email" name="email" size="30" /></td>
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

			<div id="section">
				<% 
     		List<DataLocation> owneddatalocations = new ArrayList<DataLocation>();
			List<DataLocation> partofdatalocations = new ArrayList<DataLocation>();   		
			if (request.getUserPrincipal() != null) {
				String username=request.getUserPrincipal().getName();   
				System.out.println(username);

			   try {
				   PersonManager pm = new PersonManager();
					Person person = pm.getPersonByEmail(username);
					owneddatalocations = pm.returnOwnedDatalocation(person);
					partofdatalocations = pm.returnPartOfDatalocation(person);
					request.setAttribute( "owneddatalocations", owneddatalocations);
					request.setAttribute( "partofdatalocations", partofdatalocations);
			   } catch (Exception e) { 
					e.printStackTrace();  
			   }				
			}			
			%>

				<h1>Data Collections</h1>
				<div id="listOwned" style="width: 900px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
					<h3>You are the <b>owner</b> of the following data libraries</h3>
					<display:table name="owneddatalocations" pagesize="10" class="table" id="owneddatalocation">
						<display:column property="dataSource.name" title="Data Source" escapeXml="true" 
							sortable="true" defaultorder="ascending" maxLength="30"></display:column>
						<display:column property="dataSource.url" title="URL" escapeXml="true" maxLength="25"></display:column>
						<display:column property="site.name" title="Site" escapeXml="true"></display:column>
						<display:column property="site.institution.name" title="Institution" escapeXml="true" maxLength="25"></display:column>
						<display:column title="Action" style="text-align:right;">
							<c:choose>
								<c:when
									test="${owneddatalocation.dataSource.isPublished != true}">
									<input type="button" id="publishrecords${owneddatalocation.id}"
										style="background: url(../../images/Publish.png) no-repeat;
											background-size:28px 28px; height: 32px; width: 32px;"
										title="Publish to RDA"
										onclick="publishrecords(${owneddatalocation.id})"></input>
								</c:when>
							</c:choose>
							<input type="button" id="edit"
							 	style="background: url(../../images/pencil.png) no-repeat; 
							 		background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
							 	title="Edit data record" 
							 	onclick="location.href='data.jsp?id=${owneddatalocation.id}&type=edit'"></input>
							<input type="button" id="stopown"
								style="background: url(../../images/recycle_bin.png) no-repeat; 
									background-size:28px 28px;HEIGHT: 32px; WIDTH: 32px;"
								title="Stop hosting data record" 
								onclick="stopOwning(${owneddatalocation.id});"></input>
						</display:column>

						<display:setProperty name="paging.banner.item_name" value="data source" />
						<display:setProperty name="paging.banner.items_name" value="data sources" />
					</display:table>
				</div>

				<div id="listPartOf"
					style="width: 900px; text-align: left; align: center; margin: 0 auto; margin-top: 30px">
					<h3>You are also <b>hosting</b> the following data libraries</h3>
					<display:table name="partofdatalocations" pagesize="10"
						class="table" id="partofdatalocation"
						style="width:900px;text-align:center">
						<display:column property="dataSource.name" title="Data Source"
							escapeXml="true" defaultorder="ascending" maxLength="30"></display:column>
						<display:column title="Data<br/>Published" style="text-align:center;">
	 							<c:choose>
							<c:when
									test="${partofdatalocation.dataSource.isPublished != true}"> 
									<input type="button" id="publishpart${partofdatalocation.id}"
										style="background: url(../../images/Publish.png) no-repeat; 
											background-size:28px 28px; height: 32px; width: 32px;"
										title="Publish to RDA"
										onclick="publishpartofdl(${partofdatalocation.id})"></input>
						 		</c:when>

							</c:choose> 
						</display:column>
						<display:column property="dataSource.url" title="URL"
							escapeXml="true" maxLength="25"></display:column>
						<display:column property="site.name" title="Site" escapeXml="true"></display:column>
						<display:column property="site.institution.name"
							title="Institution" escapeXml="true" maxLength="25"></display:column>
						<display:column title="Site<br/>Published" style="text-align:center;">
							<c:if
								test="${(partofdatalocation.isAvailable != true) && (partofdatalocation.dataSource.isPublished == true)}">
								<input type="button" id="publishSite${partofdatalocation.id}"
									style="background: url(../../images/Publish.png) no-repeat; 
										background-size:28px 28px; height: 32px; width: 32px;"
									title="Publish Site"
									onclick="publishsite(${partofdatalocation.id})"></input>
							 </c:if>
						</display:column>
						<display:column title="Action" style="text-align:right;">
							<input type="button" id="contactadmin"
								style="background: url(../../images/Places-mail-message-icon.png) no-repeat; 
									background-size:28px 28px; HEIGHT: 32px; WIDTH: 32px;"
								title="Contact admin of data record"
								onclick="location.href='contact.jsp?datasource=${partofdatalocation.dataSource.id}&site=${partofdatalocation.site.id}'"></input>
							<input type="button" id="stoppartof" 
								style="background: url(../../images/recycle_bin.png) no-repeat;
									background-size:28px 28px; height: 32px; width: 32px;"
								onclick="stopHosting(${partofdatalocation.id})"></input>
						</display:column>

						<display:setProperty name="paging.banner.item_name"
							value="Data source" />
						<display:setProperty name="paging.banner.items_name"
							value="Data sources" />
					</display:table>
				</div>

				<!-- <div style="width: 950px; margin: 0 auto; clear: both;">
					<div align="center">
						<h1 style="text-align: left; padding-top: 25px">Workflows</h1>
					</div>
				</div>-->
			</div>
			<div id="footer" style="width: 950px" class="clearfix">
				<jsp:include page="../../common/footer.jsp" />
			</div>
		</div>
	</div>
</body>
</html>