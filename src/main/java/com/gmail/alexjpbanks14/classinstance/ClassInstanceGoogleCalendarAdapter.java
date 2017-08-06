package com.gmail.alexjpbanks14.classinstance;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
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
	
	public ClassInstanceGoogleCalendarAdapter(GoogleAPI api, String calendarId){
		super("derp", "derp");
		this.api = api;
		this.calendarId = calendarId;
	}
	
	public LinkedList<ClassInstance> getClasses(ZonedDateTime time){
		//ZonedDateTime now = ZonedDateTime.now(zone.toZoneId());
		LinkedList<ClassInstance> classInstances = new LinkedList<>();
		try	{
			Events events = api.getCalendarEventsForToday(calendarId, lastETag, time);
			this.lastETag = events.getEtag();
			List<Event> eventList = events.getItems();
			for(Event event : eventList){
				//TODO fix this
				//ClassInstance instance = parseInformation(event, now.getZone());
				classInstances.add(parseInformation(event, time.getZone()));
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
