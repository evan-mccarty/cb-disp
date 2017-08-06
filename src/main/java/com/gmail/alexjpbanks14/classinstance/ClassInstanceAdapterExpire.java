package com.gmail.alexjpbanks14.classinstance;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

public abstract class ClassInstanceAdapterExpire extends ClassInstanceAdapter{
	
	public ZonedDateTime nextUpdate;
	public Duration offset;
	
	protected ClassInstanceAdapterExpire(String classType, String programType, Duration offset) {
		super(classType, programType);
		this.offset = offset;
	}

	public ZonedDateTime getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(ZonedDateTime nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

	public Duration getOffset() {
		return offset;
	}

	public void setOffset(Duration offset) {
		this.offset = offset;
	}

}
