package com.gmail.alexjpbanks14.webservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.TimeZone;

import com.gmail.alexjpbanks14.exception.api.CBIClassInstanceAPIInvalidResponseException;
import com.gmail.alexjpbanks14.exception.api.CBIClassInstanceAPIUnchangedException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class CBIAPI {
	
	private Gson gson;
	private DateTimeFormatter dateFormat;
	
	public static final String REQ_METHOD = "GET";
	public static final int READ_TIMEOUT = 5000;
	public static final int CONNECTION_TIMEOUT = 5000;
	
	public static final CBIAPI defaultInstance = new CBIAPI();
	
	public static CBIAPI getDefaultInstance() {
		return defaultInstance;
	}
	public CBIAPI(){
		this.gson = new GsonBuilder().create();
		dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	}
	
	public HttpURLConnection loadJson(String etag, URL url, ZonedDateTime time) throws IOException{
		HashMap<String, String> parameters = new HashMap<>();
		String formattedDate = dateFormat.format(time);
		parameters.put("startDate", formattedDate);
		url = addToURL(url, getParameterString(parameters));
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(REQ_METHOD);
		connection.setRequestProperty("If-None-Match", etag);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		connection.connect();
		return connection;
	}
	
	public void writeParameters(DataOutputStream output, HashMap<String, String> values) throws UnsupportedEncodingException, IOException{
		
		String message = getParameterString(values);
		output.writeBytes(message);
		output.flush();
		output.close();
		//output.writeBytes(URLEncoder.encode(key, "UTF-8"));
		//output.writeBytes("=");
		//output.writeBytes(URLEncoder.encode(value, "UTF-8"));
		//output.writeBytes("&");
	}
	
	public String getParameterString(HashMap<String, String> values) throws UnsupportedEncodingException{
		StringBuilder builder = new StringBuilder();
		builder.append('?');
		for(String key : values.keySet()){
			String value = values.get(key);
			builder.append(getParameterString(key, value));
			builder.append('&');
		}
		if(values.size() > 0){
			String built = builder.toString();
			return built.substring(0, built.length() - 1);
		}else{
			return new String();
		}
	}
	
	public String getParameterString(String key, String value) throws UnsupportedEncodingException{
			return URLEncoder.encode(key, "UTF-8") + "=" +
					URLEncoder.encode(value, "UTF-8");
	}
	
	public URL addToURL(URL url, String toAdd) throws MalformedURLException{
		return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + toAdd);
	}
	
	public CBClassJsonDataHolder getCBClassJsonData(String etag, URL url, ZonedDateTime zone) throws IOException,
		 CBIClassInstanceAPIUnchangedException, CBIClassInstanceAPIInvalidResponseException, JsonSyntaxException{
		HttpURLConnection connection = loadJson(etag, url, zone);
		if(connection.getResponseCode() == 200){
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			try{CBClassJsonDataHolder data = gson.fromJson(reader, CBClassJsonDataHolder.class);
				data.etag = connection.getHeaderField("Etag");
				connection.disconnect();
				return data;
			}catch(JsonSyntaxException e){
				throw e;
			}
		}else if(connection.getResponseCode() == 304){
			System.out.println("Hasetagissue" + connection.getHeaderField("Etag"));
			throw new CBIClassInstanceAPIUnchangedException("Json File not changed");
		}else{
			throw new CBIClassInstanceAPIInvalidResponseException("Response code was not valid");
		}
	}
	
	public class CBClassJsonDataHolder{
		public CBClassJsonRowsHolder data;
		public transient String etag;
	}
	
	public class CBClassJsonRowsHolder{
		public String[][] rows;
		public CBClassJsonMetaData[] metaData;
		public String $cacheExpires;
	}
	
	public class CBClassJsonMetaData{
		public String name;
	}
	
}
