<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.qfab.managers.InstitutionManager"%>
<%@ page import="org.qfab.domains.Institution"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>RIFCS Manager - Site Verification</title>
<link rel="stylesheet" type="text/css" media="all" href="../../css/theme.css" />
<script type='text/javascript' src='/CollectionInterface/dwr/engine.js'></script>
        		<script type='text/javascript' src='/CollectionInterface/dwr/util.js'></script>        				
        		<script type='text/javascript' src='/CollectionInterface/dwr/interface/DBUtils.js'></script>
        		<script type='text/javascript' src='/CollectionInterface/dwr/interface/SiteManager.js'></script>
        		<script type='text/javascript' src='/CollectionInterface/dwr/interface/EmailManager.js'></script>
        		<script type='text/javascript' src='../../scripts/site.js'></script>
<script language="javascript">
function init() {
	var sitename ="<%=request.getParameter("name")%>";	
	var sitedescription ="<%=request.getParameter("description")%>";
	var siteurl ="<%=request.getParameter("url")%>";
	var siterights ="<%=request.getParameter("rights")%>";
	var siteaffiliation ="<%=request.getParameter("selectaffiliation")%>";	
	var siteemail="<%=request.getParameter("email")%>";
	saveSite(siteemail, sitename, sitedescription, siteurl, siterights, siteaffiliation);		
}
</script>
</head>
<body onload="init()">
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="../../common/header.jsp"/>
			<h1>RIF-CS Manager - Site Verification</h1>
			<jsp:include page="../../common/navigation.jsp"/>
			<jsp:include page="../../common/user.jsp"/>
		</div>
		
		<div id="maintree" style="text-align:center; min-height:300px" class="clearfix">
			<h1 style="text-align:center; padding-top:25px">Site Verification</h1>			
			<br/>
			<div id="message">
			
			</div>
			<br/>
		</div>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="../../common/footer.jsp"/>
		</div>
	</div>
</body>
</html>