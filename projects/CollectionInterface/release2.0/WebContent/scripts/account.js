$(document).ready(function() {
	$("#adminsitesBlock").hide();
	$("#datasitesBlock").hide();
	$("#blockUI").hide();
	$("#adminSitesHelp").show();
	$('#personemail').attr("readonly", true);
	$("#msg").hide();
	
});

function populatePage(username) {
	
	PersonManager.getPersonByEmail(username, {
		callback : function(pers) {
			person = pers;
			dwr.util.setValue("firstName", person.firstname);
			dwr.util.setValue("lastName", person.lastname);
			dwr.util.setValue("email", person.email);
			dwr.util.setValue("userTitle", '');
			
			var tdusertitle = document.getElementById("userTitle");
			var selectbox = document.createElement("select");
			selectbox.id="selectuserTitle";
			tdusertitle.appendChild(selectbox);
			titles =["Miss","Mr","Mrs","Dr"];
			    for (var i=0; i<titles.length; i++) {
			      		addOption(selectbox, i, titles[i],person.title);
			     }
			dwr.util.setValue("listIntits", '');
			var tdlistinstits = document.getElementById("listIntits");
			var institselectbox = document.createElement("select");
			
			institselectbox.id="selectlistIntits";
			tdlistinstits.appendChild(institselectbox);
			InstitutionManager.getAllInstitutions({callback:function(institutes) {
				institutions ={};
				institutions = institutes;
			     for (var i=0; i<institutions.length; i++) {
			      		addOption(institselectbox, institutions[i].id, institutions[i].name,"");
			     }
				}
			    });
			
			var selectedInstitsbox = document.getElementById("selectionInstits");
				institutions ={};
				institutions = person.institutions;
			     for (var i=0; i<institutions.length; i++) {
			      		addOption(selectedInstitsbox, institutions[i].id, institutions[i].name,"");
			     }
				}
			});
		}

function populateusertable(siteId){
	$("#adminsitesBlock").show();
	$("#adminSitesHelp").hide();
	SiteManager.getSiteById(siteId, function(site){
		var label = document.getElementById('userTitle1');
		var label2 = document.getElementById('userTitle2');
		label.innerHTML=site.name;
		label2.innerHTML=site.name;
		label2.setAttribute("siteId",siteId);
		var users= site.siteAdmins;
		var cellFuncs = [
		                 function(data) { return data.title+" "+data.firstname+" "+data.lastname; },
		                 function(data) { return data.email; },
		                 function(data) { return "<input type='button' style='background: url(../../images/x_mark_red.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' onclick='removeuser("+data.id+","+siteId+")'/>"; },
		               ];	
		dwr.util.removeAllRows("usersbody");
		dwr.util.addRows( "usersbody", users, cellFuncs, { escapeHtml:false });
		$("#siteName").text(site.name);
		$("#siteDesc").text(site.description);
		$("#siteRights").text(site.rights);
		$("#siteURL").text(site.url);
		$("#siteInstit").text(site.institution.name);
		if(site.isVerified){
			$("#siteVerif").text("Verified");
		}else{
			$("#siteVerif").text("Non Verified");
		}
	});

}

function populatesitetable(dataSourceId){
	$("#datasitesBlock").show();
	DataSourceManager.getDataSourceById(dataSourceId, function(ds){
		var label = document.getElementById('sitesTitle');
		label.innerHTML=ds.name;
		var dls= ds.dataLocations;
		var dummydls = generatedummydls(dls);
		dwr.util.removeAllRows("sitesbody");
		var cellFuncs = [
		                 function(data) { return data.name;},
		                 function(data) { return data.institution;},
		                 function(data) { return data.url;},
		                 function(data) { return data.buttons;},
		               ];	
		
		dwr.util.addRows( "sitesbody", dummydls, cellFuncs, { escapeHtml:false });
	});
	
		
}

