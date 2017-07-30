package com.gmail.alexjpbanks14.socketapi.permission;

import com.gmail.alexjpbanks14.security.SocketAPISession;

public interface SocketAPIScopePermission {
	
	public boolean isPermissable(SocketAPISession session);
	
}
