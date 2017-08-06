package com.gmail.alexjpbanks14.restriction;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.events.RestrictionStateHistoryUpdateEvent;
import com.gmail.alexjpbanks14.model.RestrictionState;

public class RestrictionUpdater implements Runnable{
	
	ScheduledFuture future;
	
	public RestrictionUpdater(ScheduledExecutorService servicer, TimeZone zone, Duration offset) {
		ZonedDateTime currentTime = ZonedDateTime.now(zone.toZoneId());
		long initial = Duration.between(currentTime, currentTime.plusDays(1).toLocalDate().atStartOfDay(zone.toZoneId()).plus(offset)).toMillis();
		this.future = servicer.scheduleAtFixedRate(this, initial, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Object result = RestrictionState.getGsonRestrictionStatesForDay();
			RestrictionStateHistoryUpdateEvent event = new RestrictionStateHistoryUpdateEvent(result);
			CBI_TV.getInstance().getEventManager().envokeEvent(event);
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
}
