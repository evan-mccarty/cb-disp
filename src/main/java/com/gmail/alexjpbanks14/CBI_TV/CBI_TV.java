package com.gmail.alexjpbanks14.CBI_TV;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.secure;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.javalite.activejdbc.Base;
import org.joda.time.DateTimeZone;

import com.fazecast.jSerialComm.SerialPort;
import com.gmail.alexjpbanks14.classinstance.ClassInstanceCBIAPIAdapter;
import com.gmail.alexjpbanks14.classinstance.ClassInstanceUpdater;
import com.gmail.alexjpbanks14.events.ClassInstanceUpdateEvent;
import com.gmail.alexjpbanks14.events.EventManager;
import com.gmail.alexjpbanks14.events.EventType;
import com.gmail.alexjpbanks14.events.FlagColorUpdateEvent;
import com.gmail.alexjpbanks14.events.RestrictionStateHistoryUpdateEvent;
import com.gmail.alexjpbanks14.events.RestrictionsUpdateEvent;
import com.gmail.alexjpbanks14.flagcolor.FlagColorFetcher;
import com.gmail.alexjpbanks14.model.RestrictionState;
import com.gmail.alexjpbanks14.model.User;
import com.gmail.alexjpbanks14.restriction.RestrictionUpdater;
import com.gmail.alexjpbanks14.routes.ControlPanelClassesRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelDashboardRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelHelpRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelLogsRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelRestrictionsRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelSettingsRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelSetupRoute;
import com.gmail.alexjpbanks14.routes.ControlPanelUsersRoute;
import com.gmail.alexjpbanks14.routes.LoginRoute;
import com.gmail.alexjpbanks14.routes.TestRoute;
import com.gmail.alexjpbanks14.routes.socketinstance.SocketInstanceRoute;
import com.gmail.alexjpbanks14.security.SocketAPISession;
import com.gmail.alexjpbanks14.security.SocketAPISessionManager;
import com.gmail.alexjpbanks14.security.UserSessionManager;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstanceFactory;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeBasicErrorEnvoker;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeFlagColorEnvoker;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeLogEventEnvoker;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeRestrictionsEnvoker;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeBasicErrorHandler;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeFlagColorHandler;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeLogEventHandler;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeRestrictionsHandler;
import com.gmail.alexjpbanks14.socketapi.permission.SocketAPIScopeGlobalPermission;
import com.gmail.alexjpbanks14.template.Template;
import com.gmail.alexjpbanks14.webservice.GoogleAPI;
import com.gmail.alexjpbanks14.websocket.SocketAPIHandler;
import com.google.api.services.calendar.CalendarScopes;

import spark.Route;
import spark.Spark;
import spark.route.HttpMethod;

public class CBI_TV {
	
	public static final String CACHE_XML_LOCATION = "/cache_config/cache.xml";
	
	public static final String GOOGLE_APPLICATION_NAME = "CBI_TV";
	
	public static final Logger LOGGER = Logger.getLogger(CBI_TV.class.getName());
	
	public static final DateTimeZone TIMEZONE = DateTimeZone.forTimeZone(TimeZone.getTimeZone("America/New_York"));
	
	public UserSessionManager userManager;
	
	public SocketAPISessionManager socketManager;
	
	public SocketAPIScopeInstanceFactory scopeFactory;
	
	public BasicDataSource datasource;
	
	public CacheManager cacheManager;
	
	public GoogleAPI googleAPI;
	
	public EventManager eventManager;
	
	public ScheduledExecutorService executor;
	
	public RestrictionUpdater restrictionUpdater;
	
	public FlagColorFetcher flagColorFetcher;
	
	public static CBI_TV instance;
	
	public static void main(String[] args){
		
		new CBI_TV(args);
		
	}
	
