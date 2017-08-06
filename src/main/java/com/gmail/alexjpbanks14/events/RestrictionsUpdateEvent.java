package com.gmail.alexjpbanks14.events;

public class RestrictionsUpdateEvent extends Event{
	
	Object restrictions;
	
	public RestrictionsUpdateEvent(Object restrictions) {
		super(EventType.RESTRICTIONS_UPDATE);
		this.restrictions = restrictions;
	}

	public Object getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Object restrictions) {
		this.restrictions = restrictions;
	}
	
}
