package com.gmail.alexjpbanks14.terminal;

public class ShellEvent {
	
	private String eventMessage;
	private ShellEventType type;
	
	public ShellEvent(String eventMessage, ShellEventType type) {
		super();
		this.eventMessage = eventMessage;
		this.type = type;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public ShellEventType getType() {
		return type;
	}

	public void setType(ShellEventType type) {
		this.type = type;
	}

	public enum ShellEventType{
		OUTPUT, ERROR;
	}
	
}
