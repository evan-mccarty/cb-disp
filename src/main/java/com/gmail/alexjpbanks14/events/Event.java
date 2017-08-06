package com.gmail.alexjpbanks14.events;

public class Event {
	
	public EventType type;
	public Event(EventType type) {
		this.type = type;
	}
	
	public EventType getEventType() {
		return type;
	}
	
	public void setEventType(EventType type) {
		this.type = type;
	}
	
}
