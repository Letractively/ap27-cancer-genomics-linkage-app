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
	<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
	<script type='text/javascript' src='../../scripts/data.js'></script>
	<script type='text/javascript' src='../../scripts/jquery-1.8.2.min.js'></script>
	<script language="javascript">
	function init() {	
		var dataLocationId = <%=request.getParameter("id")%>;
		var actionType = "<%=request.getParameter("type")%>";
		var currentUser = "<%=request.getUserPrincipal().getName()%>";
		
		$("#msg").hide();
		$("#emailMsg").hide();
					
		if (actionType == "edit") {
			turnOnEditData();
			populateDataSource(currentUser, dataLocationId);
		} else if (actionType == "view") {
			populateDataSource(currentUser, dataLocationId);
			turnOnReadOnly(true);
		} else {
			turnOnNewData(currentUser);
		}		
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
		<p><span id="msg"></span></p>
		<form method="POST" action="account.jsp">
		  	<table id="rootTable" cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:550px;">
		    	<tr>
		    		<td class="labelCol"><label id="urlTopLBL">URL:</label></td>
		    		<td class="fieldCol">
		    			<select id="urlSelect" name="urlSelect"></select>
		    			<input type="button" id="loadDataSourceBtn" value="Load" onclick="loadDataSource()"/>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td class="labelCol"><label id="urlBottomLBL">URL:</label></td>
		    		<td class="fieldCol">
		    			<input required type="text" id="url" name="url">
		    		</td>		      			
		    	</tr>
		    	<tr>			    		
		    		<td class="labelCol">Full name of data source:</td>
      			<td class="fieldCol"><input required="" type="text" id="fullname" name="fullname" size="35"></td>
		    	</tr>
		    	<tr>
		    		<td class="labelCol">Abbreviation of data source:</td>
		    		<td class="fieldCol"><input required="" type="text" id="abbrev" name="abbrev" size="35"></td>		      			
		    	</tr>
		    	<tr>		    		
		    		<td class="labelCol">Access Rights:</td>
		    		<td class="fieldCol"><input type="text" id="rights" name="rights" size="35"></td>
	      			
		    	</tr>
		    	<tr>
		    		<td class="labelCol">Description:</td>
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

			<table cellpadding="2" border="0" cellspacing="0" style="margin-left:auto; margin-right:auto; width:550px;">
				<tr><td><h2>Hosting information</h2></td></tr>
				<tr><td align="right">
					<label>Add site: </label>
					<select id="site" name="site">
     					<option value="0">&lt;Select a Site&gt;</option>
     				</select>
					<input id="addSiteBtn" type="button" value="Add" onClick="addSite()" /><br/> 
					<input id="createSiteBtn" type="button" value="Create New Site" onClick="popup('/CollectionInterface/forms/secure/site.jsp', 'New Site', 1200, 800);" />
					</td>
				</tr>
				<tr>
					<td>
					<table id="siteOwnersTable" cellpadding="2" border="0" cellspacing="0" >
					<%
					DataLocation dataLoc = null;
					if (request.getParameter
					("id") != null) {
						int dataLocationId = Integer.parseInt(request.getParameter("id"));
						dataLoc = DataLocationManager.getLocationById(dataLocationId);
					}
					
					if (dataLoc != null) {
						List<Site> siteColumns = DataSourceManager.getSupportedSites(dataLoc.getDataSource());
						List<DataLocation> coOwners = DataSourceManager.getCoOwners(dataLoc.getDataSource());
						
						// display column names
						out.println("<tr>\n<th>Co-owner(s)</th>");
						Map<Integer, Integer> siteColOrder = new HashMap<Integer, Integer>();
						for(int i=0; i < siteColumns.size(); i++) {
							Site site = siteColumns.get(i);
							out.print("<th align='center'>");
							out.print(String.format("<label name='siteName' value='%d'>%s</label>", 
									site.getId(), site.getName()));
							out.println("</th>");
							siteColOrder.put(site.getId().intValue(), i);
						}
						out.println("</tr>");
						
						int currentPersonID = 0;
						List<DataLocation[]> table = new ArrayList<DataLocation[]>();
						DataLocation[] row = null;
						// convert flat table to pivot table where rows are co-owners
						// and columns are the sites
						for(DataLocation dl : coOwners) {
							int personId = dl.getPerson().getId().intValue();
							if (personId != currentPersonID) {
								if (row != null) {
									table.add(row);
								}
								currentPersonID = personId;
								row = new DataLocation[siteColumns.size()];
								for(int i = 0; i < row.length; i++) {
									row[i] = new DataLocation();
									row[i].setPerson(dl.getPerson());
									row[i].setSite(siteColumns.get(i));
								}
							}
							
							int siteId = dl.getSite().getId().intValue();
							if (siteColOrder.containsKey(siteId)) {
								int col = siteColOrder.get(siteId);
								row[col] = dl;
							}
						}
						//save the last row
						if (row != null) {
							table.add(row);
						}
						
						// display pivot table with checkboxes
						for(DataLocation[] coOwnerRow : table) {
							out.println("<tr>");
							for(int i = 0; i < coOwnerRow.length; i++) {
								DataLocation dl = coOwnerRow[i];
								if (i == 0 && dl != null) {
									out.println(String.format("<td class='email'><label name='email' value='%d'>%s</label></td>", 
											dl.getPerson().getId(), dl.getPerson().getEmail()));
								} 
								
								if (i >= 0 && (dl.getId() == null || dl.getId() == 0)) {
									out.println(String.format("<td align='center'><input type='checkbox' value='-1:%d_%d' /></td>",
										dl.getPerson().getId(), dl.getSite().getId()));
								} else {									
									out.print("<td align='center'>");
									out.print(String.format("<input type='checkbox' value='%d:%d_%d'", 
											dl.getId(),dl.getPerson().getId(), dl.getSite().getId()));
									if (dl.getIsOwner()) {
										out.print(" checked='checked'");
									}
									out.println(" /></td>");
								}
							}
							out.println("</tr>");
						}
					} else {
						out.println("<tr>\n<th>Co-owner(s)</th></tr>");
						String email = request.getUserPrincipal().getName();
						Person person = PersonManager.getPersonByEmail(email);
						out.println(String.format("<tr><td class='email'><label value='%d'>%s</label></td></tr>",
							person.getId(), email));
					}
					 %>
					</table></td>
				</tr>
				<tr><td align="left">
					<label>Co-owner email(s):</label><br/>
					<input type="email" name="coOwnerEmail" id="coOwnerEmail" 
						title="Separate multiple emails with: a comma(,), a semicolon(;) or a space( )." />
					<input id="addEmailBtn" type="button" 
							title="Add co-owner" value="Add Co-owner" onclick="addEmail();" /><br/>
					<label id="emailMsg" class="errMsg"></label>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
	    			<td>
	    				<input type="submit" value="Save" onClick="save(<%=request.getParameter("id")%>,'<%=request.getUserPrincipal().getName()%>');">
	    				<input type="button" value="Publish to RDA">
	    			</td>
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