	public CBI_TV(String[] args){
		
		if(!initProperties(args)){
			System.exit(0);
		}
		
		setInstance(this);
		
		staticFiles.location("/static");
		
		Template.setDefaultConfigurationFromConfig();
		
		if(CBI_TVProperties.useSecure()){
		
			secure(CBI_TVProperties.getSecureJKS(), CBI_TVProperties.getSecurePass(), null, null);
		
		}else{
			
			LOGGER.log(Level.WARNING, "HTTPS secure is not enabled");
			
		}
		
		connectToSQL();
		
		makeScopeFactory();
		
		setupCacheHolders();
		
		setupEventManager();
		
		executor = Executors.newScheduledThreadPool(4);
		
		//TODO make this suck less
		try {
			googleAPI = GoogleAPI.makeGoogleAPI("CBI-TV-81096270af83.json", Collections.singleton(CalendarScopes.CALENDAR_READONLY), GOOGLE_APPLICATION_NAME);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		URL url = null;
		try {
			url = new URL("http://api.community-boating.org/api/jp-class-instances");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		URL flagColor = null;
		try {
			flagColor = new URL("https://portal2.community-boating.org/pls/apex/CBI_PROD.FLAG_JS");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		flagColorFetcher = new FlagColorFetcher(executor, flagColor, Duration.ofSeconds(10));
		
		restrictionUpdater = new RestrictionUpdater(executor, TimeZone.getDefault(), Duration.ofSeconds(20));
		
		//TODO fix this please
		ClassInstanceCBIAPIAdapter cbiAdapter = new ClassInstanceCBIAPIAdapter("cbi_tv_type", "junior", url, TimeZone.getTimeZone(ZonedDateTime.now().plusDays(1).getZone()), Duration.ofMillis(2500));
		
		ClassInstanceUpdater updater = new ClassInstanceUpdater(executor, TimeZone.getDefault());
		updater.addScheduleExpire(0, cbiAdapter);
		//updater.addToUpdate(cbiAdapter);
		//updater.addSchedule(0L, 6L, TimeUnit.SECONDS);
		
		webSocket(CBI_TVRoutes.SOCKET_API_HANDLER, SocketAPIHandler.class);
		//Spark.get
		//before((req, res) -> {System.out.println("butt");});
		LoginRoute loginRoute = new LoginRoute(new Template("templates/login.html.twig"));
		dbGet(CBI_TVRoutes.CONTROL_LOGIN, loginRoute);
		dbPost(CBI_TVRoutes.CONTROL_LOGIN, loginRoute);
		dbGet(CBI_TVRoutes.CONTROL_TEST, new TestRoute(new Template("templates/test.html.twig")));
		dbGet(CBI_TVRoutes.SOCKET_API_INSTANCE, new SocketInstanceRoute());
		dbGet(CBI_TVRoutes.CONTROL_PANEL_DASHBOARD, new ControlPanelDashboardRoute(new Template("templates/dashboard.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_RESTRICTIONS, new ControlPanelRestrictionsRoute(new Template("templates/restrictions.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_CLASSES, new ControlPanelClassesRoute(new Template("templates/classes.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_LOGS, new ControlPanelLogsRoute(new Template("templates/logs.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_USERS, new ControlPanelUsersRoute(new Template("templates/users.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_SETTINGS, new ControlPanelSettingsRoute(new Template("templates/settings.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_SETUP, new ControlPanelSetupRoute(new Template("templates/setup.html.twig")));
		dbGet(CBI_TVRoutes.CONTROL_PANEL_HELP, new ControlPanelHelpRoute(new Template("templates/help.html.twig")));
		//Template template = new Template("templates/index.html.twig");
		//template.put("derp", "derpflerp");
		//System.out.println(template.render());
		
		Base.open(getDatasource());
		//Restriction restriction = Restriction.findFirst("id = ?", 1);
		System.out.println(RestrictionState.getGsonRestrictionStatesForDay());
		//System.out.println(restriction.toGsonObject());
		//System.out.println(restriction.getLatestState());
		//System.out.println(restriction.getRestrictionType());
		//System.out.println(restriction.toMap());
		//System.out.println(restriction.toJson(false, attributeNames));
		
		before((request, response) -> {
			HttpMethod method = HttpMethod.get(request.requestMethod());
			request.session();
			String path = request.raw().getRequestURI();
			System.out.println(method + ":" + path);
		});
		//user.cre
		
		//System.out.println(User.findAll().size());
		SerialPort[] ports = SerialPort.getCommPorts();
		for(SerialPort port : ports){
			System.out.println(port.getSystemPortName());
		}
		User ass = new User();
		ass.setUsername("meds");
		ass.setPassword("j2");
		ass.saveIt();
	}
	
	public void dbPost(String path, Route route){
		dbBefore(path);
		post(path, route);
		dbAfter(path);
	}
	
	public void dbGet(String path, Route route){
		dbBefore(path);
		get(path, route);
		dbAfter(path);
	}
	
	public void dbBefore(String path){
		Spark.before(path, (request, response) -> {
			this.connectToDB();
		});
	}
	
	public void dbAfter(String path){
		Spark.after(path, (request, response) -> {
			this.closeFromDB();
		});
	}
	
	public void connectToDB() {
		if(!Base.hasConnection())
			Base.open(this.getDatasource());
	}
	
	public void closeFromDB() {
		Base.close();
	}
	
	public void makeScopeFactory(){
		scopeFactory = new SocketAPIScopeInstanceFactory();
		scopeFactory.registerScope(SocketAPIScope.BASIC_ERROR, SocketAPIScopeBasicErrorHandler.class, SocketAPIScopeBasicErrorEnvoker.class, new SocketAPIScopeGlobalPermission());
		scopeFactory.registerScope(SocketAPIScope.FLAG_COLOR, SocketAPIScopeFlagColorHandler.class, SocketAPIScopeFlagColorEnvoker.class, new SocketAPIScopeGlobalPermission());
		scopeFactory.registerScope(SocketAPIScope.RESTRICTION, SocketAPIScopeRestrictionsHandler.class, SocketAPIScopeRestrictionsEnvoker.class, new SocketAPIScopeGlobalPermission());
		scopeFactory.registerScope(SocketAPIScope.LOG_EVENT, SocketAPIScopeLogEventHandler.class, SocketAPIScopeLogEventEnvoker.class, new SocketAPIScopeGlobalPermission());
	}
	
	public void setupCacheHolders(){
		URL url = this.getClass().getResource(CACHE_XML_LOCATION);
		XmlConfiguration configuration = new XmlConfiguration(url);
		cacheManager = CacheManagerBuilder.newCacheManager(configuration);
		cacheManager.init();
		userManager = new UserSessionManager(cacheManager);
		socketManager = new SocketAPISessionManager(cacheManager, scopeFactory);
		//cache.remove("derpinstance");
	}
	
	public void connectToSQL(){
		datasource = new BasicDataSource();
		datasource.setDriverClassName(CBI_TVProperties.getSQLDriver());
		datasource.setUrl(CBI_TVProperties.getSQLHost());
		datasource.setUsername(CBI_TVProperties.getSQLUser());
		datasource.setPassword(CBI_TVProperties.getSQLPassword());
		
		//Base.open(datasource);
	}
	
	public DateTimeZone getTimeZone(){
		return TIMEZONE;
	}
	
	public void setupEventManager() {
		//TODO add error handling offload to event handler classes
		eventManager = new EventManager();
		eventManager.registerHandler(EventType.CLASS_INSTANCE_UPDATE, (a) -> {
			ClassInstanceUpdateEvent event = (ClassInstanceUpdateEvent)a;
			System.out.println(event.getClasses().size());
		});
		eventManager.registerHandler(EventType.FLAG_COLOR_UPDATE, (a) -> {
			FlagColorUpdateEvent event = (FlagColorUpdateEvent)a;
			List<SocketAPISession> allScopes = socketManager.getSessionsByScope(SocketAPIScope.FLAG_COLOR);
			allScopes.forEach((scopeInstance) -> {
				scopeInstance.getFlagUpdateScope().getEnvokerInstance().flagColorUpdate(event.getColor());
			});
		});
		eventManager.registerHandler(EventType.RESTRICTIONS_UPDATE, (a) -> {
			RestrictionsUpdateEvent event = (RestrictionsUpdateEvent)a;
			List<SocketAPISession> allScopes = socketManager.getSessionsByScope(SocketAPIScope.RESTRICTION);
			allScopes.forEach((scopeInstance) -> {
				scopeInstance.getRestrictionsScope().getEnvokerInstance().restrictionUpdate(event.getRestrictions());
			});
		});
		eventManager.registerHandler(EventType.RESTRICTION_STATE_HISTORY_UPDATE, (a) -> {
			RestrictionStateHistoryUpdateEvent event = (RestrictionStateHistoryUpdateEvent)a;
			List<SocketAPISession> allScopes = socketManager.getSessionsByScope(SocketAPIScope.RESTRICTION);
			allScopes.forEach((scopeInstance) -> {
				scopeInstance.getRestrictionsScope().getEnvokerInstance().restrictionStateHistoryUpdate(event.getRestrictionStateHistory());
			});
		});
	}
	
	public boolean initProperties(String[] args){
		String filePath = "config.properties";
		
		if(args.length > 0)
			filePath = args[0];
		else
		LOGGER.info("No properties file located, using default " + filePath);
	
		if(CBI_TVProperties.loadFromFile(filePath)){
			LOGGER.info("Loaded config file");
		}else{
			LOGGER.log(Level.SEVERE, "Error loading configuration file");
			return false;
		}
		return true;
	}
	
	public static CBI_TV getInstance(){
		return instance;
	}

	public UserSessionManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserSessionManager userManager) {
		this.userManager = userManager;
	}

	public SocketAPISessionManager getSocketManager() {
		return socketManager;
	}

	public void setSocketManager(SocketAPISessionManager socketManager) {
		this.socketManager = socketManager;
	}

	public static void setInstance(CBI_TV instance) {
		CBI_TV.instance = instance;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public BasicDataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(BasicDataSource datasource) {
		this.datasource = datasource;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}

	public FlagColorFetcher getFlagColorFetcher() {
		return flagColorFetcher;
	}

	public void setFlagColorFetcher(FlagColorFetcher flagColorFetcher) {
		this.flagColorFetcher = flagColorFetcher;
	}
	
}