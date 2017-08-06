package com.gmail.alexjpbanks14.socketapi.envoker;

import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeEnvoker;

public interface SocketAPIScopeRestrictionsEnvoker extends SocketAPIScopeEnvoker{
	
	public void restrictionUpdate(Object restrictions);
	public void restrictionStateHistoryUpdate(Object restrictions);

}
