package com.gmail.alexjpbanks14.socketapi.handler;

import com.gmail.alexjpbanks14.model.ClassInstance;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeClassUpdateHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeClassUpdateHandler(SocketAPISession session) {
		super(session);
	}
	
	public Object getClasses(Boolean adult, Boolean junior) {
		
		return ClassInstance.findAll();
		
	}

}
