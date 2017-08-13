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
	this.callbacks = [];
	this.callbackIndex = 0;
}
SocketAPIScopeSession.prototype.nextCallbackIndex = function nextCallbackIndex(){
	var old = this.callbackIndex;
	this.callbackIndex++;
	return old;
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
	var scope = command.command_scope;
	var commandName = command.command_name;
	var commandArgs = command.command_arguments;
	var method;
	if(command.is_callback){
		var callbackId = command.callback_id;
		method = this.callbacks[callbackId];
	}else{
		method = this.scopes[scope].handler[commandName];
	}
	method.apply(this, commandArgs);
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
		var callback = command.has_callback;
		var argNames = command.arg_names;
		var action = (function(cmdName, cb, names){return function(...args){instance.sendCommand(cmdName, cb, args, names);};})(name, callback, argNames); 
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
SocketAPIScopeInstance.prototype.sendCommand = function sendCommand(name, callback, args, argNames){
	var scope = this.scope;
	var callbackFunction = null;
	console.log(args);
	if(callback){
		callbackFunction = args[args.length - 1];
		args.splice(-1, 1);
	}
	console.log(args);
	var commandArgs = [];
	for(var i = 0; i < argNames.length; i++){
		var commandArg = args[i];
		var commandName = argNames[i];
		commandArgs[i] = {"argument_class" : commandName,
						  "argument" : commandArg
			};
		}
	var callbackId;
	if(callback){
		 callbackId = this.socketAPIScopeSession.nextCallbackIndex();
		 this.socketAPIScopeSession.callbacks[callbackId] = callbackFunction;
	}else{
		 callbackId = null;
	}
	var command = {
		"command_scope" : scope,
		"command_name" : name,
		"command_arguments" : commandArgs,
		"has_callback" : callback,
		"is_callback" : false,
		"callback_id" : callbackId
	};
	this.socketAPIScopeSession.sendMessage(JSON.stringify(command));
}
function SocketAPIScopeFactory(){
	this.scopeProviders = [];
}
SocketAPIScopeFactory.prototype.addProvider = function addProvider(socketAPIScopeInstanceProvider){
	var scopeInstanceScope = socketAPIScopeInstanceProvider.scope;
	this.scopeProviders[scopeInstanceScope] = socketAPIScopeInstanceProvider;
}
SocketAPIScopeFactory.prototype.addScopesToInstance = function addScopesToInstance(socketAPIScopeSession, scopes){
	console.log(scopes);
	for(var i = 0; i < scopes.length; i++){
		var socketAPIScopeInstance = this.scopeProviders[scopes[i]].getInstance(socketAPIScopeSession);
		socketAPIScopeSession.registerScope(socketAPIScopeInstance);
	}
}
var socketAPIScopeFactoryPrimary = new SocketAPIScopeFactory();