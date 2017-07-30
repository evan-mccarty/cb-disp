package com.gmail.alexjpbanks14.socketapi;

import java.util.EnumSet;

public enum SocketAPIScope {
	TEST, TEST2, BASIC_ERROR;
	public static EnumSet<SocketAPIScope> parse(String args) throws IllegalArgumentException{
		EnumSet<SocketAPIScope> scopes = EnumSet.noneOf(SocketAPIScope.class);
		if(args == null)
			return scopes;
		for(String scope : args.split(",")){
			scopes.add(SocketAPIScope.valueOf(scope));
		}
		return scopes;
	}
}