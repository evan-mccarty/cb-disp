package com.gmail.alexjpbanks14.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("permissions")
public class Permission extends Model{
	
	public String getName(){
		return this.getString("name");
	}
	
	public void setName(String name){
		this.setString("name", name);
	}
	
	public String getHumanName(){
		return this.getString("human_name");
	}
	
	public void setHumanName(String humanName){
		this.setString("human_name", humanName);
	}
	
	public Integer getId(){
		return this.getInteger("id");
	}

}
