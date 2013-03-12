function populateDataSource(currentUser, dataSourceId) {
	
	DataSourceManager.getDataSourceById(dataSourceId,{
		callback : function(dataSource) {
			if (dataSource==null) { 
				alert("This data record does not exist");
				window.location="account.jsp";
			}
			dwr.util.setValue("fullname", dataSource.name);
			dwr.util.setValue("abbrev", dataSource.abbrev);
			dwr.util.setValue("url", dataSource.url);
			dwr.util.setValue("rights", dataSource.rights);
			dwr.util.setValue("description", dataSource.description);

			//load subject codes
			dwr.util.removeAllOptions("selectionFOR");
			dwr.util.removeAllOptions("selectionSEO");
			for (var i=0; i < dataSource.subjects.length; i++) {
				if (dataSource.subjects[i].type == "anzsrc_for") {
					dwr.util.addOptions("selectionFOR", [dataSource.subjects[i]], "code");
				} else {
					dwr.util.addOptions("selectionSEO", [dataSource.subjects[i]], "code");
				}
			}
			
			InstitutionManager.getAllInstitutions({
				
			    callback:function(institutes) {
			     dwr.util.removeAllOptions("selectaffiliation");
			     dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
			    }
			    });
			   dwr.util.setValue("email", currentUser);
			
			setInstitutions(currentUser);
			populatefieldset(currentUser,dataSourceId);
			

			//});
		}
	});	
}

