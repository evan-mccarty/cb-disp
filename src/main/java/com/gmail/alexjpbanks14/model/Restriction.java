package com.gmail.alexjpbanks14.model;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

@BelongsTo(parent = RestrictionType.class, foreignKeyName="restriction_type_id")
@Table("restrictions")
public class Restriction extends Model{
	
	public static final String RESTRICTION_CLASS_TYPE_STANDARD = "STANDARD";
	
	public static final String RESTRICTION_CLASS_TYPE_CUSTOM = "CUSTOM";
	
	public Integer getId() {
		return this.getInteger("id");
	}
	
	public RestrictionType getRestrictionType() {
		return this.parent(RestrictionType.class);
	}
	
	public void setRestrictionType(RestrictionType type) {
		this.setParent(type);
	}
	
	public String getButtonText() {
		return this.getString("button_text");
	}
	
	public void setButtonText(String buttonText) {
		this.setString("button_text", buttonText);
	}
	
	public String getDisplayText() {
		return this.getString("display_text");
	}
	
	public void setDisplayText(String displayText) {
		this.setString("display_text", displayText);
	}
	
	public String getRestrictionClass() {
		return this.getString("class");
	}
	
	public void setRestrictionClass(String restrictionClazz) {
		this.setString("class", restrictionClazz);
	}
	
	public Integer getPosition() {
		return this.getInteger("position");
	}
	
	public void setPosition(Integer Position) {
		this.setInteger("position", Position);
	}
	
	
	
	public RestrictionState getLatestState() {
		LazyList<Model> list = this.get(RestrictionState.class, null).orderBy("start_time desc").limit(1);
		if(list.size() > 0)
			return (RestrictionState) list.get(0);
		return null;
	}
	
	@Override
	public Map<String, Object> toMap(){
		Map<String, Object> toMap = super.toMap();
		RestrictionState latest = this.getLatestState();
		if(latest != null)
			toMap.put("restriction_state", latest.toMap());
		return toMap;
	}
	
	public void updateRestrictionStatus(Boolean status, ZonedDateTime time) {
		RestrictionState latestState = getLatestState();
		Timestamp currentTimestamp = Timestamp.from(time.toInstant());
		if(latestState == null) {
			latestState = new RestrictionState();
			latestState.setStartTime(currentTimestamp);
			latestState.setIsEnded(!status);
			latestState.setRestriction(this);
			if(!status) {
				latestState.setEndTime(currentTimestamp);
			}
		}else {
			if(!latestState.getIsEnded()) {
				if(status) {
					//Not sure what to do here yet
				}else {
					latestState.setIsEnded(true);
					latestState.setEndTime(currentTimestamp);
				}
			}else {
				if(status) {
					latestState = new RestrictionState();
					latestState.setRestriction(this);
					latestState.setStartTime(currentTimestamp);
					latestState.setIsEnded(false);
				}else {
					//Not sure hwat to dos here yst
				}
			}
		}
		latestState.saveIt();
	}
	
	public Map<String, Object> toGsonObject(){
		System.out.println("happen");
		//TODO not the most efficient usage of orm probably can minimize db calls
		Map<String, Object> map = this.toMap();
		//map.put("restriction_type", this.getRestrictionType().toMap());
		//map.put("restriction_status", this.getLatestState().toMap());
		return map;
	}
	
}
