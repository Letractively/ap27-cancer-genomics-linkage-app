function checkVerified(){
	var value = document.getElementById("site").value;
	if(value !=0){
		SiteManager.getSiteById(value, {callback:function(site) {
			if(!site.isVerified){
				document.getElementById("verifyagain").style.display="";	
			}else{
				document.getElementById("verifyagain").style.display="none";		
			}
			}
		});
	}
	
}

function sendVerificationAgain(currentUser){	
	var siteId = document.getElementById("site").value;	
	EmailManager.sendSiteVerificationEmailAgain(currentUser, siteId);	
	$("#msg").text("The site has been sent for verification again. Please note site verifications can take up to 24hrs.");
	$("#msg").removeClass("errMsg");
	$("#msg").addClass("confirmMsg");
	$("#msg").show();
	window.scrollTo(0,0);
	
	
}

function set(select) {
	  var val = select.options[select.options.selectedIndex].value;
	  document.getElementById('codeCat').value=val;
}

function insertANZ(){	
	 var cat =document.getElementById('codeCat').value;
	 if(cat =="anzsrc_for"){
		 theSel= document.getElementById('selectionFOR');
	 } else {
		 theSel= document.getElementById('selectionSEO'); 
	 }
	 
	 var inList = false;
	 var value =document.getElementById('anzsrc').value;
	 var subjects = value.split(/[,\s]+/);
	 for (var i = 0; i < subjects.length; i++) {
		 var newSubj =subjects[i];
		 for (var j = 0; j < theSel.length; j++) {
			 if (theSel.options[j].text == newSubj) {
				 inList = true;
				 break;
			 }
		 }
		 if (!inList) {
			 var newOpt = new Option(newSubj, newSubj);
		 	theSel.options[theSel.length] = newOpt;
		 }
	 }
}

function removeSelection(theSel){
  var selIndex = theSel.selectedIndex;
  if (selIndex != -1) {
    for(var i = theSel.length-1; i>=0; i--) {
      if(theSel.options[i].selected)
      {
        theSel.options[i] = null;
      }
    }
    if (theSel.length > 0) {
      theSel.selectedIndex = selIndex == 0 ? 0 : selIndex - 1;
    }
  }
}

function addOption(selectbox, value, text,currentname, group) {
	var optn = document.createElement("option");
	optn.text = text;
	optn.value = value;
	if(text==currentname){
		optn.selected=true;
	};
	if (group != null) {
		optn.label = text;
		group.appendChild(optn);
	}
	else {
		selectbox.options.add(optn);
	}
}



function popup(url, name, width, height) {
	settings=
		"toolbar=no,location=yes,directories=yes,"+
		"status=no,menubar=no,scrollbars=yes,"+
		"resizable=yes,width="+width+",height="+height;
	MyNewWindow=window.open(url, name, settings);
}