function generatedummydls(dls){
	var list = new Array();
	
	for(var i=0;i<dls.length;i++){
		var name = dls[i].site.name;
		var institution= dls[i].site.institution.name;
		var url=dls[i].site.url;
		var buttons="";
		var username = document.getElementById("userEmail").value;
     	PersonManager.getPersonByEmail(username, {
     		async:false,
     		callback:function(pers){
		var username = document.getElementById("userEmail").value;
 		var usersites=pers.adminSites;
 		var presence = false;
 		for(var j=0;j<usersites.length;j++){
 			 if(name==usersites[j].name){
 				 presence =true;
 			 }
 		 }
 		if(presence){
 			if(dls[i].dateDeleted!=null){	
 				buttons ="<input type='button' title='Undo' style='background: url(../../images/undo.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' onclick='undo("+dls[i].id+")'/>";
 				buttons =buttons+"<label>* <b>"+dls[i].dataSource.name+"</b> is now inactive for <b>"+name+"</b></label>"; 
 			}else{
 			if(dls[i].isDlPublished){
 				buttons ="<input type='button' title='Unpublish from RDA' style='background: url(../../images/unpublish.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' onclick='unpublishrecords("+dls[i].id+")'/>";
 	 			buttons=buttons+"<input type='button' title='Unpublish and Remove' style='background: url(../../images/x_mark_red.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' onclick='unpubblishandremove("+dls[i].id+")'/>";
 			}else{
 				if(dls[i].site.isVerified){
 			 buttons="<input type='button' id='publishrecords"+dls[i].dataSource.id+"' style='background: url(../../images/Publish.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' title='Publish to RDA' onclick='publishrecords("+dls[i]+")' ></input>";
 				}else{
 			 buttons="<input disabled type='button' style='background: url(../../images/Publishbw.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' title='Site needs to be verified' ></input>";	
 				}
 			 buttons =buttons+"<input type='button' onclick='removesite("+dls[i].id+")' style='background: url(../../images/x_mark_red.png) no-repeat;background-size:28px 28px; height: 32px; width: 32px;' title='Remove Site for this Data Source'/>";

 			}
 			}
 		 }else{
 			 buttons ="<input type='button' value='Remove' onclick='remove("+dls[i].id+")' disabled/>";
 			buttons=buttons+"<label>*You are not admin of this Site</label>";
 		 }
 		
     	dl=new Object();
     	dl.name=name;
     	dl.institution=institution;
     	dl.url=url;
     	dl.buttons=buttons;
     	list.push(dl);
     		}
 		});
	
		
	}
	return list;
	}

function publishrecords(dataLocationId){
	
	$("#blockUI").show();
	setTimeout('$("#blockUI").hide()',5000);
	
	DataLocationManager.publishDataLocation(dataLocationId,function(datalocation){
		populatesitetable(datalocation.dataSource.id);
	});
	
}

function unpublishrecords(dataLocationId){
	
	$("#blockUI").show();
	setTimeout('$("#blockUI").hide()',5000);
	
	DataLocationManager.unPublishDataLocation(dataLocationId,function(datalocation){
		populatesitetable(datalocation.dataSource.id);
	});
	
}

function unpubblishandremove(dataLocationId){
	$("#blockUI").show();
	setTimeout('$("#blockUI").hide()',5000);
	DataLocationManager.removePublishedSite(dataLocationId,function(datalocation){
		populatesitetable(datalocation.dataSource.id);
	});
}

function removesite(dataLocationId){

	DataLocationManager.removesite(dataLocationId,function(datalocation){
		populatesitetable(datalocation.dataSource.id);
	});
}

function undo(dataLocationId){

	DataLocationManager.undoChange(dataLocationId,function(datalocation){
		populatesitetable(datalocation.dataSource.id);
	});
}

function removeuser(userId,siteId){
	SiteManager.getSiteById(siteId, function(site){
		var admins = site.siteAdmins;
		for ( var int = 0; int < admins.length; int++) {
			if(admins[int].id==userId){
				admins.splice(int,1);
			}
		}
		SiteManager.saveOrUpdate(siteId,admins,{
			callback:function(successful) {
				populateusertable(siteId);
			}	
		});
	});
}

