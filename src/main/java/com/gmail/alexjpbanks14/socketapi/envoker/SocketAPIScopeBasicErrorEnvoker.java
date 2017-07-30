package com.gmail.alexjpbanks14.socketapi.envoker;

import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeEnvoker;

public interface SocketAPIScopeBasicErrorEnvoker extends SocketAPIScopeEnvoker{
	
	public void sendError(String error);
	
}
