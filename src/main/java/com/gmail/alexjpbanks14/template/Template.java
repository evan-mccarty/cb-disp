package com.gmail.alexjpbanks14.template;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;

import com.gmail.alexjpbanks14.CBI_TV.CBI_TVProperties;

public class Template {
	
	static EnvironmentConfiguration DEFAULT_CONFIGURATION;
	JtwigTemplate template;
	JtwigModel model;
	
	public Template(String path){
		this(path, DEFAULT_CONFIGURATION);
	}
	
	public Template(String path, EnvironmentConfiguration configuration){
		this(JtwigTemplate.classpathTemplate(path, configuration));
	}
	
	public Template(JtwigTemplate template){
		this.template = template;
		this.model = new JtwigModel();
	}
	
	public void put(String key, Object object){
		this.model = model.with(key, object);
	}
	
	public void setModel(JtwigModel model){
		this.model = model;
	}
	
	public void clear(){
		this.model = new JtwigModel();
	}
	
	public String render(){
		return render(true);
	}
	
	public String render(boolean clear){
		String render = this.template.render(model);
		if(clear)
			this.clear();
		return render;
	}
	
	public static void setDefaultConfiguration(EnvironmentConfiguration configuration){
		DEFAULT_CONFIGURATION = configuration;
	}
	
	public static void setDefaultConfigurationFromConfig(){
		EnvironmentConfigurationBuilder configuration = EnvironmentConfigurationBuilder.configuration();
				if(CBI_TVProperties.getDisableTemplateCache())
					configuration = configuration.parser().withoutTemplateCache().and();
		DEFAULT_CONFIGURATION = configuration.build();
	}
	
	public static EnvironmentConfiguration getDefaultConfiguration(){
		return DEFAULT_CONFIGURATION;
	}
	
}
