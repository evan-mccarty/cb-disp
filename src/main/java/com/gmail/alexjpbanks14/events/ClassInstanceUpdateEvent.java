package com.gmail.alexjpbanks14.events;

import java.util.List;

import com.gmail.alexjpbanks14.model.ClassInstance;

public class ClassInstanceUpdateEvent extends Event{
	
	List<ClassInstance> classes;
	
	public ClassInstanceUpdateEvent(List<ClassInstance> classes) {
		super(EventType.CLASS_INSTANCE_UPDATE);
		this.classes = classes;
	}
	
	public List<ClassInstance> getClasses(){
		return classes;
	}

}
