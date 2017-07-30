function getSocketInstance(callBack, scopes){
	//var webSocket = new WebSocket(
	fetchSocketAPIJson(function(json){
		var params = [];
		params.sessionId = json.sessionId;
		params.authToken = json.authToken;
		params.scopes = generateScopeParameter(scopes);
		var webSocket = new WebSocket(getSocketURL(json.socketPath, params));
		callBack(webSocket);
	});
}
function getSocketURL(path, params){
	return "ws://" + location.hostname + ":" + location.port + path + generateParams(params);
}
function generateParams(params){
	var paramString = "?";
	var keys = Object.keys(params);
	for(var i = 0; i < keys.length; i++){
		var key = keys[i];
		paramString += key;
		paramString += "=";
		paramString += params[key];
		if(i + 1 < keys.length)
			paramString += "&";
	}
	return paramString;
}
function fetchSocketAPIJson(callBack){
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function(){
		if(this.readyState == 4 && this.status == 200) {
			var json = JSON.parse(xhttp.responseText);
			callBack(json);
		}
	}
	xhttp.open("GET", "/socket/instance.json", true);
	xhttp.send();
}
function generateScopeParameter(scopes){
	var scopeString = "";
	for(var i = 0; i < scopes.length; i++){
		scopeString += scopes[i];
		if(i + 1 < scopes.length)
			scopeString += ",";
	}
	return encodeURI(scopeString);
}
function SocketAPIScopeSession(){
	this.scopes = [];
}
SocketAPIScopeSession.prototype.acceptSession = function acceptSession(session){
	this.session = session;
	var that = this;
	session.onmessage = function(msg){
		that.handleMessage(msg);
	};
}
SocketAPIScopeSession.prototype.sendMessage = function sendMessage(msg){
	this.session.send(msg);
}
SocketAPIScopeSession.prototype.registerScope = function registerScope(socketAPIScopeInstance){
	this.scopes[socketAPIScopeInstance.scope] = socketAPIScopeInstance;
}
SocketAPIScopeSession.prototype.getScopeInstance = function getScopeInstance(scope){
	return this.scopes[scope];
}
SocketAPIScopeSession.prototype.handleMessage = function handleMessage(message){
	var command = JSON.parse(message.data);
	console.log(command);
	var scope = command.command_scope;
	var commandName = command.command_name;
	var commandArgs = command.command_arguments;
	var method = this.scopes[scope].handler[commandName];
	method(commandArgs);
	//TODO add error handling
}
function SocketAPIScopeInstanceProvider(scope, envoker, handler){
	this.scope = scope;
	//this.createInstanceCommands = createInstanceCommands;
	this.envoker = envoker;//this.createInstanceCommands(envoker);
	this.handler = handler;
}
SocketAPIScopeInstanceProvider.prototype.createInstanceCommands = function createInstanceCommands(instance, envoker){
	var envokers = [];
	for(var i = 0; i < envoker.length; i++){
		var command = envoker[i];
		var name = command.name;
		var argNames = command.arg_names;
		var action = (function(cmdName, names){return function(...args){instance.sendCommand(cmdName, args, names);};})(name, argNames); 
		envokers[name] = action;
	}
	return envokers;
}
SocketAPIScopeInstanceProvider.prototype.getInstance = function getInstance(socketAPIScopeSession){
	var instance = new SocketAPIScopeInstance(this.scope, socketAPIScopeSession);
	instance.envoker = this.createInstanceCommands(instance, this.envoker);
	instance.handler = new this.handler(instance);
	return instance;
}
function SocketAPIScopeInstance(scope, socketAPIScopeSession){
	this.scope = scope;
	this.socketAPIScopeSession = socketAPIScopeSession;
}
SocketAPIScopeInstance.prototype.sendCommand = function sendCommand(name, args, argNames){
	var scope = this.scope;
	var commandArgs = [];
	for(var i = 0; i < argNames.length; i++){
		var commandArg = args[i];
		var commandName = argNames[i];
		commandArgs[i] = {"argument_class" : commandName,
						  "argument" : commandArg
			};
		}
	var command = {
		"command_scope" : scope,
		"command_name" : name,
		"command_arguments" : commandArgs
	};
	this.socketAPIScopeSession.sendMessage(JSON.stringify(command));
}