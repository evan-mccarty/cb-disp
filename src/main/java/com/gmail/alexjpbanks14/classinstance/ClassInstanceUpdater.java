package com.gmail.alexjpbanks14.classinstance;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.events.ClassInstanceUpdateEvent;
import com.gmail.alexjpbanks14.model.ClassInstance;

public class ClassInstanceUpdater{
	//Timer timer;
	//TimerTask timerTask;
	//LinkedHashSet<ClassInstanceAdapter> toUpdate;
	ScheduledExecutorService service;
	Map<Runnable, ScheduledFuture> futures;
	TimeZone zone;
	public ClassInstanceUpdater(ScheduledExecutorService service, TimeZone zone){
		//timer.schedule(task, time);
		//toUpdate = new LinkedHashSet<>();
		this.service = service;
		//service.sch
		futures = new HashMap<>();
		this.zone = zone;
		//service.scheduleAtFixedRate(command, initialDelay, period, unit)
	}
	
	public ClassInstanceUpdaterRunner addSchedule(long initial, long delay, TimeUnit time, List<ClassInstanceAdapter> adapters) {
		ClassInstanceUpdaterRunner runner = new ClassInstanceUpdaterGeneralRunner(adapters);
		addSchedule(initial, delay, time, runner);
		return runner;
	}
	
	public void addSchedule(long initial, long delay, TimeUnit time, ClassInstanceUpdaterRunner runner){
		ScheduledFuture<?> future = service.scheduleAtFixedRate(runner, initial, delay, time);
		futures.put(runner, future);
	}
	public ClassInstanceUpdaterRunner addScheduleAtTime(ZonedDateTime date, LocalTime time, List<ClassInstanceAdapter> adapters) {
		ClassInstanceUpdaterRunner runner = new ClassInstanceUpdaterGeneralRunner(adapters);
		addScheduleAtTime(date, time, runner);
		return runner;
	}
	public void addScheduleAtTime(ZonedDateTime date, LocalTime time, ClassInstanceUpdaterRunner runner){
		LocalDateTime now = date.toLocalDateTime();
		LocalDateTime future = now.toLocalDate().atStartOfDay().plusNanos(time.toNanoOfDay());
		long delay;
		if(now.isBefore(future))
			delay = now.until(future, ChronoUnit.MILLIS);
		else
			delay = now.until(future.plus(Period.ofDays(1)), ChronoUnit.MILLIS);
		addSchedule(delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS, runner);
	}
	public void addScheduleExpire(long delay, ClassInstanceAdapterExpire adapter) {
		ClassInstanceUpdaterExpireRunner runner = new ClassInstanceUpdaterExpireRunner(adapter);
		service.schedule(runner, delay, TimeUnit.MILLISECONDS);
	}
	//public void addToUpdate(ClassInstanceAdapter adapter) {
	//	this.toUpdate.add(adapter);
	//}
	void handleRun(List<ClassInstanceAdapter> adapters){
		CBI_TV.getInstance().connectToDB();
		boolean hasUpdate = false;
		for(ClassInstanceAdapter a : adapters) {
			hasUpdate = hasUpdate || updateClassInstance(a);
		}
		if(hasUpdate) {
			List<ClassInstance> allClasses = ClassInstance.findAll();
			ClassInstanceUpdateEvent classEvent = new ClassInstanceUpdateEvent(allClasses);
			CBI_TV.getInstance().getEventManager().envokeEvent(classEvent);
		}
		CBI_TV.getInstance().closeFromDB();
	}
	boolean updateClassInstance(ClassInstanceAdapter adapter){
		LinkedList<ClassInstance> classes = adapter.getClasses(ZonedDateTime.now(zone.toZoneId()));
		if(classes == null) {
			if(adapter.getException() != null) {
				adapter.getException().printStackTrace();
				adapter.setException(null);
				return false;
			}else {
				System.out.println("Update for adapter not needed!");
				return false;
			}
		}
		boolean doesUpdate = false;
		String classType = adapter.getClassType();
		String programType = adapter.getProgramType();
		ListIterator<ClassInstance> classInstanceList = classes.listIterator();
		Map<String, ClassInstance> instanceMap = ClassInstance.getAllByClassAndProgram(classType, programType);
		while(classInstanceList.hasNext()) {
			ClassInstance instance = classInstanceList.next();
			String instanceId = instance.getInstanceId();
			if(instanceMap.containsKey(instanceId)) {
				ClassInstance existingInstance = instanceMap.get(instanceId);
				if(!instance.equals(existingInstance)) {
					existingInstance.copyFrom(instance);
					existingInstance.saveIt();
					doesUpdate = true;
				}
				classInstanceList.remove();
				instanceMap.remove(instanceId);
			}
		}
		//Delete existing instances not referenced from updated class list
		instanceMap.values().forEach((a) -> {
			a.delete();
		});
		//Create instances not existing in current class db
		classes.forEach((a) -> {
			a.saveIt();
		});
		//classes.forEach((a) -> {
		//	ClassInstance otherInstance = instanceMap.get(a.getInstanceId());
		//	System.out.println(a.equals(otherInstance));
		//});
		//System.out.println(existing_classes.containsAll(classes));
		//Remove all classes because we are not sure how instance id from google calendar or
		//cbi are handled when information is changed
		//ClassInstance.delete("class_type = ? AND program_type = ?", classType, programType);
		//classes.forEach((a) -> {
		//	a.saveIt();
		//});
		return doesUpdate;
	}
	
