package com.gmail.alexjpbanks14.classinstance;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import com.gmail.alexjpbanks14.model.ClassInstance;
import com.gmail.alexjpbanks14.webservice.GoogleAPI;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class ClassInstanceGoogleCalendarAdapter extends ClassInstanceAdapter {
	
	private GoogleAPI api;
	private String lastETag;
	private String calendarId;
	private TimeZone zone;
	
	public ClassInstanceGoogleCalendarAdapter(GoogleAPI api, String calendarId, TimeZone zone){
		super("derp", "derp");
		this.api = api;
		this.calendarId = calendarId;
		this.zone = zone;
	}
	
	public Set<ClassInstance> getClasses(){
		//TODO make this use global time zone
		ZonedDateTime now = ZonedDateTime.now(zone.toZoneId());
		Set<ClassInstance> classInstances = new LinkedHashSet<>();
		try	{
			Events events = api.getCalendarEventsForToday(calendarId, lastETag, now);
			this.lastETag = events.getEtag();
			List<Event> eventList = events.getItems();
			for(Event event : eventList){
				//TODO fix this
				ClassInstance instance = parseInformation(event, now.getZone());
				classInstances.add(parseInformation(event, now.getZone()));
			}
			return classInstances;
		}
		catch(GoogleJsonResponseException e){
			if(e.getStatusCode() != 304){
				setException(e);
			}
		}
		catch(IOException e){
			setException(e);
		}
		return null;
	}
	
	public ClassInstance parseInformation(Event event, ZoneId zone){
		ClassInstance instance = new ClassInstance();
		instance.setInstanceId(event.getId());
		instance.setType(event.getSummary());
		Instant instant = Instant.ofEpochMilli(event.getStart().getDateTime().getValue());
		ZonedDateTime time = ZonedDateTime.ofInstant(instant, zone);
		instance.setStartTime(time.toLocalDateTime());
		instance.setClassType(this.getClassType());
		instance.setProgramType(this.getProgramType());
		return instance;
	}

}
