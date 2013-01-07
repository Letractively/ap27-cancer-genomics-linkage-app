function init() {
	var tdusertitle = document.getElementById("userTitle");
	var selectbox = document.createElement("select");
	selectbox.id="selectuserTitle";
	tdusertitle.appendChild(selectbox);
	//TODO: combine this method with accounts.js
	titles =["Miss","Mr","Mrs","Dr"];
	dwr.util.addOptions("selectuserTitle", titles);
	//for (var i=0; i<titles.length; i++) {
	//	addOption(selectbox, i, titles[i], "");
	//}
	
	var tdusertitle = document.getElementById("listInstits");
	var institselectbox = document.createElement("select");
	institselectbox.id="selectlistInstits";
	tdusertitle.appendChild(institselectbox);
	InstitutionManager.getAllInstitutions({
		callback:function(institutes) {
			institutions ={};
			institutions = institutes;
			dwr.util.addOptions("selectlistInstits", institutes, "id", "name");
			//for (var i=0; i<institutions.length; i++) {
	      	//	addOption(institselectbox, institutions[i].id, institutions[i].name, "");
			//}
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