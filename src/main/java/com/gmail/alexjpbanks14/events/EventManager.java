package com.gmail.alexjpbanks14.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
	
	Map<EventType, List<EventHandler>> handlers;
	
	public EventManager() {
		handlers = new HashMap<>();
	}
	
	public void registerHandler(EventType type, EventHandler handler) {
		if(!handlers.containsKey(type))
			handlers.put(type, new LinkedList<>());
		handlers.get(type).add(handler);
	}
	
	public void envokeEvent(Event event) {
		if(this.handlers.containsKey(event.getEventType()))
			this.handlers.get(event.getEventType()).forEach((a) -> {
				a.handleEvent(event);
			});
	}
	
	
}