function save(workflowId, currentUser, publish){	
	
	var wfName = dwr.util.getValue("wfname");
	var wfURL = dwr.util.getValue("url");
	var wfRights = dwr.util.getValue("rights");
	var wfDescription = dwr.util.getValue("description");	
	var wfSubjects = [];
	var theSel = document.getElementById("selectionFOR");	
	for (var i=0; i < theSel.length; i++) {		
		wfSubjects.push({code: theSel.options[i].text, 
			type : 'anzsrc_for', id : theSel.options[i].value});		
	}	
	theSel = document.getElementById('selectionSEO');
	for (var i=0; i < theSel.length; i++) {	
		wfSubjects.push({ code: theSel.options[i].text, 
			type : 'anzsrc_seo', id : theSel.options[i].value});		
	}
	var indsName = dwr.util.getValue("indatasetname");
	var indsURL = dwr.util.getValue("indataseturl");
	var indsRights = dwr.util.getValue("indatasetrights");
	var indsDescription = dwr.util.getValue("indatasetdescription");
	
	var outdsName = dwr.util.getValue("outdatasetname");
	var outdsURL = dwr.util.getValue("outdataseturl");
	var outdsRights = dwr.util.getValue("outdatasetrights");
	var outdsDescription = dwr.util.getValue("outdatasetdescription");
	var siteId = dwr.util.getValue("site");
	var hasDOI = dwr.util.getValue("doi");

	
	if(validateForm(wfName, wfURL, wfSubjects, siteId)){
		WorkflowManager.saveOrUpdate(workflowId, currentUser, wfName, wfURL, wfRights, wfDescription, wfSubjects, indsName, indsURL, indsRights, indsDescription, outdsName, outdsURL, outdsRights, outdsDescription, siteId, hasDOI, {
			callback:function(successful) {
				if (successful) {
					if(publish){
						SiteManager.getSiteById(siteId, {callback:function(site) {
							if(site.isVerified){
								RifcsIO.writeRifcsforWorkflow(workflowId, wfURL);
								$("#msg").text("Published to RDA");
								$("#msg").removeClass("errMsg");
								$("#msg").addClass("confirmMsg");
								$("#msg").show();
								document.getElementById("saving").style.display="none";
								document.getElementById("publishing").style.display="none";
								document.getElementById("republishing").style.display="";
								window.scrollTo(0,0);
							}else{
								$("#msg").text("Publish to RDA FAILED - The Site your workflow is hosted at has not been verified yet.\n\nPlease send the site again for verification, if it has been longer than 24hrs since your last request.\n\n Site verification can take up to 24hrs.");			
								$("#msg").removeClass("confirmMsg");
								$("#msg").addClass("errMsg");
								$("#site").addClass("error");
								$("#msg").show();
								window.scrollTo(0,0);							
							}
							}
						});
						}
					else{					
						$("#msg").text("Save Complete");
						$("#msg").removeClass("errMsg");
						$("#msg").addClass("confirmMsg");
						$("#msg").show();
						window.scrollTo(0,0);
					}
				} else {
					
					$("#msg").text("Save FAILED - There is already an existing record with this URL! Please edit the existing workflow record, if you are the owner.");			
					$("#msg").removeClass("confirmMsg");
					$("#msg").addClass("errMsg");
					$("#msg").show();
					window.scrollTo(0,0);
					
				}
			}
		});
		
		if(workflowId==null || workflowId==""){
			$('#url').attr('readonly', true);	
		}
	}else{
		if(publish){	
			$("#msg").text("Published FAILED - Missing Required Values");
		}else{
			$("#msg").text("Save FAILED - Missing Required Values");
		}
		$("#msg").removeClass("confirmMsg");
		$("#msg").addClass("errMsg");
		$("#msg").show();
		window.scrollTo(0,0);
		
	}
	
}

