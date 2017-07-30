package com.gmail.alexjpbanks14.routes;

import com.gmail.alexjpbanks14.template.Template;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class TwigRoute implements Route{
	
	Template template;
	
	public TwigRoute(String path){
		this(new Template(path));
	}
	
	public TwigRoute(Template template){
		this.template = template;
	}
	
	@Override
	public Object handle(Request req, Response res) throws Exception {
		this.handle(req, res, template);
		return template.render();
	}
	
	public abstract void handle(Request req,
			Response res, Template template);

}
