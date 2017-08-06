package com.gmail.alexjpbanks14.events;

public interface EventHandler <eventType extends Event> {
	
	public void handleEvent(eventType type);
	
}
