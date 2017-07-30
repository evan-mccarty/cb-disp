package com.gmail.alexjpbanks14.model;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;
import org.javalite.activejdbc.annotations.Table;

@Table("user_groups")
@Many2Many(other = Permission.class, join = "user_group_permissions", sourceFKName = "user_group_id", targetFKName = "permission_id")
public class UserGroup extends Model{
	
	public String getName(){
		return this.getString("group_name");
	}
	
	public void setName(String name){
		this.setString("group_name", name);
	}
	
	public Integer getId(){
		return this.getInteger("id");
	}
	
	public List<Permission> getAllPermissions(){
		return this.getAll(Permission.class);
	}
	
	public boolean hasPermission(Permission permission){
		return this.getAllPermissions().contains(permission);
	}
	
	public boolean hasPermission(String permission){
		return this.get(Permission.class, "name = ?", permission).size() > 0;
	}
	
}