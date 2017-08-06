var logEventEnvoker = [
	{   "name":"getRestrictions",
		"has_callback" : true,
	    "arg_names" : []
	},
];

var logEventHandler = function(instance){
	this.derp = function(a){
		console.log(a);
	}
}

var socketAPILogEventInstanceProvider = new SocketAPIScopeInstanceProvider("LOG_EVENT", logEventEnvoker, logEventHandler);

socketAPIScopeFactoryPrimary.addProvider(socketAPILogEventInstanceProvider);

var scopes = ['BASIC_ERROR', 'FLAG_COLOR', 'LOG_EVENT']
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
}