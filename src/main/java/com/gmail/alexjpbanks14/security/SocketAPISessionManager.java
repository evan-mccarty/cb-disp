package com.gmail.alexjpbanks14.security;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.eclipse.jetty.websocket.api.Session;
import org.ehcache.Cache;
import org.ehcache.CacheManager;

import com.gmail.alexjpbanks14.exception.socket.SocketSessionAPIInvalidPermissionException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionAPIUnregisteredScopeException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionConsumedException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionExpiredException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionInvalidTokenException;
import com.gmail.alexjpbanks14.exception.socket.SocketSessionUnknownUUIDException;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstanceFactory;

public class SocketAPISessionManager {
	
	public static final String CACHE_NAME = "socket_session";
	
	public SocketAPIScopeInstanceFactory factory;
	
	CacheManager manager;
	Cache<Session, SocketAPISession> socketSessionsCache;
	Cache<UUID, SocketAPISessionAuthorizer> socketSessionAuthorizers;
	
	public SocketAPISessionManager(CacheManager manager, SocketAPIScopeInstanceFactory factory){
		this.manager = manager;
		socketSessionsCache = manager.getCache("SocketSessions", Session.class, SocketAPISession.class);
		//manager.createCache(CACHE_NAME, CacheConfigurationBuilder.newCacheConfigurationBuilder(UUID.class, ))
		socketSessionAuthorizers = manager.getCache("SocketSessionAuthorizers", UUID.class, SocketAPISessionAuthorizer.class);
		this.factory = factory;
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				SocketAPISessionManager.this.socketSessionsCache.forEach((a) -> {
					a.getValue().getBasicErrorEnvoker().getEnvokerInstance().sendError("testing error stuffs");
				});
			}
			
		}, 0, 10000);
	}
	
	public SocketAPISessionAuthorizer requestSession(UserSession userSession){
		UUID uuid = UUID.randomUUID();
		String token = getRandomToken();
		SocketAPISessionAuthorizer session = new SocketAPISessionAuthorizer(uuid, token, userSession, ZonedDateTime.now());
		socketSessionAuthorizers.put(uuid, session);
		return session;
	}
	
	public SocketAPISession authorizeSession(UUID uuid, String token, EnumSet<SocketAPIScope> scopes, Session session) throws SocketSessionException{
		if(!socketSessionAuthorizers.containsKey(uuid))
			throw new SocketSessionUnknownUUIDException("UUID was not found in requested sessions");
		SocketAPISessionAuthorizer requested = socketSessionAuthorizers.get(uuid);
		if(requested.isConsumed())
			throw new SocketSessionConsumedException("SocketSession is already being used");
		if(requested.isExpired(ZonedDateTime.now()))
			throw new SocketSessionExpiredException("SocketSession has expired");
		if(!requested.getAuthToken().equals(token))
			throw new SocketSessionInvalidTokenException("Token is not valid");
		requested.consume();
		socketSessionAuthorizers.remove(requested.getUUID());
		SocketAPISession sessionInstance = new SocketAPISession(session, requested);
		socketSessionsCache.put(session, sessionInstance);
		scopes.forEach((scope) -> {
			List<String> errors = new LinkedList<String>();
			try {
				sessionInstance.acceptSocketAPIScope(scope, factory.getInstance(scope, sessionInstance));
			} catch (SocketSessionAPIInvalidPermissionException | SocketSessionAPIUnregisteredScopeException e) {
				e.printStackTrace();
				errors.add(e.getMessage());
			}
			if(sessionInstance.hasScope(SocketAPIScope.BASIC_ERROR))
				errors.forEach((error) -> {
					sessionInstance.getBasicErrorEnvoker().getEnvokerInstance().sendError(error);
				});
		});
		return sessionInstance;
	}
	
	public SocketAPISession getSessionInstance(Session session){
		return socketSessionsCache.get(session);
	}
	
	private String getRandomToken(){
		SecureRandom random = new SecureRandom();
		byte[] token = new byte[64];
		random.nextBytes(token);
		return Hex.encodeHexString(token);
	}
	
}