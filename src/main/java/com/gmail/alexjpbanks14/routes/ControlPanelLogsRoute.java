package com.gmail.alexjpbanks14.routes;

import com.gmail.alexjpbanks14.template.Template;

import spark.Request;
import spark.Response;

public class ControlPanelLogsRoute extends TwigRoute{

	public ControlPanelLogsRoute(Template template) {
		super(template);
	}

	@Override
	public void handle(Request req, Response res, Template template) {
	}

}
