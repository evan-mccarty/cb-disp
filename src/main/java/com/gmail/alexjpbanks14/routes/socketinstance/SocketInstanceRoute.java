package com.gmail.alexjpbanks14.routes.socketinstance;


import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.CBI_TV.CBI_TVRoutes;
import com.gmail.alexjpbanks14.security.SocketAPISessionAuthorizer;
import com.gmail.alexjpbanks14.security.UserSession;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public class SocketInstanceRoute implements Route{
	Gson gson;
	
	public SocketInstanceRoute() {
		gson = new Gson();
	}

	@Override
	public Object handle(Request req, Response res) {
		UserSession user = CBI_TV.getInstance().getUserManager().getUserSession(req.session());
		//System.out.println(user);
		SocketAPISessionAuthorizer session = CBI_TV.getInstance().getSocketManager().requestSession(user);
		
		SocketInstanceJson json = new SocketInstanceJson(session.getUUID().toString(), session.getAuthToken(), CBI_TVRoutes.SOCKET_API_HANDLER);
		
		return gson.toJson(json, SocketInstanceJson.class);
		//template.put("sessionId", session.getUUID().toString());
		//template.put("authToken", session.getAuthToken());
		//template.put("socketPath", CBI_TVRoutes.SOCKET_API_HANDLER);
	}
	
	public class SocketInstanceJson{
		
		public String sessionId;
		public String authToken;
		public String socketPath;
		
		public SocketInstanceJson(String sessionId, String authToken, String socketPath) {
			this.sessionId = sessionId;
			this.authToken = authToken;
			this.socketPath = socketPath;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public String getAuthToken() {
			return authToken;
		}
		public void setAuthToken(String authToken) {
			this.authToken = authToken;
		}
		public String getSocketPath() {
			return socketPath;
		}
		public void setSocketPath(String socketPath) {
			this.socketPath = socketPath;
		}
		
	}

}
