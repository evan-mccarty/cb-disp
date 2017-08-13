package com.gmail.alexjpbanks14.security;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.joda.time.Period;

import com.gmail.alexjpbanks14.gson.SocketSessionCommandGson;
import com.gmail.alexjpbanks14.socketapi.SocketAPICommand;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeEnvoker;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstance;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeBasicErrorEnvoker;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeFlagColorEnvoker;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeRestrictionsEnvoker;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeFlagColorHandler;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeRestrictionsHandler;

public class SocketAPISession{
	
	public static final Period EXPIRE_TIME = Period.hours(2);
	
	@SuppressWarnings("rawtypes")
	Map<SocketAPIScope, SocketAPIScopeInstance> scopeInstances;
	
	SocketAPISessionAuthorizer authorizer;
	
	private Session session;
	public SocketAPISession(Session session, SocketAPISessionAuthorizer authorizer) {
		super();
		this.session = session;
		this.authorizer = authorizer;
		this.scopeInstances = new HashMap<>();
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	@SuppressWarnings("rawtypes")
	public void acceptSocketAPIScope(SocketAPIScope scope, SocketAPIScopeInstance instance){
		scopeInstances.put(scope, instance);
	}
	@SuppressWarnings("unchecked")
	public <Handler extends SocketAPIScopeHandler, Envoker extends SocketAPIScopeEnvoker> SocketAPIScopeInstance<Handler, Envoker> getInstance(SocketAPIScope scope){
		if(this.hasScope(scope))
			return (SocketAPIScopeInstance<Handler, Envoker>)scopeInstances.get(scope);
		else
			throw new RuntimeException("Scope was attempted to be accessed but had no active instance");
		//TODO fix this to use real custom throwable
	}
	public boolean hasScope(SocketAPIScope scope){
		return scopeInstances.containsKey(scope);
	}
	public SocketAPIScopeInstance<SocketAPIScopeHandler, SocketAPIScopeBasicErrorEnvoker> getBasicErrorEnvoker(){
		return this.getInstance(SocketAPIScope.BASIC_ERROR);
	}
	public SocketAPIScopeInstance<SocketAPIScopeFlagColorHandler, SocketAPIScopeFlagColorEnvoker> getFlagUpdateScope(){
		return this.getInstance(SocketAPIScope.FLAG_COLOR);
	}
	public SocketAPIScopeInstance<SocketAPIScopeRestrictionsHandler, SocketAPIScopeRestrictionsEnvoker> getRestrictionsScope(){
		return this.getInstance(SocketAPIScope.RESTRICTION);
	}
	public SocketAPISessionAuthorizer getAuthorizer() {
		return authorizer;
	}
	public void setAuthorizer(SocketAPISessionAuthorizer authorizer) {
		this.authorizer = authorizer;
	}
	public void sendCommand(SocketAPICommand command) throws IOException{
		String commandJson = SocketSessionCommandGson.instance.toJson(command, SocketAPICommand.class);
		this.session.getRemote().sendString(commandJson);
	}
	public void sendErrorIfPossible(String error){
		if(this.hasScope(SocketAPIScope.BASIC_ERROR))
			this.getBasicErrorEnvoker().getEnvokerInstance().sendError(error);
	}
	@SuppressWarnings("rawtypes")
	public void handleCommand(SocketAPICommand command) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		SocketAPIScopeHandler handler = getInstance(command.scope).getHandlerInstance();
		String methodName = command.commandName;
		Class[] classes = getClassesFromCommand(command);
		Method method = handler.getClass().getMethod(methodName, classes);
		Object response = method.invoke(handler, command.commandArguments);
		if(command.hasCallback) {
			Object[] responseParameters;
			if(!method.getReturnType().equals(Void.TYPE))
				responseParameters = new Object[] { response };
			else
				responseParameters = new Object[0];
			SocketAPICommand responseCommand = new SocketAPICommand(command.scope, null, responseParameters, false, true, command.callbackId);
			try {
				this.sendCommand(responseCommand);
			} catch (IOException e) {
				e.printStackTrace();
				this.sendErrorIfPossible(e.getMessage());
			}
		}
	}
		
	@SuppressWarnings("rawtypes")
	Class[] getClassesFromCommand(SocketAPICommand command){
		Object[] args = command.commandArguments;
		Class[] classes = new Class[args.length];
		for(int i = 0; i < args.length; i++){
			Object arg = args[i];
			classes[i] = arg.getClass();
		}
		return classes;
	}
}
