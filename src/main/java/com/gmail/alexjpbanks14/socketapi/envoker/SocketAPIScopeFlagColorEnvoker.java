package com.gmail.alexjpbanks14.socketapi.envoker;

import com.gmail.alexjpbanks14.flagcolor.FlagColor;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeEnvoker;

public interface SocketAPIScopeFlagColorEnvoker extends SocketAPIScopeEnvoker{
	
	public void flagColorUpdate(FlagColor newColor);
	
}
