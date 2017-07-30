package com.gmail.alexjpbanks14.socketapi;

import com.gmail.alexjpbanks14.security.SocketAPISession;

public class SocketAPIScopeInstance <Handler extends SocketAPIScopeHandler, Envoker extends SocketAPIScopeEnvoker> {
	Handler handlerInstance;
	Envoker envokerInstance;
	SocketAPISession session;
	public SocketAPIScopeInstance(Handler handlerInstance, Envoker envokerInstance, SocketAPISession session) {
		this.handlerInstance = handlerInstance;
		this.envokerInstance = envokerInstance;
		this.session = session;
	}
	public Handler getHandlerInstance() {
		return handlerInstance;
	}
	public void setHandlerInstance(Handler handlerInstance) {
		this.handlerInstance = handlerInstance;
	}
	public Envoker getEnvokerInstance() {
		return envokerInstance;
	}
	public void setEnvokerInstance(Envoker envokerInstance) {
		this.envokerInstance = envokerInstance;
	}
	
}
