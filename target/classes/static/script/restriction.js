var restrictionsEnvoker = [
	{   "name":"getRestrictions",
		"has_callback" : true,
	    "arg_names" : []
	},
	{	"name":"saveRestrictions",
		"has_callback" : false,
		"arg_names" : ["java.util.HashMap"]
	},
	{
		"name":"getRestrictionStatesForDay",
		"has_callback" : true,
		"arg_names" : []
	},
];
var restrictionsHandler = function(instance){
	this.restrictionUpdate = function(event){
		if(isRestrictionValueValid(event.argument)){
			updateRestrictionValues(event.argument);
		}else{
			displayRestrictions(event);
		}
	}
	this.restrictionStateHistoryUpdate = function(event){
		clearDisplayRestrictionStateHistory();
		displayRestrictionStateHistory(event);
	}
}
var socketAPIRestrictionsInstanceProvider = new SocketAPIScopeInstanceProvider("RESTRICTION", restrictionsEnvoker, restrictionsHandler);

var restrictionUpdateEvents = new Map();

function RestrictionUpdateEvent(restriction_id, status, is_custom, custom){
	this.restriction_id = restriction_id;
	this.status = status;
	this.is_custom = is_custom;
	this.custom = custom;
}
	
socketAPIScopeFactoryPrimary.addProvider(socketAPIRestrictionsInstanceProvider);

