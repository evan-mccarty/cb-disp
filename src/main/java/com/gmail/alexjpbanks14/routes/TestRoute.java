package com.gmail.alexjpbanks14.routes;

import com.gmail.alexjpbanks14.template.Template;

import spark.Request;
import spark.Response;

public class TestRoute extends TwigRoute{

	public TestRoute(String path) {
		super(path);
	}
	
	public TestRoute(Template template){
		super(template);
	}

	@Override
	public void handle(Request req, Response res, Template template) {
		
	}

}
