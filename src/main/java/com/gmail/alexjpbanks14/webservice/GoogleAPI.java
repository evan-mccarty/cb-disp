package com.gmail.alexjpbanks14.webservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;

public class GoogleAPI {
	
	private GoogleCredential credentials;
	private JsonFactory jsonFactory;
	private HttpTransport transport;
	private Collection<String> scopes;
	private String applicationName;
	
	
	public GoogleAPI(GoogleCredential credentials, JsonFactory jsonFactory, HttpTransport transport,
			Collection<String> scopes, String applicationName) {
		this.credentials = credentials;
		this.jsonFactory = jsonFactory;
		this.transport = transport;
		this.scopes = scopes;
		this.applicationName = applicationName;
	}
	
	public static GoogleAPI makeGoogleAPI(String credential, Collection<String> scope, String applicationName) throws FileNotFoundException, IOException, GeneralSecurityException{
		GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream(credential)).createScoped(scope);
		GoogleAPI api = new GoogleAPI(credentials, JacksonFactory.getDefaultInstance(), GoogleNetHttpTransport.newTrustedTransport(), scope, applicationName);
		return api;
	}

	public GoogleCredential getCredentials() {
		return credentials;
	}

	public void setCredentials(GoogleCredential credentials) {
		this.credentials = credentials;
	}

	public JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	public void setJsonFactory(JsonFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}

	public HttpTransport getTransport() {
		return transport;
	}

	public void setTransport(HttpTransport transport) {
		this.transport = transport;
	}

	public Collection<String> getScopes() {
		return scopes;
	}

	public void setScopes(Collection<String> scopes) {
		this.scopes = scopes;
	}
	
	public Calendar getCalendar(){
		return new Calendar.Builder(transport, jsonFactory, credentials).setApplicationName(applicationName).build();
	}
	
	public Events getCalendarEventsForToday(String calendarId, String eTag, ZonedDateTime today) throws IOException, GoogleJsonResponseException{
		TimeZone zone2 = TimeZone.getTimeZone(today.getZone());
		LocalDate date = today.toLocalDate();
		Date todayDate = Date.from(date.atTime(LocalTime.MIN).toInstant(today.getOffset()));
		Date tomorrowDate = Date.from(date.atTime(LocalTime.MIN).plusDays(1).toInstant(today.getOffset()));
		//TimeZone zone = now2.
		com.google.api.client.util.DateTime now = new com.google.api.client.util.DateTime(todayDate, zone2);
		com.google.api.client.util.DateTime later = new com.google.api.client.util.DateTime(tomorrowDate, zone2);
		//GoogleAPI api = GoogleAPI.makeGoogleAPI("CBI-TV-81096270af83.json", Collections.singleton(CalendarScopes.CALENDAR_READONLY), "test program");
		com.google.api.services.calendar.Calendar.Events.List get = this.getCalendar().events().list("community-boating.org_3lrgpalth1huovkki81uosqo2c@group.calendar.google.com")
		.setMaxResults(10).setTimeMin(now).setTimeMax(later).setSingleEvents(true);
		get.setRequestHeaders(new HttpHeaders().setIfNoneMatch(eTag));
		//System.out.println(now);
		return get.execute();
		//List<com.google.api.services.calendar.model.Event> eventList = events.getItems();
		//System.out.println(events.size());
		//for(com.google.api.services.calendar.model.Event event : eventList){
		//	System.out.println(event.getSummary());
			//System.out.println(event.getStart().getDateTime());
			//Instant instance = Instant.ofEpochMilli(event.getStart().getDateTime().getValue());
			//LocalDateTime datething = LocalDateTime.ofInstant(instance, now3.getOffset());
			//System.out.println(datething.format(DateTimeFormatter.BASIC_ISO_DATE));
		//}
	}
 	
}
