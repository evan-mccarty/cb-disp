var basicErrorEnvoker = [
	{   "name":"handleError",
		"has_callback" : false,
	    "arg_names" : ["java.lang.String"] 
	},
	{   "name":"getInt",
		"has_callback" : true,
	    "arg_names" : [] 
	}
];
var basicErrorHandler = function(instance){
	this.sendError = function(derp){
		console.log(derp);
	}
}
var socketAPIBasicErrorInstanceProvider = new SocketAPIScopeInstanceProvider("BASIC_ERROR", basicErrorEnvoker, basicErrorHandler);
var flagColorEnvoker = [
	{   "name":"getFlagColor",
		"has_callback" : true,
	    "arg_names" : []
	},
];
var flagColorHandler = function(instance){
	this.flagColorUpdate = function(a){
		updateFlagColor(a);
	}
}
var socketAPIFlagColorInstanceProvider = new SocketAPIScopeInstanceProvider("FLAG_COLOR", flagColorEnvoker, flagColorHandler);
function updateFlagColor(flagColor){
	var imgPath
	if(flagColor.argument == "GREEN")
		imgPath = "/img/green.png";
	else if(flagColor.argument == "YELLOW")
		imgPath = "/img/yellow.png";
	else if(flagColor.argument == "RED")
		imgPath = "/img/red.png";
	else
		imgPath = "/img/black.png";
	$("#cb_flag_img").attr("src", imgPath);
	console.log(flagColor.argument)
}

socketAPIScopeFactoryPrimary.addProvider(socketAPIBasicErrorInstanceProvider);
socketAPIScopeFactoryPrimary.addProvider(socketAPIFlagColorInstanceProvider);