function validateSiteforDataSource(form){		
	name=form.sname.value;
	description=form.sdescription.value;
	url=form.surl.value;
	rights=form.srights.value;
	affiliation=form.selectaffiliation.value;
	email=form.email.value;
	var isAdmin = true;
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

function setInstitutions(currentUser){
	InstitutionManager.getAllInstitutions({
		
	    callback:function(institutes) {
	     dwr.util.removeAllOptions("selectaffiliation");
	     dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
	    }
	    });
	   dwr.util.setValue("email", currentUser);
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

function populatefieldset(currentUser, dataSourceId){
	$('#site_list').empty();
	var legend = document.createElement('legend');
	legend.innerHTML="Sites";
	document.getElementById("site_list").appendChild(legend);
	if(dataSourceId==0 ||dataSourceId==null){
		newsitelist(currentUser);
	}else{
	DataSourceManager.getDataSourceById(dataSourceId,{
		callback : function(dataSource) {
	SiteManager.getSitesforPerson(currentUser, function(siteforAdmin){
		//DataSourceManager.getDataLocationsForDataSource(dataSource, function(dls) {
			var dls = dataSource.dataLocations;
			if(siteforAdmin!=null){
			for(var i=0; i < siteforAdmin.length; i++){
				var presence=false;
				
				for ( var int = 0; int < dls.length; int++) {
					if(siteforAdmin[i].name==dls[int].site.name && dls[int].dateDeleted==null){
						presence=true;
					}
				}
				var label = document.createElement('label');
				var input = document.createElement('input');
				var br = document.createElement("br");
				input.id="site"+siteforAdmin[i].id;
				input.setAttribute("type", "checkbox");
				input.setAttribute("class", "site_group");
				input.setAttribute("value", siteforAdmin[i].id);
				label.innerHTML=siteforAdmin[i].name;
				if(presence==true){
					input.setAttribute("checked","true");
				}
				if(!siteforAdmin[i].isVerified){
					input.setAttribute("disabled","disabled");
					label.innerHTML=siteforAdmin[i].name+"  <i>*Not verified</i>";
				}
				document.getElementById("site_list").appendChild(input);
				label.setAttribute("for", "site"+siteforAdmin[i].id);
				document.getElementById("site_list").appendChild(label);
				document.getElementById("site_list").appendChild(br);
			}
			}
		});
		}
	});	
	}
}

function newsitelist(currentUser){
	SiteManager.getSitesforPerson(currentUser, function(siteforAdmin){
		for(var i=0; i < siteforAdmin.length; i++){
			var label = document.createElement('label');
			var input = document.createElement('input');
			var br = document.createElement("br");
			input.id="site"+siteforAdmin[i].id;
			input.setAttribute("type", "checkbox");
			input.setAttribute("class", "site_group");
			input.setAttribute("value", siteforAdmin[i].id);
			document.getElementById("site_list").appendChild(input);
			label.setAttribute("for", "site"+siteforAdmin[i].id);
			label.innerHTML=siteforAdmin[i].name;
			if(!siteforAdmin[i].isVerified){
				input.setAttribute("disabled","disabled");
				label.innerHTML=siteforAdmin[i].name+"  <i>*Not verified</i>";
			}
			document.getElementById("site_list").appendChild(label);
			document.getElementById("site_list").appendChild(br);

		}
	});
}

function allowaccess(currentUser, dataSourceId){
	
	var isAdmin = false;
	
	DataSourceManager.getDataSourceById(dataSourceId,function(datasource){
	DataSourceManager.getAdminsForDataSource(datasource,function(admins){
		if(admins.length==0){
			//alert("Here we are");
			isAdmin=true;
		}
	for (var i=0; i < admins.length; i++) {
		admin=admins[i];
		if(admin.email==currentUser){
			isAdmin=true;
		}
	}
	if(isAdmin){
		document.getElementById("addowner").style.display="";
	}else{
		alert("You are not allowed here!");
		window.location="account.jsp";
	}
	});
	});
}

/*function allowaccess(currentUser, dataSourceId){
	var isAdmin = ownerOrHost(currentUser, dataSourceId);
	
	if(!isAdmin){
		alert("You are not allowed here!");
		//window.location="account.jsp";
	}
}*/


function set(select) {
	  var wert = select.options[select.options.selectedIndex].value;
	  document.getElementById('codeCat').value=wert;
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

function addDataOwner(dataId) {
	// modified code from http://www.simonbingham.me.uk/index.cfm/main/post/uuid/adding-a-row-to-a-table-containing-form-fields-using-jquery-18
	var userEmail = $("#userEmail").val();
	var newEmails = $("#coOwnerEmail").val().split(/[,\s;]+/);
	var emailList = $("#siteOwnersTable").find("tr label[name='email']");
	var newadmins = new Array();

	if (newEmails != null && newEmails.length>0) {
		for(var i = 0; i < newEmails.length; i++) {
			var newEmail = newEmails[i];
			
			var alreadyInList = false;
			for(var j = 0; j < emailList.length; j++) {
				if ($(emailList[j]).text() == newEmail) {
					alreadyInList = true;
					$("#emailMsg").text("(" + newEmail + ") is already a co-owner.");
					$("#emailMsg").show();
					break;
				}
			}
			if (!alreadyInList) {
				newadmins.push(newEmail);
			}
		}
		DataSourceManager.addNewDataOwner(dataId,userEmail,newadmins,{
			callback : function(successful) {
				
			}
		});
		$("#coOwnerEmail").val("");
	}
}

function popup(url, name, width, height) {
	settings=
		"toolbar=no,location=yes,directories=yes,"+
		"status=no,menubar=no,scrollbars=yes,"+
		"resizable=yes,width="+width+",height="+height;
	MyNewWindow=window.open(url, name, settings);
}

function validateForm() {
	var valid= true;
	//for browser backward compatibility
	var dsFullName = dwr.util.getValue("fullname");
	var dsAbbrev = dwr.util.getValue("abbrev");
	var dsURL = dwr.util.getValue("url");
	var dsRights = dwr.util.getValue("rights");
	var dsDescription = dwr.util.getValue("description");
	var subjects = [];
	var theSel = document.getElementById("selectionFOR");	
	for (var i=0; i < theSel.length; i++) {		
		subjects.push({code: theSel.options[i].text, 
			type : 'anzsrc_for', id : theSel.options[i].value});		
	}	
	theSel = document.getElementById('selectionSEO');
	for (var i=0; i < theSel.length; i++) {	
		subjects.push({ code: theSel.options[i].text, 
			type : 'anzsrc_seo', id : theSel.options[i].value});		
	}
	
	if(dsFullName==null || dsFullName==""){
		$("#fullname").addClass("error");
		$("#urlChoiceLBL").addClass("error");
		$("#urlBottomLBL").addClass("error");
		valid=false;
	}else{
		$("#url").removeClass("error");
		$("#urlChoiceLBL").removeClass("error");
		$("#urlBottomLBL").removeClass("error");
	}
	if(dsURL==null || dsURL==""){					
		$("#url").addClass("error");
		$("#urlLBL").addClass("error");
		valid=false;
	}else{
		$("#url").removeClass("error");
		$("#urlLBL").removeClass("error");	
	}
	if(dsAbbrev==null || dsAbbrev==""){					
		$("#abbrev").addClass("error");
		$("#abbrevLBL").addClass("error");
		valid=false;
	}else{
		$("#abbrev").removeClass("error");
		$("#abbrevLBL").removeClass("error");	
	}
	if(dsRights==null || dsRights==""){					
		$("#right").addClass("error");
		$("#rightLBL").addClass("error");
		valid=false;
	}else{
		$("#right").removeClass("error");
		$("#rightLBL").removeClass("error");	
	}
	if(dsDescription==null || dsDescription==""){					
		$("#description").addClass("error");
		$("#descriptionLBL").addClass("error");
		valid=false;
	}else{
		$("#description").removeClass("error");
		$("#descriptionLBL").removeClass("error");	
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
	return valid;
}

function saveChanges(currentUser,datasourceId){
	if (!validateForm()) {	
		$("#msg").text("Save action FAILED - Missing Required Values");
		$("#msg").removeClass("confirmMsg");
		$("#msg").addClass("errMsg");
		$("#msg").show();
		window.scrollTo(0,0);
		return false;
	}
	var dsFullName = dwr.util.getValue("fullname");
	var dsAbbrev = dwr.util.getValue("abbrev");
	var dsURL = dwr.util.getValue("url");
	var dsRights = dwr.util.getValue("rights");
	var dsDescription = dwr.util.getValue("description");
	var allcheckedsites = [];
	var alluncheckedsites = [];
	var allsites = $("#site_list input:checkbox");
	allsites.each(function(){
		if($(this)[0].checked){
			allcheckedsites.push($(this).val());
		}else{
			alluncheckedsites.push($(this).val());
		}
	});

		
	var theSel = document.getElementById("selectionFOR");	
	var dsSubjects = [];
	for (var i=0; i < theSel.length; i++) {
		dsSubjects.push({code: theSel.options[i].text, 
			type : 'anzsrc_for', id : theSel.options[i].value});
	}
	
	theSel = document.getElementById("selectionSEO");
	for (var i=0; i < theSel.length; i++) {
		dsSubjects.push({ code: theSel.options[i].text, 
			type : 'anzsrc_seo', id : theSel.options[i].value});
	}
	
	DataSourceManager.saveOrUpdate(datasourceId,
			dsFullName, dsAbbrev, dsURL, dsRights, 
			dsDescription,currentUser, dsSubjects, allcheckedsites,alluncheckedsites, {
		callback:function(status) {
			if (status=="Done") {
				$("#msg").text("Save Complete");
				$("#msg").removeClass("errMsg");
				$("#msg").addClass("confirmMsg");
				$("#msg").show();
			} else {
				$("#msg").text("Save FAILED: "+status);
				$("#msg").removeClass("confirmMsg");
				$("#msg").addClass("errMsg");
				$("#msg").show();
			}
		}
	});
}

function turnOnReadOnly(hideLoad) { 
	$('form :input:not(:button):not(:submit)').attr('readonly',true);
	$('form select').attr('readonly',true);
	$('form textarea').attr('readonly',true);
	$('#coOwnerEmail').attr("readonly", false);
	
	$('#addSiteRow').hide();
	$('.addSubjectCodeRow').hide();
	$('#deleteFor').hide();
	$('#deleteSeo').hide();
	$('#urlChoiceLBL').hide();
	if (hideLoad) {
		$('#urlSelect').hide();
		$('#loadDataSourceBtn').hide();
		$('#urlTopLBL').hide();
		$('#urlBottomLBL').show();
		
	}
}

function turnOnEditData() { 
	$('form input').attr('readonly', false);
	$('form select').attr('readonly', false);
	$('form textarea').attr('readonly', false);
	$('#url').attr('readonly', true);
	$('#urlSelect').hide();
	$('#loadDataSourceBtn').hide();
	$('#addSiteRow').hide();
	$('#urlTopLBL').hide();
	$('#urlBottomLBL').show();
	$('#urlChoiceLBL').hide();
	
}

function turnOnNewData(currentUser) {
	$('form input').attr('readonly', false);
	$('form select').attr('readonly', false);
	$('form textarea').attr('readonly', false);
	$('#addSiteBtn').on('click', $('#site')[0], addSite);
	$('#addSiteRow').show();
	$('#urlTopLBL').show();
	$('#urlBottomLBL').show();

	SiteManager.getAllSites(function(sites) {
		dwr.util.removeAllOptions("site");
		dwr.util.addOptions("site", ["<Select a Site>"]);
		dwr.util.addOptions("site", sites, "id", "name");
	});
}

function getRemainingDataSources(currentUser,id){
DataSourceManager.getRemainingDataSources(currentUser, function(dataSource) {
	dwr.util.removeAllOptions("urlSelect");
	//dwr.util.addOptions("urlSelect", ["<Available Datasets>"]);
	var emptyData = new Object();
	emptyData.url="<New Dataset>";
	emptyData.id=0;
	var newlist = new Array();
	newlist.push(emptyData);
	dwr.util.addOptions("urlSelect", newlist, "id", "url");
	dwr.util.addOptions("urlSelect", dataSource, "id", "url");
	$('.selDIV option[value="'+id+'"]').prop('selected', true);
});
}

function getDataSourceId(){
	var e = document.getElementById("urlSelect");
	var dataId = e.options[e.selectedIndex].value;
	location.href='data.jsp?type=add&id='+dataId;
}

function loadDataSource(currentUser,dataId) {
	populateDataSource(currentUser, dataId);
	turnOnReadOnly(false);
}

function addSite(){
		dwr.util.setValue("sname", "");
		dwr.util.setValue("sdescription", "");
		dwr.util.setValue("surl", "");
		dwr.util.setValue("srights", "");
		document.getElementById('blockUI').style.display = '';
		document.getElementById('siteForm').style.display='';
		$("#siteMsg").hide();
	}
	
