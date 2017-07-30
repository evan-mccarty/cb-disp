package com.gmail.alexjpbanks14.socketapi;

public class SocketAPICommand {
	
	public SocketAPIScope scope;
	
	public String commandName;
	
	public Object[] commandArguments;
	
	public SocketAPICommand(SocketAPIScope scope, String commandName, Object[] commandArguments){
		this.scope = scope;
		this.commandName = commandName;
		this.commandArguments = commandArguments;
	}
	
}
