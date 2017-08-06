package com.gmail.alexjpbanks14.events;

public class RestrictionStateHistoryUpdateEvent extends Event{
	
	Object restrictionStateHistory;
	
	public RestrictionStateHistoryUpdateEvent(Object restrictionStateHistory) {
		super(EventType.RESTRICTION_STATE_HISTORY_UPDATE);
		this.restrictionStateHistory = restrictionStateHistory;
	}

	public Object getRestrictionStateHistory() {
		return restrictionStateHistory;
	}

	public void setRestrictionStateHistory(Object restrictionStateHistory) {
		this.restrictionStateHistory = restrictionStateHistory;
	}

}