var scopes = ['BASIC_ERROR', 'FLAG_COLOR', 'RESTRICTION']
getSocketInstance(function (json){
	handleResult(json);
	j = json;
}, scopes);
var socket;
function handleResult(session){
	var socketAPIScopeSession = new SocketAPIScopeSession();
	
	session.onerror = function(error){
		console.log(error);
	};
	session.onclose = function(error){
   		console.log(error);
	};
	session.onopen = function(error){
    	handleOpen(error);
    }
	//TODO export these functions to external js file
	//var socketAPIBasicErrorInstance = socketAPIBasicErrorInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIBasicErrorInstance);
	
	//var socketAPIFlagColorInstance = socketAPIFlagColorInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIFlagColorInstance);
	
	//var socketAPIRestrictionsInstance = socketAPIRestrictionsInstanceProvider.getInstance(socketAPIScopeSession);
	//socketAPIScopeSession.registerScope(socketAPIRestrictionsInstance);
	
	socketAPIScopeFactoryPrimary.addScopesToInstance(socketAPIScopeSession, scopes);
	
	socketAPIScopeSession.acceptSession(session);
	socket = socketAPIScopeSession;
}
function handleOpen(error){
	socket.scopes.FLAG_COLOR.envoker.getFlagColor(updateFlagColor);
	socket.scopes.RESTRICTION.envoker.getRestrictions(displayRestrictions);
	socket.scopes.RESTRICTION.envoker.getRestrictionStatesForDay(displayRestrictionStateHistory);
}
function clearDisplayRestrictionStateHistory(){
	$(".restriction_state_history_clear").remove();
}
function opemCustomText(button){
	var restriction_id = $(button.target).parent().data("restriction-id");
	var restriction_status = $(button.target).parent().data("restriction-status");
	setRestrictionUpdateEvent(restriction_status, $(button.target).parent(), true);
}
function displayRestrictionStateHistory(restrictionStates){
	var restrictionStatesArgument = restrictionStates.argument;
	console.log(restrictionStatesArgument);
	var tableLeft = $("#restrictionhistoryleft");
	var tableRight = $("#restrictionhistoryright");
	for(var i = 0; i < restrictionStatesArgument.length; i++){
		var restrictionStateArgument = restrictionStatesArgument[i];
		var tr = $("<tr>", {class : "restriction_state_history_clear"});
		if(restrictionStateArgument.is_ended){
			tableRight.append(tr);
		}else{
			tableLeft.append(tr);
		}
		var tdRight = $("<td>", {text : restrictionStateArgument.restriction.button_text});
		tr.append(tdRight);
		tdLeft = $("<td>", {text : restrictionStateArgument.start_time});
		tr.append(tdLeft);
		if(restrictionStateArgument.is_ended){
			tdLeft = $("<td>", {text : restrictionStateArgument.end_time});
			tr.append(tdLeft);
		}
	}
}
function displayRestrictions(restrictions){
	var restrictionTypes = restrictions.argument;
	restrictionTypes = sortRestrictionTypes(restrictionTypes);
	var perRow = 3;
	var perRowInner = 2;
	var table = $("<table>", {});
	table.empty();
	var tr;
	for(var i = 0; i < restrictionTypes.length; i++){
		var restrictionType = restrictionTypes[i];
		if(i % perRow == 0){
			tr = $("<tr>", {});
			table.append(tr);
		}
		var td = $("<td>", {class : "outterTable"});
		tr.append(td);
		var innerH3 = $("<h3>", {id : generateIdType(restrictionType)});
		td.append(innerH3);
		var innerTable = $("<table>", {});
		td.append(innerTable);
		var innerTr;
		var restrictions = restrictionType.restrictions;
		for(var i2 = 0; i2 < restrictions.length; i2++){
			var restriction = restrictions[i2];
			if(i2 % perRowInner == 0){
				innerTr = $("<tr>", {});
				innerTable.append(innerTr);
			}
			var innerTd = $("<td>", {class : "innerTable"});
			innerTr.append(innerTd);
			var innerDiv = $("<div>", {id : generateId(restriction)});
			innerDiv.data("restriction-id", restriction.id);
			innerTd.append(innerDiv);
			var button = $("<button>", {class : "restriction button-disabled"});
			button.click(handleRestrictionClick);
			innerDiv.append(button);
			if(restriction.class == "CUSTOM"){
				var button2 = $("<textarea>", {class : "customEdit", text : "", rows : "4", cols : "50"});
				button2.on('change keyup paste', opemCustomText);
				innerDiv.append(button2);
			}
		}
		if(i + 1 == restrictionTypes.length){
			createSaveButto(tr);
		}
	}
	$("#restrictions").append(table);
	updateRestrictionValues(restrictionTypes);
}
function createSaveButto(parentHolder){
	var tdeez = $("<td>", {});
	parentHolder.append(tdeez);
	var buttttoooon = $("<button>", {});
	buttttoooon.text("Save this");
	buttttoooon.click(handleRestrictionSave);
	tdeez.append(buttttoooon);
}
function setRestrictionUpdateEvent(state, restriction, update_custom){
	var restriction_id = $(restriction).data("restriction-id");
	var custom_text = $(restriction).find(".customEdit").val();
	console.log(custom_text);
	var restrictionUpdateEvent = restrictionUpdateEvents.get(restriction_id);
	if(!restrictionUpdateEvents.has(restriction_id)){
		restrictionUpdateEvents.set(restriction_id, new RestrictionUpdateEvent(restriction_id, state, update_custom, custom_text));
	}else if(restrictionUpdateEvent.status == state){
		if(update_custom){
			restrictionUpdateEvent.is_custom = true;
			restrictionUpdateEvent.custom = custom_text;
		}
		return;
	}else{
		if(restrictionUpdateEvent.is_custom){
			restrictionUpdateEvent.custom = custom_text;
			restrictionUpdateEvent.status = !restrictionUpdateEvent.status;
		}else{
			restrictionUpdateEvents.delete(restriction_id);
		}
	}
		
}
function isRestrictionValueValid(restrictionTypes){
	var outterTotal = restrictionTypes.length;
	var innerTotal = 0;
	for(var i = 0; i < restrictionTypes.length; i++){
		var restrictionType = restrictionTypes[i];
		if($("#" + generateIdType(restrictionType)).length == 0)
			return false;
		for(var i2 = 0; i2 < restrictionType.restrictions.length; i2++){
			var restriction = restrictionType.restrictions[i2];
			innerTotal++;
			var button = $("#" + generateId(restriction));
			if(button.length == 0)
				return false;
		}
	}
	return $(".innerTable").length == innerTotal 
		   && $(".outterTable").length == outterTotal;
}
function generateIdType(restrictionType){
	return "restriction_type_" + restrictionType.id;
}
function generateId(restriction){
	return "restriction_" + restriction.restriction_type_id + "_" + restriction.id + "_" + restriction.class;
}
function updateRestrictionValues(restrictionTypes){
	for(var i = 0; i < restrictionTypes.length; i++){
		var restrictionType = restrictionTypes[i];
		$("#" + generateIdType(restrictionType)).text(restrictionType.title);
		for(var i2 = 0; i2 < restrictionType.restrictions.length; i2++){
			var restriction = restrictionType.restrictions[i2];
			var div = $("#" + generateId(restriction));
			var button = div.find(".restriction");
			button.text(restriction.button_text);
			button.attr("title", restriction.display_text);
			if(restriction.class == "CUSTOM"){
				var button2 = div.find(".customEdit");
				button2.val(restriction.display_text)
			}
			setRestrictionState(button, getRestrictionState(restriction));
		}
	}
}
function handleRestrictionClick(button){
	var buttonState = $(button.target).parent().data("restrictionStatus");
	console.log(buttonState);
	setRestrictionState(button.target, !buttonState);
	setRestrictionUpdateEvent(!buttonState, $(button.target).parent(), false);
}
function setRestrictionState(restriction, state){
	$(restriction).parent().data("restriction-status", state);
	if(state){
		$(restriction).addClass("button-enabled").removeClass("button-disabled");
	}else{
		$(restriction).addClass("button-disabled").removeClass("button-enabled");
	}
	
}
function getRestrictionState(restriction){
	return restriction.restriction_state == undefined ? false : restriction.restriction_state.is_ended == false;
}
function sortRestrictionTypes(restrictionTypes){
	var sort = function(a, b){
		if(a.position < b.position)
			return -1;
		if(a.position > b.position)
			return 1;
		return 0;
	}
	restrictionTypes.sort(sort);
	for(var i = 0; i < restrictionTypes.length; i++){
		restrictionTypes[i].restrictions.sort(sort);
	}
	return restrictionTypes;
}
function handleRestrictionSave(restriction){
	socket.scopes["RESTRICTION"].envoker.saveRestrictions([...restrictionUpdateEvents]);
	restrictionUpdateEvents = new Map();
}