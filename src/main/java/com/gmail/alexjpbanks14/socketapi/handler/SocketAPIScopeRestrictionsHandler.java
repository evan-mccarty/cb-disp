package com.gmail.alexjpbanks14.socketapi.handler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.events.RestrictionStateHistoryUpdateEvent;
import com.gmail.alexjpbanks14.events.RestrictionsUpdateEvent;
import com.gmail.alexjpbanks14.model.Restriction;
import com.gmail.alexjpbanks14.model.RestrictionState;
import com.gmail.alexjpbanks14.model.RestrictionType;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeRestrictionsHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeRestrictionsHandler(SocketAPISession session) {
		super(session);
	}
	
	public Object getRestrictions() {
		CBI_TV.getInstance().connectToDB();
		Object result = RestrictionType.getGsonArray();
		CBI_TV.getInstance().closeFromDB();
		return result;
	}
	
	public Object getRestrictionStatesForDay() {
		CBI_TV.getInstance().connectToDB();
		Object result = RestrictionState.getGsonRestrictionStatesForDay();
		CBI_TV.getInstance().closeFromDB();
		return result;
	}
	
	public void saveRestrictions(HashMap<Object, Object> restrictions) {
		System.out.println(restrictions);
		CBI_TV.getInstance().connectToDB();
		for(Object id : restrictions.keySet()) {
			Map<String, Object> valueMaps = (Map<String, Object>) restrictions.get(id);
			Boolean val = Boolean.parseBoolean(valueMaps.get("status").toString());
			Boolean is_custom = Boolean.parseBoolean(valueMaps.get("is_custom").toString());
			Restriction restriction = Restriction.findById(id);
			if(is_custom && restriction.getRestrictionClass().equals(Restriction.RESTRICTION_CLASS_TYPE_CUSTOM)) {
				String custom_text = valueMaps.get("custom").toString();
				restriction.setDisplayText(custom_text);
				restriction.saveIt();
			}
			restriction.updateRestrictionStatus(val, ZonedDateTime.now());
		}
		Object result = RestrictionType.getGsonArray();
		RestrictionsUpdateEvent updateEvent = new RestrictionsUpdateEvent(result);
		CBI_TV.getInstance().getEventManager().envokeEvent(updateEvent);
		Object result2 = RestrictionState.getGsonRestrictionStatesForDay();
		RestrictionStateHistoryUpdateEvent stateHistoryEvent = new RestrictionStateHistoryUpdateEvent(result2);
		CBI_TV.getInstance().getEventManager().envokeEvent(stateHistoryEvent);
		CBI_TV.getInstance().closeFromDB();
	}

}
