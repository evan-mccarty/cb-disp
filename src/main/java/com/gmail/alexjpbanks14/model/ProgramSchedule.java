package com.gmail.alexjpbanks14.model;

import java.awt.Color;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("program_schedules")
public class ProgramSchedule extends Model{
	
	public Time getJpStart() {
		return this.getTime("jp_start");
	}
	
	public void setJpStart(Time jpStart) {
		this.setTime("jp_start", jpStart);
	}
	
	public Time getJpEnd() {
		return this.getTime("jp_end");
	}
	
	public void setJpEnd(Time jpEnd) {
		this.setTime("jp_end", jpEnd);
	}
	
	public Boolean getJpEnabled() {
		return this.getBoolean("jp_enabled");
	}
	
	public void setJpEnabled(Boolean jpEnabled) {
		this.setBoolean("jp_enabled", jpEnabled);
	}
	
	public Time getApStart() {
		return this.getTime("ap_start");
	}
	
	public void setApStart(Time apStart) {
		this.setTime("ap_start", apStart);
	}
	
	public Time getApEnd() {
		return this.getTime("ap_end");
	}
	
	public void setApEnd(Time apEnd) {
		this.setTime("ap_end", apEnd);
	}
	
	public Boolean getApEnabled() {
		return this.getBoolean("ap_enabled");
	}
	
	public void setApEnabled(Boolean apEnabled) {
		this.setBoolean("ap_enabled", apEnabled);
	}
	
	public Boolean getApEndSunset() {
		return this.getBoolean("ap_end_sunset");
	}
	
	public void setApEndSunset(Boolean apEndSunset) {
		this.setBoolean("ap_end_sunset", apEndSunset);
	}
	
	public String getGroupColor() {
		return this.getString("program_color");
	}
	
	public void setGroupColor(String color) {
		this.setString("program_color", color);
	}
	
	public String getGroupName() {
		return this.getString("program_text");
	}
	
	public void setGroupName(String name) {
		this.setString("program_text", name);
	}
	
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("kk:mm");
	
	public static final String DEFAULT_COLOR = "#00FFFF";
	public static final String DEFAULT_NAME = "New Group";
	
	public void parseMapValues(Map<String, Object> map) {
		System.out.println(map.get("jp_start"));
		LocalTime time = LocalTime.parse(map.get("jp_start").toString());
		this.setJpEnabled(Boolean.parseBoolean(map.get("jp_enabled").toString()));
		this.setJpStart(parseTime(map.get("jp_start")));
		this.setJpEnd(parseTime(map.get("jp_end")));
		this.setApEnabled(Boolean.parseBoolean(map.get("ap_enabled").toString()));
		this.setApStart(parseTime(map.get("ap_start")));
		this.setApEnd(parseTime(map.get("ap_end")));
		this.setApEndSunset(Boolean.parseBoolean(map.get("ap_sunset_end").toString()));
		try {
			Color.decode(map.get("group_color").toString());
			this.setGroupColor(map.get("group_color").toString());
		}catch(Exception e) {
			this.setGroupColor(DEFAULT_COLOR);
		}
		if(!map.containsKey("group_name") || map.get("group_name").toString().trim().length() == 0) {
			this.setGroupName(DEFAULT_NAME);
		}else {
			this.setGroupName(map.get("group_name").toString());
		}
	}
	
	@Override
	public Map<String, Object> toMap(){
		Map<String, Object> values = super.toMap();
		values.put("jp_start", this.getJpStart().toLocalTime().format(TIME_FORMATTER));
		values.put("jp_end", this.getJpEnd().toLocalTime().format(TIME_FORMATTER));
		values.put("ap_start", this.getApStart().toLocalTime().format(TIME_FORMATTER));
		values.put("ap_end", this.getApEnd().toLocalTime().format(TIME_FORMATTER));
		return values;
	}
	
	public Time parseTime(Object object) {
		return Time.valueOf(LocalTime.parse(object.toString()));
	}
	
}
