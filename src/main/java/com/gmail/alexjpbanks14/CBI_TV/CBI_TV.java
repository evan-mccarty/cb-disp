package com.gmail.alexjpbanks14.CBI_TV;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.secure;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.javalite.activejdbc.Base;
import org.joda.time.DateTimeZone;

import com.fazecast.jSerialComm.SerialPort;
import com.gmail.alexjpbanks14.model.User;
import com.gmail.alexjpbanks14.routes.LoginRoute;
import com.gmail.alexjpbanks14.routes.TestRoute;
import com.gmail.alexjpbanks14.routes.socketinstance.SocketInstanceRoute;
import com.gmail.alexjpbanks14.security.SocketAPISessionManager;
import com.gmail.alexjpbanks14.security.UserSessionManager;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScopeInstanceFactory;
import com.gmail.alexjpbanks14.socketapi.envoker.SocketAPIScopeBasicErrorEnvoker;
import com.gmail.alexjpbanks14.socketapi.handler.SocketAPIScopeBasicErrorHandler;
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
		
		//TODO make this suck less
		try {
			googleAPI = GoogleAPI.makeGoogleAPI("CBI-TV-81096270af83.json", Collections.singleton(CalendarScopes.CALENDAR_READONLY), GOOGLE_APPLICATION_NAME);
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		webSocket(CBI_TVRoutes.SOCKET_API_HANDLER, SocketAPIHandler.class);
		
		before(CBI_TVRoutes.CONTROL_SECURE + "/*", (req, res) -> {
			System.out.println("derplord");
			System.out.println(req.attributes());
			System.out.println(req.params());
		});
		path(CBI_TVRoutes.CONTROL_SECURE, () -> {
			before((req, res) -> {
				//res.redirect("/ass_butt.txt");
				//halt();
				System.out.println("lolswag");
			});
		});
		//Spark.get
		//before((req, res) -> {System.out.println("butt");});
		LoginRoute loginRoute = new LoginRoute(new Template("templates/login.html.twig"));
		dbGet(CBI_TVRoutes.CONTROL_LOGIN, loginRoute);
		dbPost(CBI_TVRoutes.CONTROL_LOGIN, loginRoute);
		dbGet(CBI_TVRoutes.CONTROL_TEST, new TestRoute(new Template("templates/test.html.twig")));
		dbGet(CBI_TVRoutes.SOCKET_API_INSTANCE, new SocketInstanceRoute());
		//Template template = new Template("templates/index.html.twig");
		//template.put("derp", "derpflerp");
		//System.out.println(template.render());
		
		Base.open(getDatasource());
		
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
			Base.open(CBI_TV.this.getDatasource());
		});
	}
	
	public void dbAfter(String path){
		Spark.after(path, (request, response) -> {
			Base.close();
		});
	}
	
	public void makeScopeFactory(){
		scopeFactory = new SocketAPIScopeInstanceFactory();
		scopeFactory.registerScope(SocketAPIScope.BASIC_ERROR, SocketAPIScopeBasicErrorHandler.class, SocketAPIScopeBasicErrorEnvoker.class, new SocketAPIScopeGlobalPermission());
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
	
}