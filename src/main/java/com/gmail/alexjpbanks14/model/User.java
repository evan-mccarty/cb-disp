package com.gmail.alexjpbanks14.model;

import java.util.List;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

import com.gmail.alexjpbanks14.security.PasswordUtil;

@BelongsTo(parent = UserGroup.class, foreignKeyName="user_ibfk_1")
@Table("users")
public class User extends Model{

	public String getUsername(){
		return this.getString("username");
	}
	public void setUsername(String username){
		this.setString("username", username);
	}
	
	public String getPassword(){
		return this.getString("password");
	}
	public void Password(String password){
		this.setString("password", password);
	}
	
	public Integer getId(){
		return this.getInteger("id");
	}
	
	public void setSalt(String salt){
		this.setString("salt", salt);
	}
	
	public String getSalt(){
		return this.getString("salt");
	}
	
	public void setPassword(String password, String salt){
		Password(PasswordUtil.hashPassword(password, salt));
		setSalt(salt);
	}
	
	public void setPassword(String password){
		setPassword(password, PasswordUtil.getSecureSalt());
	}
	
	public static User findByUsername(String username){
		return User.findFirst("username = ?", username);
	}
	
	public UserGroup getUserGroup(){
		return this.parent(UserGroup.class);
	}
	
	public void setUserGroup(UserGroup group){
		this.setParent(group);
	}
	
	public LazyList<Permission> getAllPermissions(){
		return this.getAll(Permission.class);
	}
	
	public boolean hasPermission(Permission permission){
		return this.getAllPermissions().contains(permission);
	}
	
	public boolean hasPermission(String permission){
		return this.get(Permission.class, "name = ?", permission).size() > 0;
	}
	
}
