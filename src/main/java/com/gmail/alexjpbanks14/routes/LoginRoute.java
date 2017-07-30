package com.gmail.alexjpbanks14.routes;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.exception.session.UserSessionException;
import com.gmail.alexjpbanks14.form.FormElement;
import com.gmail.alexjpbanks14.model.User;
import com.gmail.alexjpbanks14.security.PasswordUtil;
import com.gmail.alexjpbanks14.template.Template;

import spark.Request;
import spark.Response;

public class LoginRoute extends TwigRoute{

	public LoginRoute(Template template) {
		super(template);
	}

	@Override
	public void handle(Request req, Response res, Template template) {
		if(req.requestMethod().equals("GET"))
			handleGet(req, res, template);
		if(req.requestMethod().equals("POST"))
			handlePost(req, res, template);
	}
	
	private void handleGet(Request req, Response res, Template template){
		
	}
	
	private void handlePost(Request req, Response res, Template template){
		FormElement username = new FormElement(req, "username");
		FormElement password = new FormElement(req, "password");
		System.out.println(username.getValue());
		if(username.isEmpty() || password.isEmpty()){
			template.put("hasError", true);
			template.put("errorMessage", "Username/Password cannot be empty");
			return;
		}
		try{
			System.out.println(username.getValue() + ":" + password.getValue());
			System.out.println(PasswordUtil.hashPassword(password.getValue(), User.findByUsername(username.getValue()).getSalt()));
			CBI_TV.getInstance().getUserManager().addUserIfValid(req.session(), username.getValue().trim(), password.getValue().trim());
			System.out.println("thats cool man");
		}catch(UserSessionException e){
			System.out.println(e.getMessage());
			template.put("hasError", true);
			template.put("errorMessage", "Username or password is invalid");
			return;
		}
	}

}