function populateWorkflow(currentUser, workflowId){	
	WorkflowManager.getWorkflowById(workflowId,{
		callback : function(wf) {
			if (wf==null) { 
				alert("This data record does not exist");
				window.location="account.jsp";
			}
			if (currentUser != wf.person.email) {
				alert("You do not have access to this record");
				window.history.back(-1);
			}
			
			if (wf.isWfPublished){
				document.getElementById("saving").style.display="none";
				document.getElementById("publishing").style.display="none";
				document.getElementById("republishing").style.display="";
			}
			
			dwr.util.setValue("url", wf.url);
			dwr.util.setValue("wfname", wf.name);
			dwr.util.setValue("description", wf.description);
			dwr.util.setValue("rights", wf.rights);
						
			dwr.util.removeAllOptions("selectionFOR");
			dwr.util.removeAllOptions("selectionSEO");
			for (var i=0; i < wf.subjects.length; i++) {
				if (wf.subjects[i].type == "anzsrc_for") {
					dwr.util.addOptions("selectionFOR", [wf.subjects[i]], "code");
				} else {
					dwr.util.addOptions("selectionSEO", [wf.subjects[i]], "code");
				}
			}
					
			dwr.util.setValue("indataseturl", wf.inUrl);
			dwr.util.setValue("indatasetname", wf.inName);
			dwr.util.setValue("indatasetdescription", wf.inDescription);
			dwr.util.setValue("indatasetrights", wf.inRights);
			
			dwr.util.setValue("outdataseturl", wf.outUrl);
			dwr.util.setValue("outdatasetname", wf.outName);
			dwr.util.setValue("outdatasetdescription", wf.outDescription);
			dwr.util.setValue("outdatasetrights", wf.outRights);
			
			var siteselectbox = document.getElementById("site");
			SiteManager.getSites({callback:function(sites) {
				hosts ={};
				hosts = sites;					
			     for (var i=0; i<hosts.length; i++) {
			      		addOption(siteselectbox, hosts[i].id, hosts[i].name, "");
			      		
			     }
			     document.getElementById('site').selectedIndex = parseInt(wf.site.id);
			     if(wf.site.id !=0){
			    	 if(!wf.site.isVerified){
			    
			    	 document.getElementById("verifyagain").style.display="";
			     }
			     }
				}
			
			    });
			document.getElementById("doi").checked = wf.hasDoi;
			InstitutionManager.getAllInstitutions({
				callback:function(institutes) {
					dwr.util.removeAllOptions("selectaffiliation");
					dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
				}
			 });
			dwr.util.setValue("email", currentUser);
			
						
		}
	});	
}
function populateWorkflowWithErrors(currentUser, workflowId){	
	validValues=true;	
	validSite=true;
	WorkflowManager.getWorkflowById(workflowId,{
		callback : function(wf) {
			if (wf==null) { 
				alert("This data record does not exist");
				window.location="account.jsp";
			}
			if (currentUser != wf.person.email) {
				alert("You do not have access to this record");
				window.history.back(-1);
			}
			
			if (wf.isWfPublished){
				document.getElementById("saving").style.display="none";
				document.getElementById("publishing").style.display="none";
				document.getElementById("republishing").style.display="";
			}
			var name = wf.name;
			var url = wf.url;
			name = name.replace(/^\s+|\s+$/g,"");
			url = url.replace(/^\s+|\s+$/g,"");
			
			dwr.util.setValue("url", url);
			if(url == null || url==""){
				validValues=false;
				$("#url").addClass("error");
				$("#urllabel").addClass("error");				
			}			
			
			dwr.util.setValue("wfname", name);
			if(name == null || name==""){
				validValues=false;
				$("#wfname").addClass("error");
				$("#namelabel").addClass("error");	
			}
			
			dwr.util.setValue("description", wf.description);
			dwr.util.setValue("rights", wf.rights);
						
			dwr.util.removeAllOptions("selectionFOR");
			dwr.util.removeAllOptions("selectionSEO");
			for (var i=0; i < wf.subjects.length; i++) {
				if (wf.subjects[i].type == "anzsrc_for") {
					dwr.util.addOptions("selectionFOR", [wf.subjects[i]], "code");
				} else {
					dwr.util.addOptions("selectionSEO", [wf.subjects[i]], "code");
				}
			}
					
			dwr.util.setValue("indataseturl", wf.inUrl);
			dwr.util.setValue("indatasetname", wf.inName);
			dwr.util.setValue("indatasetdescription", wf.inDescription);
			dwr.util.setValue("indatasetrights", wf.inRights);
			
			dwr.util.setValue("outdataseturl", wf.outUrl);
			dwr.util.setValue("outdatasetname", wf.outName);
			dwr.util.setValue("outdatasetdescription", wf.outDescription);
			dwr.util.setValue("outdatasetrights", wf.outRights);
			
			var siteselectbox = document.getElementById("site");
			SiteManager.getSites({callback:function(sites) {
				hosts ={};
				hosts = sites;					
			     for (var i=0; i<hosts.length; i++) {
			      		addOption(siteselectbox, hosts[i].id, hosts[i].name, "");
			      		
			     }
			     document.getElementById('site').selectedIndex = parseInt(wf.site.id);
			     if(wf.site.id !=0){
			    	 if(!wf.site.isVerified){			    
			    	 document.getElementById("verifyagain").style.display="";
			    	 validSite =false;
			    	 $("#site").addClass("error");			    	 
			     }
			     }
			     
			     if(!validValues){
						$("#msg").text("Publish FAILED - Missing Required Values");			
						$("#msg").removeClass("confirmMsg");
						$("#msg").addClass("errMsg");
						$("#msg").show();
						window.scrollTo(0,0);
					}else{
						if(!validSite){
							$("#msg").text("Publish to RDA FAILED - The Site your workflow is hosted at has not been verified yet.\n\nPlease send the site again for verification, if it has been longer than 24hrs since your last request.\n\n Site verification can take up to 24hrs.");			
							$("#msg").removeClass("confirmMsg");
							$("#msg").addClass("errMsg");
							$("#site").addClass("error");
							$("#msg").show();
							window.scrollTo(0,0);			
							
						}
						
					}
				}			
			
			    });
			document.getElementById("doi").checked = wf.hasDoi;
			InstitutionManager.getAllInstitutions({
				callback:function(institutes) {
					dwr.util.removeAllOptions("selectaffiliation");
					dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
				}
			 });
			dwr.util.setValue("email", currentUser);
			
			
						
		}
	});	
}

