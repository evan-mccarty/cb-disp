package com.gmail.alexjpbanks14.socketapi.permission;

import com.gmail.alexjpbanks14.security.SocketAPISession;

public class SocketAPIScopeGlobalPermission implements SocketAPIScopePermission{

	@Override
	public boolean isPermissable(SocketAPISession session) {
		return true;
	}

}
