function init(){
	$("#msg").hide();

}

function sendToOwner(workflowId) {
		valid = true
		var firstname = dwr.util.getValue("first_name");
		var lastname = dwr.util.getValue("last_name");
		var email = dwr.util.getValue("email");
		var telephone = dwr.util.getValue("telephone");
		var comments = dwr.util.getValue("comments");
		
		firstname = firstname.replace(/^\s+|\s+$/g,"");
		lastname = lastname.replace(/^\s+|\s+$/g,"");
		email = email.replace(/^\s+|\s+$/g,"");
		comments = comments.replace(/^\s+|\s+$/g,"");
		if(firstname==null || firstname==""){					
			$("#first_name").addClass("error");
			$("#first_name_label").addClass("error");
			valid=false;
		}
		if(lastname==null || lastname==""){					
			$("#last_name").addClass("error");
			$("#last_name_label").addClass("error");
			valid=false;
		}
		if(email==null || email==""){					
			$("#email").addClass("error");
			$("#email_label").addClass("error");
			valid=false;
		}
		if(comments==null || comments==""){			
			$("#comments_label").addClass("error");
			valid=false;
		}
		if(valid){		
			EmailManager.sendRequestToWFOwner(workflowId,firstname, lastname, email, telephone, comments, {
						callback : function(check) {
							alert("Your email has been sent to the owner.");
							window.history.back();
						}
					});
		}else{
			$("#msg").text("Sending Email FAILED - Missing Required Values");			
			$("#msg").removeClass("confirmMsg");
			$("#msg").addClass("errMsg");
			$("#msg").show();
			window.scrollTo(0,0);
		}

	}

function getParam(name) {
		name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
		var regexS = "[\\?&]" + name + "=([^&#]*)";
		var regex = new RegExp(regexS);
		var results = regex.exec(window.location.href);
		if (results == null)
			return "";
		else
			return results[1];
}

