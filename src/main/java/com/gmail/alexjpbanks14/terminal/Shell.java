package com.gmail.alexjpbanks14.terminal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.gmail.alexjpbanks14.terminal.ShellEvent.ShellEventType;

public class Shell {
	
	private Scanner input;
	private Scanner error;
	private PrintWriter output;
	
	private Runtime runtime;
	
	private Process shellInstance;
	private Thread outputListener;
	private Thread errorListener;
	private Thread shutdownListener;
	
	private List<ShellEventHandler> listeners;
	
	public Shell(String commandName, String[] envp, File dir, Runtime runtime) throws IOException{
		this.runtime = runtime;
		shellInstance = runtime.exec(commandName, envp, dir);
		input = new Scanner(shellInstance.getInputStream());
		error = new Scanner(shellInstance.getErrorStream());
		output = new PrintWriter(shellInstance.getOutputStream());
		listeners = new LinkedList<>();
		makeThreadListeners();
	}
	
	public Shell(String commandName) throws IOException{
		this(commandName, null, null, Runtime.getRuntime());
	}
	
	private void makeThreadListeners(){
		outputListener = new ScannerListener(input, ShellEventType.OUTPUT);
		errorListener = new ScannerListener(error, ShellEventType.ERROR);
		shutdownListener = new Thread(){
			@Override
			public void run(){
				Shell.this.close();
			}
		};
		runtime.addShutdownHook(shutdownListener);
	}
	
	public void startThreadListeners(){
		outputListener.start();
		errorListener.start();
	}
	
	private void shellEvent(ShellEvent event){
		listeners.forEach((a) -> {
			if(event.getType().equals(a.getType()))
				a.handleEvent(event);
		});
	}
	
	public void close(){
		outputListener.interrupt();
		errorListener.interrupt();
		input.close();
		error.close();
		output.close();
	}
	
	public void addShellEventHandler(ShellEventHandler handler){
		listeners.add(handler);
	}
	
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler){
		outputListener.setUncaughtExceptionHandler(handler);
		errorListener.setUncaughtExceptionHandler(handler);
		shutdownListener.setUncaughtExceptionHandler(handler);
	}
	
	public PrintWriter getPrintWriter(){
		return output;
	}
	
	class ScannerListener extends Thread{
		private Scanner scanner;
		private ShellEventType type;
		public ScannerListener(Scanner scanner, ShellEventType type) {
			super();
			this.scanner = scanner;
			this.type = type;
		}
		public Scanner getScanner() {
			return scanner;
		}
		public void setScanner(Scanner scanner) {
			this.scanner = scanner;
		}
		public ShellEventType getType() {
			return type;
		}
		public void setType(ShellEventType type) {
			this.type = type;
		}
		@Override
		public void run(){
			while(this.isAlive()){
				String line = scanner.next();
				ShellEvent event = new ShellEvent(line, type);
				Shell.this.shellEvent(event);
			}
		}
	}
	
}
