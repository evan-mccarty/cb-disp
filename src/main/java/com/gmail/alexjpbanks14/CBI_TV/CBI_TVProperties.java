package com.gmail.alexjpbanks14.CBI_TV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class CBI_TVProperties {
	
	static Properties file;
	
	//public CBI_TVProperties(){
	//public void init(){
	//	file = new Properties();
	//}
	//}
	
	public static boolean loadFromFile(String input){
		return loadFromFile(new File(input));
	}
	
	public static boolean loadFromFile(File input){
		file = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(input);
			file.load(in);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isNull(String name){
		return file.getProperty(name) == null;
	}
	
	public static String getString(String name){
		return file.getProperty(name);
	}
	
	public static Integer getInteger(String name){
		if(isNull(name))
			return null;
		else
			return Integer.parseInt(file.getProperty(name));
	}
	
	public static Boolean getBoolean(String name){
		if(isNull(name))
			return null;
		else
			return Boolean.parseBoolean(file.getProperty(name));
	}
	
	public static String getSQLDriver(){
		return getString("sqldriver");
	}
	
	public static String getSQLHost(){
		return getString("sqlhost");
	}
	
	public static String getSQLUser(){
		return getString("sqluser");
	}
	
	public static String getSQLPassword(){
		return getString("sqlpassword");
	}
	
	public static Integer getPort(){
		return getInteger("port");
	}
	
	public static boolean getDisableTemplateCache(){
		return getBoolean("disablecache");
	}
	
	public static boolean useSecure(){
		return getBoolean("usesecure");
	}
	
	public static String getSecureJKS(){
		return getString("securejks");
	}
	
	public static String getSecurePass(){
		return getString("securepass");
	}
	
}
