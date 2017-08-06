package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Model;

public class ClassInstance extends Model{
	
	public ClassInstance() {
		this.setIsVisisble(true);
		this.setIsCancelled(false);
	}
	
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
	
	public static Map<String, ClassInstance> getAllByClassAndProgram(String clazzType, String programType){
		List<ClassInstance> instances = ClassInstance.find("class_type = ? AND program_type = ?", clazzType, programType);
		Map<String, ClassInstance> instanceMap = new HashMap<>(instances.size());
		for(ClassInstance instance : instances) {
			instanceMap.put(instance.getInstanceId(), instance);
		}
		return instanceMap;
	}
	
	public static final String[] equalsValues = new String[] {"instance_id", "type", "start_date", "location", "enrollees", "instructor_last", "instructor_first", "visible", "cancelled", "class_type", "program_type"};
	
	@Override
	public boolean equals(Object o){
		if(o instanceof ClassInstance) {
			ClassInstance alt = (ClassInstance)o;
			for(String value : equalsValues) {
				Object value1 = this.get(value);
				Object value2 = alt.get(value);
				if((value1 == null && value2 != null) || (value1 != null && value2 == null))
					return false;
				if((value1 != null && value2 != null) && !value1.equals(value2))
					return false;
			}
			return true;
		}
		return false;
	}
	
	/*@Override
	public int hashCode(){
		return this.getInstanceId().hashCode();
	}*/
	
	
}
