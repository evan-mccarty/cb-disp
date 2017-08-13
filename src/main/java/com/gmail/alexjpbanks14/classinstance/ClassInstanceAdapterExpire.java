package com.gmail.alexjpbanks14.classinstance;

import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

public abstract class ClassInstanceAdapterExpire extends ClassInstanceAdapter{
	
	public ZonedDateTime nextUpdate;
	public Duration offset;
	public Duration failed;
	
	protected ClassInstanceAdapterExpire(String classType, String programType, Duration offset, Duration failed) {
		super(classType, programType);
		this.offset = offset;
		this.failed = failed;
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
	
	public Duration getFailed() {
		return failed;
	}
	
	public void setFailed(Duration failed) {
		this.failed = failed;
	}

}
