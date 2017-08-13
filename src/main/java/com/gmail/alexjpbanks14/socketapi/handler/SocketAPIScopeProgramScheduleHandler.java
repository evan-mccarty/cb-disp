package com.gmail.alexjpbanks14.socketapi.handler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.model.ProgramSchedule;
import com.gmail.alexjpbanks14.model.ProgramScheduleDay;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeHandler;

public class SocketAPIScopeProgramScheduleHandler extends SocketAPIScopeHandler{

	public SocketAPIScopeProgramScheduleHandler(SocketAPISession session) {
		super(session);
	}
	
	public Object getProgramSchedulesForMonth(Integer year, Integer month) {
		CBI_TV.getInstance().connectToDB();
		LocalDate date = LocalDate.of(year, month + 1, 1);
		LocalDate dateMax = date.plusMonths(1);
		System.out.println(date + ":" + year);
		HashMap<String, Object> responseMap = new HashMap<>();
		Object result = ProgramScheduleDay.find("program_date >= ? AND program_date < ?", Date.valueOf(date), Date.valueOf(dateMax)).toMaps();
		responseMap.put("class_dates", result);
		Object programs = ProgramSchedule.findAll().toMaps();
		responseMap.put("class_programs", programs);
		CBI_TV.getInstance().closeFromDB();
		return responseMap;
	}
	
	public static final int PROGRAM_CREATE_GROUP = 0;
	public static final int PROGRAM_SAVE_GROUP = 1;
	public static final int PROGRAM_DELETE_GROUP = 2;
	public static final int PROGRAM_REMOVE_GROUP = 3;
	
	public void saveProgramSchedules(ArrayList<String> days, HashMap<String, Object> schedules, Integer action) {
		CBI_TV.getInstance().connectToDB();
		ProgramSchedule schedule;
		switch(action) {
		case SocketAPIScopeProgramScheduleHandler.PROGRAM_CREATE_GROUP:
			schedule = new ProgramSchedule();
			schedule.parseMapValues(schedules);
			schedule.saveIt();
			updateDateValues(fixDateValues(days), schedule);
			break;
		case PROGRAM_SAVE_GROUP:
			schedule = ProgramSchedule.findById(schedules.get("calendar_id"));
			schedule.parseMapValues(schedules);
			schedule.saveIt();
			break;
		case PROGRAM_DELETE_GROUP:
			schedule = ProgramSchedule.findById(schedules.get("calendar_id"));
			schedule.deleteCascade();
		}
		/*for(HashMap<String, Object> o : values) {
			ProgramSchedule schedule = new ProgramSchedule();
			schedule.fromMap(o);
			ProgramSchedule existing = ProgramSchedule.findFirst("program_date = ?", schedule.getProgramDate());
			if(existing != null) {
				existing.copyFrom(schedule);
				existing.saveIt();
			}else {
				schedule.saveIt();
			}
		}*/
		CBI_TV.getInstance().closeFromDB();
	}
	
	private List<LocalDate> fixDateValues(ArrayList<String> list) {
		return list.stream().map((a) -> {return ZonedDateTime.parse(a, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();}).collect(Collectors.toList());
	}
	
	private ProgramScheduleDay findExisting(LocalDate date) {
		Date typeDate = Date.valueOf(date);
		//program.parseMapValues(colvals);
		return ProgramScheduleDay.findFirst("program_date = ?", typeDate);
		
	}
	
	private void updateDateValues(List<LocalDate> datelist, ProgramSchedule programschedule) {
		System.out.println(datelist);
		for(LocalDate date : datelist) {
			ProgramScheduleDay existing = findExisting(date);
			if(existing != null) {
				existing.setProgramSchedule(programschedule);
				existing.saveIt();
				System.out.println(existing.getProgramDate());
			}else {
				ProgramScheduleDay program = new ProgramScheduleDay();
				program.setProgramSchedule(programschedule);
				program.setProgramDate(Date.valueOf(date));
				program.saveIt();
				System.out.println("derp2");
			}
		}
	}
	
	private void deleteDateValues(List<LocalDate> datelist) {
		
	}

}