	abstract class ClassInstanceUpdaterRunner implements Runnable {
		
		List<ClassInstanceAdapter> adapters;
		
		public ClassInstanceUpdaterRunner(List<ClassInstanceAdapter> adapters) {
			//updaters = new LinkedList<>();
			this.adapters = adapters;
		}
		
		@Override
		public void run() {
			try {
				runUpdater();
			}catch(Throwable e) {
				e.printStackTrace();
			}
			//ClassInstanceUpdater.this.handleRun(adapters);
		}
			
		public abstract void runUpdater() throws Throwable;
		
		public List<ClassInstanceAdapter> getAdapters() {
			return adapters;
		}

		public void setAdapters(List<ClassInstanceAdapter> adapters) {
			this.adapters = adapters;
		}

	}
	
	class ClassInstanceUpdaterGeneralRunner extends ClassInstanceUpdaterRunner {

		public ClassInstanceUpdaterGeneralRunner(List<ClassInstanceAdapter> adapters) {
			super(adapters);
		}

		@Override
		public void runUpdater() throws Throwable {
			ClassInstanceUpdater.this.handleRun(adapters);
		}
		
	}
	
	class ClassInstanceUpdaterExpireRunner extends ClassInstanceUpdaterRunner{
		
		ClassInstanceAdapterExpire adapter;
		
		public ClassInstanceUpdaterExpireRunner(ClassInstanceAdapterExpire adapter) {
			super(Collections.singletonList(adapter));
			this.adapter = adapter;
		}

		@Override
		public void runUpdater() throws Throwable {
			//TODO fix this so it wont create array each time, probably use super class
			Throwable throwable = null;
			try {
				ClassInstanceUpdater.this.handleRun(this.adapters);
			}catch(Throwable t) {
				throwable = t;
			}
			ZonedDateTime currentTime = ZonedDateTime.now(zone.toZoneId());
			ZonedDateTime nextTime = adapter.getNextUpdate().plus(adapter.getOffset());
			long offset = currentTime.until(nextTime, ChronoUnit.MILLIS);
			ScheduledFuture future = ClassInstanceUpdater.this.service.schedule(this, offset, TimeUnit.MILLISECONDS);
			ClassInstanceUpdater.this.futures.replace(this, future);
			if(throwable != null)
				throw throwable;
		}
		
	}
}
