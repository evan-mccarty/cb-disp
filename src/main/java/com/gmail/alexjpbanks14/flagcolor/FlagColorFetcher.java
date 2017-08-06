package com.gmail.alexjpbanks14.flagcolor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TV;
import com.gmail.alexjpbanks14.events.FlagColorUpdateEvent;

public class FlagColorFetcher implements Runnable{
	
	URL url;
	FlagColor flag;
	ScheduledFuture future;
	
	public static final String REQUEST_METHOD = "GET";
	public static final int REQUEST_TIMEOUT = 2000;
	public static final String CHARSET = "UTF-8";
	public static final Pattern JS_REGEX = Pattern.compile("var FLAG_COLOR = \"(\\S)\";");
	
	public FlagColorFetcher(ScheduledExecutorService service, URL url, Duration delay) {
		this.url = url;
		future = service.scheduleAtFixedRate(this, 0, delay.toMillis(), TimeUnit.MILLISECONDS);;
	}
	
	public void fetchFlagColor() throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		connection.setRequestMethod(REQUEST_METHOD);
		connection.setConnectTimeout(REQUEST_TIMEOUT);
		InputStream input = connection.getInputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while((length = input.read(buffer)) != -1) {
			result.write(buffer, 0, length);
		}
		String lines = result.toString(CHARSET);
		Matcher matcher = JS_REGEX.matcher(lines);
		if(matcher.find()) {
			String flagLetter = matcher.group(1);
			char c = flagLetter.charAt(0);
			FlagColor newColor;
			switch(c) {
				case 'G':
					newColor = FlagColor.GREEN;
					break;
				case 'Y':
					newColor = FlagColor.YELLOW;
					break;
				case 'R':
					newColor = FlagColor.RED;
					break;
				case 'B':
				default:
					newColor = FlagColor.BLACK;
					break;
			}
			if(flag == null || !flag.equals(newColor)) {
				FlagColorUpdateEvent event = new FlagColorUpdateEvent(newColor, flag);
				CBI_TV.getInstance().getEventManager().envokeEvent(event);
				flag = newColor;
			}
		}else {
			System.err.println("Response from flag js was not valid");
		}
	}

	@Override
	public void run() {
		try {
			fetchFlagColor();
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public FlagColor getFlagColor() {
		return flag;
	}
	
}
