package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

@BelongsTo(parent = Restriction.class, foreignKeyName="restriction_id")
@Table("restriction_states")
public class RestrictionState extends Model{
	
	public static List<Map<String, Object>> getGsonRestrictionStatesForDay(){
		ZonedDateTime now = ZonedDateTime.now();
		Timestamp today = Timestamp.from(now.toLocalDate().atStartOfDay().toInstant(now.getOffset()));
		Timestamp tomorrow = Timestamp.from(now.plusDays(1).toLocalDate().atStartOfDay().toInstant(now.getOffset()));
		return RestrictionState.find("start_time > ? AND start_time < ?", today, tomorrow).include(Restriction.class).toMaps();
	}
	
	public Integer getId() {
		return this.getInteger("id");
	}
	
	public Restriction getRestriction() {
		return this.parent(Restriction.class);
	}
	
	public void setRestriction(Restriction Restriction) {
		this.setParent(Restriction);
	}
	
	public Timestamp getStartTime() {
		return this.getTimestamp("start_time");
	}
	
	public void setStartTime(Timestamp startTime) {
		this.setTimestamp("start_time", startTime);
	}
	
	public Timestamp getEndTime() {
		return this.getTimestamp("end_time");
	}
	
	public void setEndTime(Timestamp EndTime) {
		this.setTimestamp("end_time", EndTime);
	}
	
	public Boolean getIsEnded() {
		return this.getBoolean("is_ended");
	}
	
	public void setIsEnded(Boolean IsEnded) {
		this.setBoolean("is_ended", IsEnded);
	}
	
}
