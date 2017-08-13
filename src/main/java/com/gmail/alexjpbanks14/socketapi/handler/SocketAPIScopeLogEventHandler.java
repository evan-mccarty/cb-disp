package com.gmail.alexjpbanks14.socketapi.handler;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.model.LogEvent;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeLogEventHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeLogEventHandler(SocketAPISession session) {
		super(session);
	}
	
	public Object getLogEventsWithSearch(String search,  String orderBy, Integer page, Integer perPage) {
		CBI_TV.getInstance().connectToDB();
		Object result = LogEvent.getLogEventsGson(orderBy, search, perPage, page*perPage);
		CBI_TV.getInstance().closeFromDB();
		return result;
	}

}
