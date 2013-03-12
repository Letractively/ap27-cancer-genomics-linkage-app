function populatePage(username) {
	dwr.util.setValue("email", username);
	//dwr.util.setValue("affiliation", '');	
	InstitutionManager.getAllInstitutions({
		callback:function(institutes) {
			dwr.util.removeAllOptions("selectaffiliation");
			dwr.util.addOptions("selectaffiliation", institutes, "id", "name");
		}
	 });
}

function addOption(selectbox, value, text, currentname, group) {
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
	
	
function saveSite(email, name, description, url, rights, affiliation) {
	var isAdmin = true;
	SiteManager.registerNewSite(	
					name, 
					description, 
					url,
					rights,					 
					affiliation, 
					email,
					isAdmin,{callback:function(value){						
						if(value){									
								document.getElementById('message').innerHTML = 'Your site has been sent for verification.';															
						}else{	
							document.getElementById('message').innerHTML = 'Your site is already verified.<br /><a href="javascript:history.back();">Go back.</a>';							
						}						
						
					}
					});	
	
	
	
}

function validateForm(name, description, url, rights){
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

	

