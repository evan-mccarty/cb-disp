package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.javalite.activejdbc.Model;

public class ClassInstance extends Model{
	
	public String getInstanceId(){
		return this.getString("instance_id");
	}
	
	public void setInstanceId(String instanceId){
		this.setString("instance_id", instanceId);
	}
	
	public String getType(){
		return this.getString("type");
	}
	
	public void setType(String type){
		this.setString("type", type);
	}
	
	public LocalDateTime getStart(){
		return this.getTimestamp("start_date").toLocalDateTime();
	}
	
	public void setStartTime(LocalDateTime dateTime){
		this.setTimestamp("start_date", Timestamp.valueOf(dateTime));
	}
	
	public String getLocation(){
		return this.getString("location");
	}
	
	public void setLocation(String location){
		this.setString("location", location);
	}
	
	public Integer getEnrollees(){
		return this.getInteger("enrollees");
	}
	
	public void setEnrollees(Integer enrollees){
		this.setInteger("enrollees", enrollees);
	}
	
	public String getInstructorLast(){
		return this.getString("instructor_last");
	}
	
	public void setInstructorLast(String instructorLast){
		this.setString("instructor_last", instructorLast);
	}
	
	public String getInstructorFirst(){
		return this.getString("instructor_first");
	}
	
	public void setInstructorFirst(String instructorFirst){
		this.setString("instructor_first", instructorFirst);
	}
	
	public Boolean isVisisble(){
		return this.getBoolean("visible");
	}
	
	public void setIsVisisble(Boolean visible){
		this.setBoolean("visible", visible);
	}
	
	public Boolean isCancelled(){
		return this.getBoolean("cancelled");
	}
	
	public void setIsCancelled(Boolean cancelled){
		this.setBoolean("cancelled", cancelled);
	}
	
	public String getClassType(){
		return this.getString("class_type");
	}
	
	public void setClassType(String classType){
		this.setString("class_type", classType);
	}
	
	public String getProgramType(){
		return this.getString("program_type");
	}
	
	public void setProgramType(String programType){
		this.setString("program_type", programType);
	}
	
}
