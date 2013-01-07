

function populatePage(username) {
	PersonManager.getPersonByEmail(username,
			{
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



function publishrecords(datalocationId){
	DataLocationManager.getLocationById(datalocationId, {callback:function(datalocation){	
		DataLocation.publishAssociatedDataSourcetoRDA(datalocation);
		var button = document.getElementById("publishrecords"+datalocationId);
		button.disabled=1;
		button.style.opacity=0.4;
		button.style.filter="filter: alpha(opacity=40)";
	}
	});
	}

function publishpartofdl(datalocationId){
	DataLocationManager.getLocationById(datalocationId, {callback:function(datalocation){
		DataLocation.publishAndOwn(datalocation);
		var button = document.getElementById("publishpart"+datalocationId);
		button.disabled=1;
		button.style.opacity=0.4;
		button.style.filter="filter: alpha(opacity=40)";

	}
	});
}

function publishsite(datalocationId){
	DataLocationManager.getLocationById(datalocationId, {callback:function(datalocation){	
		DataLocation.publishSite(datalocation);
		var button = document.getElementById("publishSite"+datalocationId);
		button.disabled=1;
		button.style.opacity=0.4;
		button.style.filter="filter: alpha(opacity=40)";
		}
	});
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

function stopHosting(dataLocId){
	//TODO: confirm delete
	DataLocationManager.stopHosting(dataLocId, {callback:function(finished){
		document.location.reload();
	}
	});
}

function stopOwning(dataLocId){
	//TODO: confirm delete
	DataLocationManager.stopOwning(dataLocId, {callback:function(finished){
	document.location.reload();
}
});
}