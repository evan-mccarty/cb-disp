package com.gmail.alexjpbanks14.classinstance;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

import com.gmail.alexjpbanks14.exception.api.CBIClassInstanceAPIInvalidResponseException;
import com.gmail.alexjpbanks14.exception.api.CBIClassInstanceAPIUnchangedException;
import com.gmail.alexjpbanks14.model.ClassInstance;
import com.gmail.alexjpbanks14.webservice.CBIClassInstanceAPI;
import com.gmail.alexjpbanks14.webservice.CBIClassInstanceAPI.CBClassJsonDataHolder;
import com.gmail.alexjpbanks14.webservice.CBIClassInstanceAPI.CBClassJsonMetaData;
import com.google.gson.JsonSyntaxException;

public class ClassInstanceCBIAPIAdapter extends ClassInstanceAdapter {
	
	public static final String INSTANCE_ID_KEY = "INSTANCE_ID";
	public static final String TYPE_NAME_KEY = "TYPE_NAME";
	public static final String START_DATE_KEY = "START_DATE";
	public static final String START_TIME_KEY = "START_TIME";
	public static final String LOCATION_NAME_KEY = "LOCATION_NAME";
	public static final String INSTRUCTOR_NAME_LAST_KEY = "INSTRUCTOR_NAME_LAST";
	public static final String INSTRUCTOR_NAME_FIRST_KEY = "INSTRUCTOR_NAME_FIRST";
	public static final String ENROLLEES_KEY = "ENROLLEES";
	
	private URL jsonURL;
	private String lastETag;
	
	private DateTimeFormatter dateFormatter;
	private DateTimeFormatter timeFormatter;
	
	public ClassInstanceCBIAPIAdapter(String classType, String programType, URL jsonURL){
		super(classType, programType);
		this.jsonURL = jsonURL;
		this.lastETag = new String();
		this.dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		this.timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
	}
	
	public Set<ClassInstance> getClasses(){
		Set<ClassInstance> classInstances = new LinkedHashSet<>();
		CBIClassInstanceAPI cb_api = CBIClassInstanceAPI.getDefaultInstance();
		try {
			CBClassJsonDataHolder data = cb_api.getCBClassJsonData(lastETag, jsonURL);
			lastETag = data.etag;
			for(String[] values : data.data.rows){
				//TODO fix this crap
				ClassInstance instance = parseInformation(values, data.data.metaData);
				classInstances.add(parseInformation(values, data.data.metaData));
			}
			return null;
		} catch (CBIClassInstanceAPIUnchangedException e) {
			return null;
		} catch (IOException | CBIClassInstanceAPIInvalidResponseException | JsonSyntaxException e) {
			setException(e);
			e.printStackTrace();
			return null;
		}
	}
	
	private ClassInstance parseInformation(String[] values, CBClassJsonMetaData[] metaData){
		ClassInstance classInstance = new ClassInstance();
		LocalDate startDate = null;
		LocalTime startTime = null;
		for(int i = 0; i < values.length && i < metaData.length; i++){
			String metaDataKey = metaData[i].name;
			String value = values[i];
			if(metaDataKey.equals(INSTANCE_ID_KEY))
				classInstance.setInstanceId(value);
			if(metaDataKey.equals(TYPE_NAME_KEY))
				classInstance.setType(value);
			if(metaDataKey.equals(START_DATE_KEY))
				startDate = LocalDate.parse(value, dateFormatter);
			if(metaDataKey.equals(START_TIME_KEY))
				startTime = LocalTime.parse(value, timeFormatter);
			if(metaDataKey.equals(LOCATION_NAME_KEY))
				classInstance.setLocation(value);
			if(metaDataKey.equals(INSTRUCTOR_NAME_FIRST_KEY))
				classInstance.setInstructorFirst(value);
			if(metaDataKey.equals(INSTRUCTOR_NAME_LAST_KEY))
				classInstance.setInstructorLast(value);
			if(metaDataKey.equals(ENROLLEES_KEY))
				classInstance.setEnrollees(Integer.parseInt(value));
				
		}
		LocalDateTime dateTime = LocalDateTime.of(startDate, startTime);
		classInstance.setStartTime(dateTime);
		classInstance.setClassType(this.getClassType());
		classInstance.setProgramType(this.getProgramType());
		return classInstance;
	}
	
}
