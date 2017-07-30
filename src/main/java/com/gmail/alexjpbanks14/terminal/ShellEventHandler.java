package com.gmail.alexjpbanks14.terminal;

import com.gmail.alexjpbanks14.terminal.ShellEvent.ShellEventType;

public interface ShellEventHandler {
	
	public ShellEventType getType();
	public void handleEvent(ShellEvent event);
	
}
