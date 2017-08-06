package com.gmail.alexjpbanks14.socketapi;

public class SocketAPICommand {
	
	public Boolean hasCallback;
	public Boolean isCallback;
	
	public Long callbackId;
	
	public SocketAPIScope scope;
	
	public String commandName;
	
	public Object[] commandArguments;
	
	public SocketAPICommand(SocketAPIScope scope, String commandName, Object[] commandArguments, Boolean hasCallback, Boolean isCallback, Long callbackId){
		this.scope = scope;
		this.commandName = commandName;
		this.commandArguments = commandArguments;
		this.hasCallback = hasCallback;
		this.isCallback = isCallback;
		this.callbackId = callbackId;
	}
	
}
