<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>RIF-CS Manager</title>
	<link rel="stylesheet" type="text/css" media="all" href="css/theme.css" />
	<script language="javascript">
	</script>
</head>
<body>
	<div id="page" style="width:1050px;text-align:center;min-height:700px">
		<div id="header" style="height:100px;" class="clearfix">
			<jsp:include page="common/header.jsp"/>
			<h1>RIF-CS Manager</h1>
			<%
				if (request.getUserPrincipal() != null) {
					String email = request.getUserPrincipal().getName();					
					request.setAttribute( "userEmail", email);
				}
			 %>
			<c:if test="${userEmail != null}">
				<jsp:include page='common/navigation.jsp'/>
			</c:if>
			<jsp:include page="common/user.jsp"/>
		</div>
		<div id="maintree" style="text-align:left; min-height:300px; padding-left:60px; padding-right:60px;" class="clearfix">
		<p>Welcome to the</p>
		<h1>RIF-CS Manager</h1>
		<p>developed for managing the <a href="http://www.ands.org.au/resource/rif-cs.html">RIF-CS</a> records 
		that describe the  reference datasets and public workflows from the <a href="http://ap27-cgla.blogspot.com.au/">Cancer 
		Genomics Linkage Application</a> implemented using the <a href="http://genome.edu.au">Genomics Virtual Lab (GVL)-Early Activity</a> 
		project.</p>
		
		<p>The <a href="http://genome.edu.au">Genomics Virtual Lab-Early Activity</a>
		 is hosted at three sites: </p> 
		<ul>		
			<li>GVL at The University of Queensland (UQ)</li>
			<li>GVL at the Garvan Institute</li>
			<li>GVL at Victorian Life Science of Computation Initiative (VLSCI)</li>
		</ul>
		
		
		<p>This web-interface provides a administrative dashboard for users to manage their RIF-CS 
		records to be published on <a href="http://researchdata.ands.org.au/">Research Data Australia
		(RDA)</a>. These records provide metadata about the reference datasets and workflows. The records 
		also include information about which GVL site is hosting the data or workflow for re-use. 
		The RDA aims to assists Australian Researchers with ease of publishing, discovering, accessing and re-using research data.</p>
		<div id="footer" style="width:950px" class="clearfix">
			<jsp:include page="common/footer.jsp"/>
		</div>
	</div>
	</div>
</body>
</html>