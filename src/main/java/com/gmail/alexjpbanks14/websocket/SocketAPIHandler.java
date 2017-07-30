package com.gmail.alexjpbanks14.websocket;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.gson.SocketSessionCommandGson;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPICommand;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstanceFactory;

@WebSocket
public class SocketAPIHandler {
	
	SocketAPIScopeInstanceFactory socketAPIInstanceFactory;
	
	@OnWebSocketConnect
	public void connected(Session session) throws Exception{
		Map<String, List<String>> paramMaps = session.getUpgradeRequest().getParameterMap();
		String authToken = getParameterValue(paramMaps, "authToken");
		UUID sessionId = UUID.fromString(getParameterValue(paramMaps, "sessionId"));
		EnumSet<SocketAPIScope> scopes = null;
		try{
			scopes = SocketAPIScope.parse(getParameterValue(paramMaps, "scopes"));
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		//System.out.println(scopes);
		try {
			//System.out.println("leet memes");
			CBI_TV.getInstance().socketManager.authorizeSession(sessionId, authToken, scopes, session);
			//System.out.println("fjdkslafdj");
			//Thread.sleep(100);
			//sessionState.getBasicErrorEnvoker().getEnvokerInstance().sendError("leet memes");
		} catch (Exception e) {
		//	System.out.println(e1.getMessage());
			e.printStackTrace();
			throw e;
		//	System.out.println("error");
		}
		//System.out.println("lerp");
		
		//session.getUpgradeResponse().setHeader("Sec-WebSocket-Protocol", derp);
		//System.out.println(derp);
	}
	
	private String getParameterValue(Map<String, List<String>> params, String name){
		if(params.containsKey(name) && params.get(name).size() > 0)
			return params.get(name).get(0);
		return null;
	}
	
	@OnWebSocketClose
	public void close(Session session, int statusCode, String reason){
		
	}
	
	@OnWebSocketMessage
	public void message(Session session, String message) throws Exception{
		SocketAPISession instance = CBI_TV.getInstance().socketManager.getSessionInstance(session);
		try{
			SocketAPICommand command = SocketSessionCommandGson.instance.fromJson(message, SocketAPICommand.class);
			instance.handleCommand(command);
		}catch(Exception e){
			e.printStackTrace();
			if(instance != null)
				instance.sendErrorIfPossible(e.getMessage());
		}
	}
	
}
