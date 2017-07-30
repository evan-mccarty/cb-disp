package com.gmail.alexjpbanks14.security;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

public class SocketAPISessionAuthorizer {
	
	public static Duration EXPIRE_TIME = Duration.ofHours(8);
	
	private UUID UUID;
	private String authToken;
	private UserSession userSession;
	private ZonedDateTime requested;
	private boolean isConsumed;
	
	public SocketAPISessionAuthorizer(java.util.UUID uUID, String authToken, UserSession userSession,
			ZonedDateTime requested) {
		UUID = uUID;
		this.authToken = authToken;
		this.userSession = userSession;
		this.requested = requested;
		this.isConsumed = false;
	}

	public UUID getUUID() {
		return UUID;
	}

	public void setUUID(UUID uUID) {
		UUID = uUID;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public ZonedDateTime getRequested() {
		return requested;
	}

	public void setRequested(ZonedDateTime requested) {
		this.requested = requested;
	}

	public boolean isConsumed() {
		return isConsumed;
	}

	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}
	
	public void consume(){
		this.setConsumed(true);
	}
	
	public boolean isExpired(ZonedDateTime time){
		return Duration.between(requested, time).compareTo(EXPIRE_TIME) > 0;
	}
	
}