function addAdmin() {
	// modified code from http://www.simonbingham.me.uk/index.cfm/main/post/uuid/adding-a-row-to-a-table-containing-form-fields-using-jquery-18
	var userEmail = $("#userEmail").val();
	var newEmails = $("#adminSiteEmail").val().split(/[,\s;]+/);
	var emailList = $("#users").find("tr label[name='email']");
	var siteId = document.getElementById("userTitle2").getAttribute("siteid");
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
		SiteManager.addNewAdmins(userEmail,siteId,newadmins,{
			callback : function(successful) {
				
			}
		});
		$("#adminSiteEmail").val("");
	}
}

function addInstits(){	
	var e = document.getElementById("selectlistIntits");
	var value = e.options[e.selectedIndex].value;
	var text = e.options[e.selectedIndex].text;
	 theSel= document.getElementById('selectionInstits');
	 newText =text;
	 newValue =value;
if (theSel.length == 0) {
  var newOpt1 = new Option(newText, newValue);
  theSel.options[0] = newOpt1;
  theSel.selectedIndex = 0;
} else{
	var absence =true;
	for(var j=0; j<theSel.length;j++){
		if(theSel.options[j].text == newText){
			alert("This User is already part of "+newText);
			absence=false;
		}
	}
	if(absence==true){
    var lgth = theSel.length;
    var newOpt = new Option(newText, newValue);
    theSel.options[lgth] = newOpt;
	}
  }
}

function removeSelection(){
	var theSel= document.getElementById('selectionInstits');
	var selIndex = theSel.selectedIndex;
	  if (selIndex != -1) {
	    for(i=theSel.length-1; i>=0; i--) {
	      if(theSel.options[i].selected) {
	        theSel.options[i] = null;
	      }
	    }
	    if (theSel.length > 0) {
	      theSel.selectedIndex = selIndex == 0 ? 0 : selIndex - 1;
	    }
	  }
}

function saveDetails(){
		var firstname = dwr.util.getValue("firstName");
		var lastname = dwr.util.getValue("lastName");
		var email = dwr.util.getValue("email");
		var e = document.getElementById("selectuserTitle");
		var title = e.options[e.selectedIndex].text;
		theSel= document.getElementById('selectionInstits');
		institutes=[];
		for(var i=0; i<theSel.length; i++) {
			institutes.push(theSel.options[i].value);
		}
		PersonManager.saveOrUpdate(firstname,lastname,email,title,institutes);
}

//Similar to sentVerificationAgain  in workflow.js
function resendsiteVerification(currentUser,siteId){
	EmailManager.sendSiteVerificationEmailAgain(currentUser, siteId);	
	$("#msg").text("The site has been sent for verification again. Please note site verifications can take up to 24hrs.");
	$("#msg").removeClass("errMsg");
	$("#msg").addClass("confirmMsg");
	$("#msg").show();
	$("#sendverif"+siteId).attr("disabled","disabled");
	$("#sendverif"+siteId).css("background","url(../../images/sendverifbw.png) no-repeat");
}

function stopadmin(ownedId){
	DataOwnerManager.stopAdmin(ownedId, function(dataO){
		location.reload();
/*		var current_index = $("#tabs").tabs("option","selected");
		$("#tabs").tabs('load',current_index);*/
	});
}

function stopadminandremove(ownedId){
	DataOwnerManager.stopAdminAndRemove(ownedId, function(success){	
		location.reload();
/*		var current_index = $("#tabs").tabs("option","selected");
		$("#tabs").tabs('load',current_index);*/
		
	});
}
		
function publishwf(workflowId){
	window.location="workflow.jsp?id="+workflowId+"&type=publish";	
}

//Add option   
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

function matchPasswords() {
	$(".errMsg").hide();
	if ($("#confirmPassword").val() != $("#newPassword").val()) {
		$("#pwdMatchErr").show();
	} else {
		$(".errMsg").hide();
	}
}

function resetPassword(username) {
	if ($("#confirmPassword").val() == $("#newPassword").val()) {
		PersonManager.changePassword(username, $("#currentPassword").val(), $("#newPassword").val(),{
			callback : function(successful) {
				if (successful) {
					 $(".errMsg").hide();
					 $("#pwdResetMsg").show();
				 } else {
					 $("#pwdResetErr").show();
				 }				
			}
		});
	}
}

function stopHostingWf(workflowId){
	WorkflowManager.stopHosting(workflowId, {callback:function(finished){
		document.location.reload();
	}
	});
	
}