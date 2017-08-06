package com.gmail.alexjpbanks14.CBI_TV;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp.BasicDataSource;
import org.javalite.activejdbc.Base;

import com.gmail.alexjpbanks14.classinstance.ClassInstanceGoogleCalendarAdapter;
import com.gmail.alexjpbanks14.classinstance.ClassInstanceUpdater;
import com.gmail.alexjpbanks14.model.ClassInstance;
import com.gmail.alexjpbanks14.socketapi.SocketAPICommand;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeEnvoker;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstanceFactory;
import com.gmail.alexjpbanks14.socketapi.permission.SocketAPIScopeGlobalPermission;
import com.gmail.alexjpbanks14.webservice.GoogleAPI;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Events;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test {
	
	public static BasicDataSource datasource;
	
	public static void main(String[] args) throws IOException, GeneralSecurityException{
		/*GoogleCredential credentials = GoogleCredential.fromStream(new FileInputStream("CBI-TV-81096270af83.json")).createScoped(Collections.singleton(CalendarScopes.CALENDAR_READONLY));
		JsonFactory JSON = JacksonFactory.getDefaultInstance();
		HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		String app_name = "CB-TV";
		Calendar calendar = new Calendar.Builder(transport, JSON, credentials).setApplicationName(app_name).build();//community-boating.org_3lrgpalth1huovkki81uosqo2c@group.calendar.google.com
		com.google.api.services.calendar.model.Calendar calendarInstance = calendar.calendars().get("community-boating.org_3lrgpalth1huovkki81uosqo2c@group.calendar.google.com").execute();
		System.out.println(calendarInstance.getSummary());*/
		
		CBI_TVProperties.loadFromFile("config.properties");
		
		connectToSQL();
		
		Base.open(datasource);
		
		ClassInstance instance2 = ClassInstance.create();
		ClassInstance instance3 = ClassInstance.create();
		instance2.setInstanceId("butt");
		instance3.setInstanceId("butt");
		Set<ClassInstance> instances = new HashSet<>();
		instances.add(instance2);
		System.out.println(instances.contains(instance3));
		
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
		
		
		ClassInstanceUpdater updater = new ClassInstanceUpdater(service, TimeZone.getDefault());
		
		//updater.addScheduleAtTime(ZonedDateTime.now(), LocalTime.of(1, 44, 1));
		//updater.addSchedule(0, 2, TimeUnit.SECONDS);
		
		System.out.println(TimeUnit.DAYS.toMillis(1));
		
		//UserGroup group = UserGroup.findFirst("id = 1");
		
		//Permission permission = Permission.create("name", "derp", "human_name", "schmacked");
		//permission.saveIt();
		//group.add(permission);
		
		Gson gson = new GsonBuilder().create();//.registerTypeAdapter(CBClassJsonClassInstance.class, new TypeAdapter(){

			/*@Override
			public CBClassJsonClassInstance read(JsonReader arg0) throws IOException {
				arg0.beginArray();
				System.out.println("started");
				while(arg0.hasNext()){
					if(arg0.peek() == JsonToken.NULL)
						arg0.nextNull();
					arg0.
					//System.out.println(arg0.next);
					System.out.println(arg0.nextString());
				}
				arg0.endArray();
				return new Test().new CBClassJsonClassInstance();
			}

			@Override
			public void write(JsonWriter arg0, Object arg1) throws IOException {
				// TODO Auto-generated method stub
				
			}*/
			
		//}).create();
		TimeZone zone = TimeZone.getDefault();
		ZonedDateTime time2 = ZonedDateTime.now(zone.toZoneId());
		//ZonedDateTime time = ZonedDateTime.parse("2017-07-15T12:17:49.837Z");
		System.out.println(time2.getHour());
		//LocalDate today = time2.toLocalDate();
		//if(today.isEqual(time2.plusHours(2).toLocalDate()))
		//	System.out.println("good");
		//else
		//	System.out.println("not good");
		//Duration duration = Duration.between(date1, date2);
		
		URL url = new URL("http://api.community-boating.org/api/jp-class-instances");
		//url.openConnection();
		
		System.out.println(gson.toJson("flerper"));
		String variable = "{\"data\":{\"rows\":[[5544,\"Orientation\",\"07/15/2017\",\"09:15AM\",null,0],[5575,\"Mainsail I\",\"07/15/2017\",\"10:00AM\",null,19],[5594,\"Rigging\",\"07/15/2017\",\"10:00AM\",null,0],[5827,\"Laser\",\"07/15/2017\",\"11:00AM\",null,6],[5677,\"Mainsail Clinic\",\"07/15/2017\",\"12:30PM\",null,6],[5363,\"Orientation\",\"07/15/2017\",\"01:00PM\",null,0],[5642,\"Rigging\",\"07/15/2017\",\"02:15PM\",null,0],[6395,\"Advanced Sonar\",\"07/15/2017\",\"03:00PM\",null,4],[5948,\"Jib Clinic\",\"07/15/2017\",\"04:30PM\",null,5],[6394,\"Shore School\",\"07/15/2017\",\"04:30PM\",null,0]],\"metaData\":[{\"name\":\"INSTANCE_ID\"},{\"name\":\"TYPE_NAME\"},{\"name\":\"START_DATE\"},{\"name\":\"START_TIME\"},{\"name\":\"LOCATION_STRING\"},{\"name\":\"ENROLLEES\"}],\"$cacheExpires\":\"2017-07-15T09:42:59.535Z\"}}";
		//CBClassJsonDataHolder data = gson.fromJson(variable, CBClassJsonDataHolder.class);
		//CBIClassInstanceAPI api = new CBIClassInstanceAPI();
		ZonedDateTime now3 = ZonedDateTime.now().plusDays(1);
		LocalDate date2 = now3.toLocalDate();
		//System.out.println(date2);
		//ZonedDateTime today = 
		//LocalDateTime now2 = LocalDateTime.now();
		//Date date = Date.from(date2.);
		TimeZone zone2 = TimeZone.getTimeZone(now3.getZone());
		Date today = Date.from(date2.atTime(LocalTime.MIN).toInstant(now3.getOffset()));
		Date tomorrow = Date.from(date2.atTime(LocalTime.MIN).plusDays(1).toInstant(now3.getOffset()));
		//TimeZone zone = now2.
		com.google.api.client.util.DateTime now = new com.google.api.client.util.DateTime(today, zone2);
		com.google.api.client.util.DateTime later = new com.google.api.client.util.DateTime(tomorrow, zone2);
		GoogleAPI api = GoogleAPI.makeGoogleAPI("CBI-TV-81096270af83.json", Collections.singleton(CalendarScopes.CALENDAR_READONLY), "test program");
		Events events = api.getCalendar().events().list("community-boating.org_3lrgpalth1huovkki81uosqo2c@group.calendar.google.com")
		.setMaxResults(10).setTimeMin(now).setTimeMax(later).setSingleEvents(true).execute();
		//System.out.println(now);
		ClassInstanceGoogleCalendarAdapter adapter = new ClassInstanceGoogleCalendarAdapter(api, "community-boating.org_3lrgpalth1huovkki81uosqo2c@group.calendar.google.com");
		List<com.google.api.services.calendar.model.Event> eventList = events.getItems();
		//System.out.println(events.size());
		List<ClassInstance> classes = adapter.getClasses(ZonedDateTime.now());
		//System.out.println(adapter.getClasses().size());
		System.out.println("classes" + classes.size());
		for(com.google.api.services.calendar.model.Event event : eventList){
			System.out.println(event.getSummary());
			//System.out.println(event.getStart().getDateTime());
			Instant instance = Instant.ofEpochMilli(event.getStart().getDateTime().getValue());
			LocalDateTime datething = LocalDateTime.ofInstant(instance, now3.getOffset());
			System.out.println(datething.format(DateTimeFormatter.BASIC_ISO_DATE));
		}
		//new GsonBuilder().registerTypeHierarchyAdapter(TestObject.class, typeAdapter)
		/*Gson testGson = new GsonBuilder().registerTypeAdapter(TestObject.class, new TypeAdapter(){

			@Override
			public void write(JsonWriter out, Object value) throws IOException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object read(JsonReader in) throws IOException {
				in.
				return null;
			}
			
		}).create();*/
		
		//customDerserializer deserializer = new customDerserializer();
		
		//Gson gson = new GsonBuilder().registerTypeAdapter(SocketAPIComamnd.class, customDeserialized)
		
		SocketAPIScopeInstanceFactory factory = new SocketAPIScopeInstanceFactory();
		factory.registerScope(SocketAPIScope.FLAG_COLOR, testHandler.class, testEnvoker.class, new SocketAPIScopeGlobalPermission());		//ClassInstanceCBIAPIAdapter adapter = new ClassInstanceCBIAPIAdapter(url);
		//SocketAPIScopeInstance<testHandler, testEnvoker> instance = factory.getInstance(SocketAPIScope.TEST, testHandler.class, testEnvoker.class);
		//System.out.println(instance.getEnvokerInstance().doSomething());
		//SocketAPIScopeEnvokerProxy proxy = new SocketAPIScopeEnvokerProxy();
		//SocketAPIScopeEnvoker envoker = (SocketAPIScopeEnvoker)Proxy.newProxyInstance(SocketAPIScopeEnvoker.class.getClassLoader(), new Class[]{SocketAPIScopeEnvoker.class}, proxy);
		//System.out.println(envoker);
		//adapter.getClasses();
		//com.gmail.alexjpbanks14.webservice.CBIClassInstanceAPI.CBClassJsonDataHolder derp;
		/*try {
			//derp = api.getCBClassJsonData("", url);
			//System.out.println(derp.data.rows.length);
			//api.getCBClassJsonData("", url);
		} catch (CBIClassInstanceAPIUnchangedException e) {
			e.printStackTrace();
		} catch (CBIClassInstanceAPIInvalidResponseException e) {
			e.printStackTrace();
		} catch (CBIClassInstanceAPIInvalidJsonException e) {
			e.printStackTrace();
		}*/
		//System.out.println(derp.etag);
		//System.out.println(data.data.rows[0][0]);
		
		//System.out.println(group.getAllPermissions().get(0).);
		//testClass instance = new Test().new testClass();
		//doSomething caller = (doSomething) Proxy.newProxyInstance(testClass.class.getClassLoader(), new Class[] {doSomething.class}, instance);
		//caller.doSomething();
		new Test();
	}
	
	public Test()
	{
		
		EnumSet<SocketAPIScope> scopes = EnumSet.allOf(SocketAPIScope.class);
		
		System.out.println(scopes.toString());
		
		SocketAPICommand command = new SocketAPICommand(null, null, null, null, null, null);
		command.scope = SocketAPIScope.FLAG_COLOR;
		
		
		Gson gson = new Gson();
		String json = gson.toJson(command, SocketAPICommand.class);
		System.out.println(json);
		SocketAPICommand command2 = gson.fromJson(json, SocketAPICommand.class);
		System.out.println(command2.scope.name());
		
		/*customDeserializer deserializer = new customDeserializer();
		
		Gson gson = new GsonBuilder().registerTypeAdapter(SocketAPICommand.class, deserializer).create();
		
		deserializer.gson = gson;
		
		SocketAPICommand command = new SocketAPICommand();
		command.commandType = SocketAPICommand.class;
		TestObject object = new TestObject();
		object.field = new String[]{"derp", "flerp"};
		command.commandArguments = new Object[]{object, "derphello", new Integer(1)};
		String json = gson.toJson(command, SocketAPICommand.class);
		SocketAPICommand command2 = gson.fromJson(json, SocketAPICommand.class);
		System.out.println(((TestObject)command2.commandArguments[0]).field.length);*/
		
	}
//	class customDeserializer implements JsonDeserializer<SocketAPICommand>, JsonSerializer<SocketAPICommand>{
//		
//		public Gson gson;
//		
//		@Override
//		public SocketAPICommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//				throws JsonParseException {
//			//SocketAPICommand command = new SocketAPICommand();
//			JsonObject object = json.getAsJsonObject();
//			String clazzName = object.get("command_type").getAsString();
//			try {
//				Class clazz = Class.forName(clazzName);
//			//	command.commandType = clazz;
//				//System.out.println(clazz.getSimpleName());
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			JsonArray array = object.getAsJsonArray("command_arguments");
//			ArrayList<Object> objects = new ArrayList<>();
//			array.forEach((a) -> {
//				JsonObject object2 = a.getAsJsonObject();
//				String clazzName2 = object2.get("argument_type").getAsString();
//				try {
//					Class clazz2 = Class.forName(clazzName2);
//					JsonElement element = object2.get("argument_data");
//					Object instance = gson.fromJson(element, clazz2);
//					objects.add(instance);
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//				
//			});
//			//command.commandArguments = objects.toArray();
//			//return command;
//		}
//	
//		@Override
//		public JsonElement serialize(SocketAPICommand src, Type typeOfSrc, JsonSerializationContext context) {
//			// TODO Auto-generated method stub
//			JsonObject object = new JsonObject();
//			object.add("command_type", new JsonPrimitive(src.commandType.getCanonicalName()));
//			JsonArray array = new JsonArray();
//			for(Object o : src.commandArguments){
//				JsonObject argument = new JsonObject();
//				argument.add("argument_type", new JsonPrimitive(o.getClass().getCanonicalName()));
//				argument.add("argument_data", gson.toJsonTree(o, o.getClass()));
//				array.add(argument);
//			}
//			object.add("command_arguments", array);
//			return object;
//		}
//		
//	}
	
	public interface customSocketAPIEnvoker extends SocketAPIScopeEnvoker{
		
	}
	
	public static void connectToSQL(){
		datasource = new BasicDataSource();
		datasource.setDriverClassName(CBI_TVProperties.getSQLDriver());
		datasource.setUrl(CBI_TVProperties.getSQLHost());
		datasource.setUsername(CBI_TVProperties.getSQLUser());
		datasource.setPassword(CBI_TVProperties.getSQLPassword());
		
		//Base.open(datasource);
	}
	
	public class testClass implements InvocationHandler{
		//public abstract void doSomething();

		@Override
		public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public interface doSomething{
		public void doSomething();
	}
	
}
