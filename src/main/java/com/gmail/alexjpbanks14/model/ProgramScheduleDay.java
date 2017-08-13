package com.gmail.alexjpbanks14.model;

import java.sql.Date;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.Table;

@BelongsTo(parent = ProgramSchedule.class, foreignKeyName="program_schedule_id")
@Table("program_schedule_days")
public class ProgramScheduleDay extends Model{
	
	public Date getProgramDate() {
		return this.getDate("program_date");
	}
	
	public void setProgramDate(Date programDate) {
		this.setDate("program_date", programDate);
	}
	
	public ProgramSchedule getProgramSchedule() {
		return this.parent(ProgramSchedule.class);
	}
	
	public void setProgramSchedule(ProgramSchedule programSchedule) {
		this.setParent(programSchedule);
	}
	
}
