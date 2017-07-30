package com.gmail.alexjpbanks14.socketapi.handler;

import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeBasicErrorHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeBasicErrorHandler(SocketAPISession session) {
		super(session);
	}
	
	public void handleError(String message){
		System.out.println("BUTTHOLE" + message);
	}
	
}
