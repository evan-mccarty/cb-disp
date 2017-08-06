package com.gmail.alexjpbanks14.model;

import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("restriction_types")
public class RestrictionType extends Model{
	
	public Integer getId() {
		return this.getInteger("id");
	}
	
	public String getTitle() {
		return this.getString("title");
	}
	
	public void setTitle(String Title) {
		this.setString("title", Title);
	}
	
	public Integer getPosition() {
		return this.getInteger("position");
	}
	
	public void setPosition(Integer Position) {
		this.setInteger("position", Position);
	}
	
	public static List<Map<String, Object>> getGsonArray(){
		LazyList<RestrictionType> list = RestrictionType.findAll().include(Restriction.class);
		list.forEach((a) -> {
			a.getAll(Restriction.class).forEach((b) -> {
				b.getLatestState();
			});
		});
		return list.toMaps();
	}
	
}
