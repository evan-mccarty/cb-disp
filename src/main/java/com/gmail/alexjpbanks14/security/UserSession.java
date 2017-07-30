package com.gmail.alexjpbanks14.security;

import com.gmail.alexjpbanks14.model.User;

public class UserSession{
	private String id;
	private User user;
	public UserSession(String id, User user) {
		super();
		this.id = id;
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}