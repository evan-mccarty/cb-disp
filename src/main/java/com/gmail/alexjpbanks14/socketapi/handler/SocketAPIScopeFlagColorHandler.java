package com.gmail.alexjpbanks14.socketapi.handler;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.flagcolor.FlagColor;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeFlagColorHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeFlagColorHandler(SocketAPISession session) {
		super(session);
	}
	
	public FlagColor getFlagColor() {
		return CBI_TV.getInstance().getFlagColorFetcher().getFlagColor();
	}
	
}
