package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import com.gmail.alexjpbanks14.logevent.EventActionGroup;
import com.gmail.alexjpbanks14.logevent.EventActionType;

@BelongsTo(parent = User.class, foreignKeyName="user_id")
@Table("log_events")
public class LogEvent extends Model{
	
	public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
	public static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ISO_LOCAL_TIME;
	
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
	
	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> result = super.toMap();
		Timestamp time = (Timestamp) result.get("event_time");
		LocalDateTime dateTime = time.toLocalDateTime();
		result.put("event_date", dateTime.format(FORMAT_DATE));
		result.put("event_time", dateTime.format(FORMAT_TIME));
		if(result.containsKey("user")) {
			result.put("user_name", ((Map<String, Object>)result.get("user")).get("username"));
			result.remove("user");
		}
		return result;
		
	}
	
	public static Map<String, Object> getLogEventsGson(String column_name, String search, Integer limit, Integer offset){
		//Integer perPage = 20;
		//Integer offset = page * perPage;
		Map<String, Object> resp = new HashMap<String, Object>();
		//String column_name = "butt";
		String searchColumn;
		if(column_name.equals("user")) {
			searchColumn = "users.username";
		}else if(column_name.equals("event_type")) {
			searchColumn = "log_events.event_time";
			LocalDateTime datetime = LocalDateTime.parse(search);
		}else if(column_name.equals("group")) {
			searchColumn = "log_events.action_group";
		}else if(column_name.equals("type")) {
			searchColumn = "log_events.action_type";
		}else if(column_name.equals("action")) {
			searchColumn = "log_events.event_action";
		}else {
			throw new RuntimeException("Invalid column name or value");
		}
		String searchQueries = " FROM log_events INNER JOIN users ON users.id = log_events.user_id AND " + searchColumn + " LIKE ?";
		String searchLike = search + "%";
		List<Map> values = Base.findAll("SELECT log_events.*,users.username" + searchQueries + " ORDER BY log_events.event_time LIMIT ? OFFSET ?", searchLike, limit, offset);
		for(Map<String, Object> value : values) {
			if(value.containsKey("event_time")) {
				Timestamp event_Time = (Timestamp) value.get("event_time");
				LocalDateTime dateTime = event_Time.toLocalDateTime();
				value.put("event_date", dateTime.format(FORMAT_DATE));
				value.put("event_time", dateTime.format(FORMAT_TIME));
			}
		}
		Number number = (Number) Base.firstCell("SELECT COUNT(*)" + searchQueries, searchLike);
		resp.put("respvalues", values);
		resp.put("pageCounts", number);
		return resp;
		//List<Map<String, Object>> maps = LogEvent.findAll().limit(limit).offset(offset).toMaps();
	}
	
}
