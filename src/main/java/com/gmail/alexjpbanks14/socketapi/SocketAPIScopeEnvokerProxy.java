package com.gmail.alexjpbanks14.socketapi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;

import com.gmail.alexjpbanks14.security.SocketAPISession;

public class SocketAPIScopeEnvokerProxy implements InvocationHandler{
	
	SocketAPIScope scope;
	SocketAPISession session;
	
	public SocketAPIScopeEnvokerProxy(SocketAPIScope scope, SocketAPISession session){
		this.scope = scope;
		this.session = session;
	}
	
	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
		SocketAPICommand command = new SocketAPICommand(scope, arg1.getName(), arg2, false, false, null);
		try{
			session.sendCommand(command);
			System.out.println(command);
		}catch(Throwable e){
			if(ArrayUtils.contains(arg1.getExceptionTypes(), e.getClass()))
				throw e;
			e.printStackTrace();
		}
		return null;
	}

	public SocketAPISession getSession() {
		return session;
	}

	public void setSession(SocketAPISession session) {
		this.session = session;
	}

}
