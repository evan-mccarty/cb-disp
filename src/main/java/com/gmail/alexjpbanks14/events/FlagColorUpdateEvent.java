package com.gmail.alexjpbanks14.events;

import com.gmail.alexjpbanks14.flagcolor.FlagColor;

public class FlagColorUpdateEvent extends Event{
	
	FlagColor color;
	FlagColor old_color;
	
	public FlagColorUpdateEvent(FlagColor color, FlagColor old_color) {
		super(EventType.FLAG_COLOR_UPDATE);
		this.color = color;
		this.old_color = old_color;
	}

	public FlagColor getColor() {
		return color;
	}

	public void setColor(FlagColor color) {
		this.color = color;
	}

	public FlagColor getOld_color() {
		return old_color;
	}

	public void setOld_color(FlagColor old_color) {
		this.old_color = old_color;
	}
	
}