function populateSiteForm(currentUser){	
	var siteselectbox = document.getElementById("site");
	SiteManager.getSites({callback:function(sites) {
		hosts ={};
		hosts = sites;			
	     for (var i=0; i<hosts.length; i++) {
	      		addOption(siteselectbox, hosts[i].id, hosts[i].name, "");
	      		
	     }	     
		}
	
	    });
	InstitutionManager.getAllInstitutions({
		callback:function(institutes) {
			dwr.util.removeAllOptions("selectaffiliation");
			dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
		}
	 });
	dwr.util.setValue("email", currentUser);
}


function validateForm(name, url, subjects, site){
	valid=true;	
	name = name.replace(/^\s+|\s+$/g,"");
	url = url.replace(/^\s+|\s+$/g,"");

	if(url==null || url==""){					
		$("#url").addClass("error");
		$("#urllabel").addClass("error");
		valid=false;
	}else{
		$("#url").removeClass("error");
		$("#urllabel").removeClass("error");	
	}
	
	if(name==null || name==""){					
		$("#wfname").addClass("error");
		$("#namelabel").addClass("error");		
		valid=false;		
	}else{
		$("#wfname").removeClass("error");
		$("#namelabel").removeClass("error");		
	}	
	
	if(subjects.length > 0){
		$("#selectionFOR").removeClass("error");
		$("#selectionSEO").removeClass("error");
		$("#anzforlabel").removeClass("error");
		$("#anzseolabel").removeClass("error");
		
	}else{
		$("#selectionFOR").addClass("error");
		$("#selectionSEO").addClass("error");
		$("#anzforlabel").addClass("error");
		$("#anzseolabel").addClass("error");
		valid=false;
	}
	if(site==null || site==0){
		$("#sitelabel").addClass("error");
		$("#site").addClass("error");
		valid=false;		
	}
	else{
		$("#sitelabel").removeClass("error");
		$("#site").removeClass("error");
						
	}
	
	
	return valid;
}

function validateSiteForm(name, description, url, rights){
	valid=true;
	if(name==null || name==""){					
		$('#requiredname').show();
		valid=false;
		
	}else{
		$('#requiredname').hide();
	}
	
	if(description==null || description==""){					
		$('#requireddescription').show();
		valid=false;		
	}else{
		$('#requireddescription').hide();
	}
	
	if(url==null || url==""){					
		$('#requiredurl').show();
		valid=false;		
	}else{
		$('#requiredurl').hide();
	}
	
	if(rights==null || rights==""){					
		$('#requiredrights').show();
		valid=false;		
	}else{
		$('#requiredrights').hide();
	}
	return valid;	
}

function validateSite(form){		
	name=form.sname.value;
	description=form.sdescription.value;
	url=form.surl.value;
	rights=form.srights.value;
	affiliation=form.selectaffiliation.value;
	email=form.email.value;
	var isAdmin = false;
	valid=validateSiteForm(name, description, url, rights);
	if(valid){			
			SiteManager.registerNewSite(	
							name, 
							description, 
							url,
							rights,					 
							affiliation, 
							email,
							isAdmin,
							{callback:function(value){						
								if(value){									
									$("#siteMsg").text("Your site has been sent for verification.");															
								}else{	
									$("#siteMsg").text("Your site is already verified.");							
								}						
								
							}
							});						
		
		document.getElementById('siteForm').style.display = 'none';
		$("#siteMsg").show();
	}
	
}

function refreshSites(){
	dwr.util.removeAllOptions("site");
	var siteselectbox = document.getElementById("site");	
	SiteManager.getSites({callback:function(sites) {
		hosts ={};
		hosts = sites;				
	     for (var i=0; i<hosts.length; i++) {
	      		addOption(siteselectbox, hosts[i].id, hosts[i].name, "");
	      		
	     }		
	}
	});
		
}

function showSiteForm(){
	dwr.util.setValue("sname", "");
	dwr.util.setValue("sdescription", "");
	dwr.util.setValue("surl", "");
	dwr.util.setValue("srights", "");
	document.getElementById('blockUI').style.display = '';
	document.getElementById('siteForm').style.display='';
	$("#siteMsg").hide();
}





