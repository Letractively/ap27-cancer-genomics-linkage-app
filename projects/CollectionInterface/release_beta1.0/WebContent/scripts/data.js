function populateDataSource(currentUser, dataLocationId) {	
	DataLocationManager.getLocationById(dataLocationId,{
		callback : function(dataLoc) {
			if (dataLoc==null) { 
				alert("This data record does not exist");
				window.location="account.jsp";
			}
			if (currentUser != dataLoc.person.email) {
				alert("You do not have access to this record");
				window.history.back(-1);
			}
			dwr.util.setValue("fullname", dataLoc.dataSource.name);
			dwr.util.setValue("abbrev", dataLoc.dataSource.abbrev);
			dwr.util.setValue("url", dataLoc.dataSource.url);
			dwr.util.setValue("rights", dataLoc.dataSource.rights);
			dwr.util.setValue("description", dataLoc.dataSource.description);

			//load subject codes
			dwr.util.removeAllOptions("selectionFOR");
			dwr.util.removeAllOptions("selectionSEO");
			for (var i=0; i < dataLoc.dataSource.subjects.length; i++) {
				if (dataLoc.dataSource.subjects[i].type == "anzsrc_for") {
					dwr.util.addOptions("selectionFOR", [dataLoc.dataSource.subjects[i]], "code");
				} else {
					dwr.util.addOptions("selectionSEO", [dataLoc.dataSource.subjects[i]], "code");
				}
			}
			
			DataSourceManager.getSupportedSites(dataLoc.dataSource, function(supportedSites) {
				SiteManager.getRemainingSites(supportedSites, function(sites) {
					dwr.util.removeAllOptions("site");
					dwr.util.addOptions("site", ["<Select a Site>"]);
					dwr.util.addOptions("site", sites, "id", "name");
				});
			});
		}
	});	
}

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

function addEmail() {
	// modified code from http://www.simonbingham.me.uk/index.cfm/main/post/uuid/adding-a-row-to-a-table-containing-form-fields-using-jquery-18
	
	var newEmails = $("#coOwnerEmail").val().split(/[,\s;]+/);
	var emailList = $("#siteOwnersTable").find("tr label[name='email']");

	if (newEmails != null) {
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
				var newRow = $("#siteOwnersTable").find("tr:last").clone();
				newRow.find("label").text(newEmail);
				newRow.find("label").attr("value", newEmail);
				newRow.find("input:checkbox").attr("checked", false);
				newRow.find("input:checkbox").attr("value", function() {
					var tokens = $(this).val().match(/(-*\d+):(-*.+)_(\d+)$/);
					return "-1:"+ newEmail + "_" + tokens[3];
				});
				
				// append new row
				$("#siteOwnersTable").find("tr:last").after(newRow);
			}
		}
		$("#coOwnerEmail").val("");
	}
}

function addSite() {  
	var newSite = $('#site :selected').text();
	var newSiteID = $('#site :selected').val();
	
	if (newSiteID > 0) {
		$("#siteOwnersTable").find("th:last").after("<th>" + newSite + "</th>");	
		$("#siteOwnersTable").find("tr").each(function() {
			var personID = $(this).find("label").attr("value");
			$(this).find("td:last-child").after("<td><input type='checkbox' value='-1:" 
					+ personID + '_' + newSiteID + "'/></td>");
		});
		$('#site option:selected').remove();
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
	//for browser backward compatibility
	var supportedSites = $('#siteOwnersTable').find("tr:eq(0) label");
	if (supportedSites == null) {
		alert("Please add a supported Site");
		$("#sites").focus();
		return false;
	}
	return true;
}

function save(dataLocId, currentUser){
	if (!validateForm()) {
		return false;
	}
	
	var dsFullName = dwr.util.getValue("fullname");
	var dsAbbrev = dwr.util.getValue("abbrev");
	var dsURL = dwr.util.getValue("url");
	var dsRights = dwr.util.getValue("rights");
	var dsDescription = dwr.util.getValue("description");
	
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
	var siteOwners = [];
	var checkedSites = $("#siteOwnersTable input:checkbox");
	if (checkedSites == null) {
		alert("Data is not hosted by any sites or\n there are no associated owners.");
	} else {
		checkedSites.each(function() {
			var tokens = $(this).val().match(/(-*\d+):(-*.+)_(\d+)$/);
			if (tokens[1] != "-1" && !($(this)[0].checked)) {
				siteOwners.push({dataLocID: tokens[1], personID: tokens[2], 
					siteID: tokens[3], isOwner: false});
			} else if (tokens[1] == "-1" && $(this)[0].checked){
				siteOwners.push({dataLocID: tokens[1], personID: tokens[2], 
					siteID: tokens[3], isOwner: true});
			}
		});
	}
	
	DataLocationManager.saveOrUpdate(dataLocId, currentUser, 
			dsFullName, dsAbbrev, dsURL, dsRights, 
			dsDescription, dsSubjects, siteOwners);
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
}

function turnOnNewData(currentUser) {
	$('form input').attr('readonly', false);
	$('form select').attr('readonly', false);
	$('form textarea').attr('readonly', false);
	$('#addSiteBtn').on('click', $('#site')[0], addSite);
	$('#addSiteRow').show();
	$('#urlTopLBL').show();
	$('#urlBottomLBL').hide();
	
	DataSourceManager.getRemainingDataSources(currentUser, function(dataSource) {
		dwr.util.removeAllOptions("urlSelect");
		dwr.util.addOptions("urlSelect", ["<Available Datasets>"]);
		dwr.util.addOptions("urlSelect", dataSource, "id", "url");
	});

	SiteManager.getAllSites(function(sites) {
		dwr.util.removeAllOptions("site");
		dwr.util.addOptions("site", ["<Select a Site>"]);
		dwr.util.addOptions("site", sites, "id", "name");
	});
}

function loadDataSource() {
	$('#url').val($('#urlSelect option:selected').text());
	DataSourceManager.getDataSourceByURL($('#urlSelect option:selected').text(), {
		callback: function(ds) {
			dwr.util.setValue("fullname", ds.name);
			dwr.util.setValue("abbrev", ds.abbrev);
			dwr.util.setValue("url", ds.url);
			dwr.util.setValue("rights", ds.rights);
			dwr.util.setValue("description", ds.description);
			
			//load subject codes
			dwr.util.removeAllOptions("selectionFOR");
			dwr.util.removeAllOptions("selectionSEO");
			for (var i=0; i < ds.subjects.length; i++) {
				if (ds.subjects[i].type == "anzsrc_for") {
					dwr.util.addOptions("selectionFOR", [ds.subjects[i]], "code");
				} else {
					dwr.util.addOptions("selectionSEO", [ds.subjects[i]], "code");
				}
			}
			
			DataSourceManager.getSupportedSites(ds, function(supportedSites) {
				SiteManager.getRemainingSites(supportedSites, function(sites) {
					dwr.util.removeAllOptions("site");
					dwr.util.addOptions("site", ["<Select a Site>"]);
					dwr.util.addOptions("site", sites, "id", "name");
				});
			});
			
			turnOnReadOnly(false);
		}
	});
}