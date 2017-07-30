package com.gmail.alexjpbanks14.classinstance;

import java.util.Set;

import com.gmail.alexjpbanks14.model.ClassInstance;

public abstract class ClassInstanceAdapter {
	
	//public String getCreationType();
	private boolean hasException;
	private Exception exception;
	private String classType;
	private String programType;
	
	protected ClassInstanceAdapter(String classType, String programType){
		this.hasException = false;
		this.exception = null;
		this.classType = classType;
		this.programType = programType;
	}
	
	protected void setException(Exception e){
		this.hasException = true;
		this.exception = e;
	}

	public boolean hasException() {
		return hasException;
	}

	public Exception getException() {
		return exception;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}
	
	public abstract Set<ClassInstance> getClasses();
	
}
