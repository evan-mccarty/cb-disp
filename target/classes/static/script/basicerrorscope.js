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