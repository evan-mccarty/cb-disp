package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import com.gmail.alexjpbanks14.logevent.EventActionGroup;
import com.gmail.alexjpbanks14.logevent.EventActionType;

@BelongsTo(parent = User.class, foreignKeyName="user_id")
@Table("log_events")
public class LogEvent extends Model{
	
	public Integer getId() {
		return this.getInteger("id");
	}
	
	public User getUser() {
		return this.parent(User.class);
	}
	
	public void setUser(User user) {
		this.setParent(user);
	}
	
	public Timestamp getTimeEvent() {
		return this.getTimestamp("event_time");
	}
	
	public void setTimeEvent(Timestamp timeEvent) {
		this.setTimestamp("event_time", timeEvent);
	}
	
	public EventActionGroup getActionGroup() {
		return EventActionGroup.valueOf(this.getString("action_group"));
	}
	
	public void setActionGroup(EventActionGroup actionGroup) {
		this.setString("action_group", actionGroup.name());
	}
	
	public EventActionType getActionType() {
		return EventActionType.valueOf(this.getString("action_type"));
	}
	
	public void setActionType(EventActionType actionType) {
		this.setString("action_type", actionType.name());
	}
	
	public String getActionEvent() {
		return this.getString("event_action");
	}
	
	public void setActionEvent(String actionEvent) {
		this.setString("event_action", actionEvent);
	}
	
	public static List<Map<String, Object>> getLogEventsGson(Integer page){
		Integer perPage = 20;
		Integer offset = page * perPage;
		return LogEvent.findAll().limit(perPage).offset(offset).toMaps();
	}
	
}
