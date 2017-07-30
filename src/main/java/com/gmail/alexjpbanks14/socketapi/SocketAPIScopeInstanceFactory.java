package com.gmail.alexjpbanks14.socketapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.gmail.alexjpbanks14.exception.socket.SocketSessionAPIInvalidPermissionException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionAPIUnregisteredScopeException;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.permission.SocketAPIScopePermission;

public class SocketAPIScopeInstanceFactory {
	
	@SuppressWarnings("rawtypes")
	private Map<SocketAPIScope, SocketAPIScopeInstanceProvider> scopeInstances;
	
	public SocketAPIScopeInstanceFactory(){
		scopeInstances = new HashMap<>();
	}
	
	public <handlerType extends SocketAPIScopeHandler, envokerType extends SocketAPIScopeEnvoker> void registerScope(SocketAPIScope scope, Class<handlerType> handler,
			Class<envokerType> envoker, SocketAPIScopePermission permission){
		SocketAPIScopeInstanceProvider<handlerType, envokerType> provider = new SocketAPIScopeInstanceProvider<>(scope, handler, envoker, permission);
		//SocketAPIScopeInstance<handler, envoker> instance;
		scopeInstances.put(scope, provider);
		//Class clazz = SocketAPIScopeInstance<handlerType extends SocketAPIScopeHandler, envokerType extends SocketAPIScopeEnvoker.class;
		//List<handler> list = null;
	}
	
	@SuppressWarnings("rawtypes")
	public SocketAPIScopeInstance getInstance(SocketAPIScope scope, SocketAPISession session) throws SocketSessionAPIInvalidPermissionException, SocketSessionAPIUnregisteredScopeException{
		if(!scopeInstances.containsKey(scope))
			throw new SocketSessionAPIUnregisteredScopeException("Scope " + scope + " was not found");
		if(!scopeInstances.get(scope).getPermission().isPermissable(session))
			throw new SocketSessionAPIInvalidPermissionException("permission was not valid for " + scope.name());
		return scopeInstances.get(scope).getInstance(session);
	}
	
	public class SocketAPIScopeInstanceProvider <handlerType extends SocketAPIScopeHandler, envokerType extends SocketAPIScopeEnvoker>{
		private SocketAPIScope scope;
		private Class<handlerType> handler;
		private Class<envokerType> envoker;
		private SocketAPIScopePermission permission;
		public SocketAPIScopeInstanceProvider(SocketAPIScope scope, Class<handlerType> handler, Class<envokerType> envoker, SocketAPIScopePermission permission){
			this.scope = scope;
			this.handler = handler;
			this.envoker = envoker;
			this.permission = permission;
		}
		@SuppressWarnings("unchecked")
		public SocketAPIScopeInstance<handlerType, envokerType> getInstance(SocketAPISession session){
			handlerType handlerInstance;
			
				//System.out.println(handler);
				try {
					Constructor<handlerType> defaultConstructor = handler.getConstructor(SocketAPISession.class);
					handlerInstance = defaultConstructor.newInstance(session);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
					return null;
				}
			
			//envokerType envokerInstance = envoker.newInstance();
			SocketAPIScopeEnvokerProxy proxyInstance = new SocketAPIScopeEnvokerProxy(scope, session);
			envokerType envokerInstance = (envokerType) Proxy.newProxyInstance(envoker.getClassLoader(), new Class[]{envoker}, proxyInstance);
			SocketAPIScopeInstance<handlerType, envokerType> instance = new SocketAPIScopeInstance<handlerType, envokerType>(handlerInstance, envokerInstance, session);
			return instance;
		}
		public SocketAPIScopePermission getPermission(){
			return this.permission;
		}
	}
	
}
