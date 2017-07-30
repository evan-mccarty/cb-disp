package com.gmail.alexjpbanks14.security;

import org.ehcache.Cache;
import org.ehcache.CacheManager;

import com.gmail.alexjpbanks14.exception.session.UserSessionException;
import com.gmail.alexjpbanks14.exception.session.UserSessionInvalidPasswordException;
import com.gmail.alexjpbanks14.model.User;

import spark.Session;

public class UserSessionManager {
	
	public static final String CACHE_NAME = "UserSessions";
	
	Cache<String, UserSession> userSessions;
	
	public UserSessionManager(CacheManager manager){
		//manager.getCache(arg0, arg1, arg2)
		userSessions = manager.getCache(CACHE_NAME, String.class, UserSession.class);
	}
	
	public void getSession(){
		//activeSessions = new HashMap<>();
	}
	
	public UserSession getUserSession(Session session){
		String id = session.id();
		if(userSessions.containsKey(id)){
			return userSessions.get(id);
		}
		return null;
	}
	
	public User getUser(Session session){
		UserSession userSession = getUserSession(session);
		if(userSession != null)
			return userSession.getUser();
		return null;
	}
	
	public UserSession putUser(Session session, User user){
		String id = session.id();
		UserSession userSession = new UserSession(id, user);
		userSessions.put(id, userSession);
		return userSession;
	}
	
	public UserSession addUserIfValid(Session session, String username, String password) throws UserSessionException{
		
		User user = User.findByUsername(username);
		if(user == null)
			throw new UserSessionException("User was not found!");
		if(PasswordUtil.authenticateUser(user, password)){
			return putUser(session, user);
		}else{
			throw new UserSessionInvalidPasswordException("Password was not correct");
		}
		
	}
	
}