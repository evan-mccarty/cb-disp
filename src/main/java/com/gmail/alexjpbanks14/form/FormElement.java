package com.gmail.alexjpbanks14.form;

import spark.Request;

public class FormElement {
	
	private String name;
	private String value;
	
	public FormElement(Request req, String name){
		this.name = name;
		value = req.queryParams(name);
	}
	
	public boolean isEmpty(){
		return value == null ? true : value.trim().length() == 0;
	}
	
	public String getName(){
		return name;
	}
	
	public String getValue(){
		return value;
	}
	
	public Integer intValue(){
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Double doubleValue(){
		try{
			return Double.parseDouble(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Float floatValue(){
		try{
			return Float.parseFloat(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Long longValue(){
		try{
			return Long.parseLong(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Short shortValue(){
		try{
			return Short.parseShort(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Byte byteValue(){
		try{
			return Byte.parseByte(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	public Boolean booleanValue(){
		try{
			return Boolean.parseBoolean(value);
		}catch(NumberFormatException e){
			return null;
		}
	}
	
}
