package com.gmail.alexjpbanks14.classinstance;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.gmail.alexjpbanks14.model.ClassInstance;

public class ClassInstanceUpdater implements Runnable{
	//Timer timer;
	//TimerTask timerTask;
	LinkedHashSet<ClassInstanceAdapter> toUpdate;
	ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
	Set<ScheduledFuture> futures;
	public ClassInstanceUpdater(int threads){
		//timer.schedule(task, time);
		ScheduledExecutorService service = Executors.newScheduledThreadPool(threads);
		futures = new HashSet<>();
		//service.scheduleAtFixedRate(command, initialDelay, period, unit)
	}
	public void addSchedule(long initial, long delay, TimeUnit time){
		ScheduledFuture<?> future = service.scheduleAtFixedRate(this, initial, delay, time);
		futures.add(future);
	}
	public void addScheduleAtTime(ZonedDateTime date, LocalTime time){
		LocalDateTime now = date.toLocalDateTime();
		LocalDateTime future = now.toLocalDate().atStartOfDay().plusNanos(time.toNanoOfDay());
		long delay;
		if(now.isBefore(future))
			delay = now.until(future, ChronoUnit.MILLIS);
		else
			delay = now.until(future.plus(Period.ofDays(1)), ChronoUnit.MILLIS);
		System.out.println(delay / 1000 / 60 / 60);
		addSchedule(delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
	}
	@Override
	public void run() {
		handleRun();
	}
	void handleRun(){
		toUpdate.forEach((a) -> {
			updateClassInstance(a);
		});
	}
	void updateClassInstance(ClassInstanceAdapter adapter){
		List<ClassInstance> classes = new LinkedList<>(adapter.getClasses());
		String classType = adapter.getClassType();
		String programType = adapter.getProgramType();
		List<ClassInstance> existing_classes = ClassInstance.find("class_type = ? AND program_type = ?", classType, programType);
		//TODO not the most efficient, maybe we can use a hashset here?
		//iterator = classes.listIterator();
		existing_classes.removeAll(classes);
		
	}
}
