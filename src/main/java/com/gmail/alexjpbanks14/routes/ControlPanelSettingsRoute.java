package com.gmail.alexjpbanks14.routes;

import com.gmail.alexjpbanks14.template.Template;

import spark.Request;
import spark.Response;

public class ControlPanelSettingsRoute extends TwigRoute{

	public ControlPanelSettingsRoute(Template template) {
		super(template);
	}

	@Override
	public void handle(Request req, Response res, Template template) {
	}

}
