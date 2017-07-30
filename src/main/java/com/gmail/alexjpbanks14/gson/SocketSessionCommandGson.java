package com.gmail.alexjpbanks14.gson;

import java.lang.reflect.Type;

import com.gmail.alexjpbanks14.socketapi.SocketAPICommand;
import com.gmail.alexjpbanks14.socketapi.SocketAPIScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SocketSessionCommandGson implements JsonDeserializer<SocketAPICommand>, JsonSerializer<SocketAPICommand>{
	
	public static final Gson instance = new GsonBuilder().registerTypeAdapter(SocketAPICommand.class, new SocketSessionCommandGson()).create();
	
	@Override
	public JsonElement serialize(SocketAPICommand src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject serialized = new JsonObject();
		serialized.add("command_name", new JsonPrimitive(src.commandName));
		serialized.add("command_scope", new JsonPrimitive(src.scope.name()));
		JsonArray argumentArray = new JsonArray();
		for(Object argument : src.commandArguments){
			JsonObject argumentObject = new JsonObject();
			argumentObject.add("argument_class", new JsonPrimitive(argument.getClass().getName()));
			JsonElement argumentValue = instance.toJsonTree(argument, argument.getClass());
			argumentObject.add("argument", argumentValue);
			argumentArray.add(argumentObject);
		}
		serialized.add("command_arguments", argumentArray);
		return serialized;
	}

	@Override
	public SocketAPICommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject serialized = json.getAsJsonObject();
		String commandName = serialized.get("command_name").getAsString();
		SocketAPIScope commandScope = SocketAPIScope.valueOf(serialized.get("command_scope").getAsString());
		JsonArray argumentArray = serialized.getAsJsonArray("command_arguments");
		Object[] commandArguments = new Object[argumentArray.size()];
		for(int i = 0; i < commandArguments.length; i++){
			JsonObject argumentObject = argumentArray.get(i).getAsJsonObject();
			String commandClass = argumentObject.get("argument_class").getAsString();
			Class<?> argumentClazz;
			try {
				argumentClazz = Class.forName(commandClass, true, this.getClass().getClassLoader());
			} catch (ClassNotFoundException e) {
				System.err.println("Class was not found for argument, so it was left null");
				e.printStackTrace();
				continue;
			}
			JsonElement argument = argumentObject.get("argument");
			Object commandArgument = instance.fromJson(argument, argumentClazz);
			commandArguments[i] = commandArgument;
		}
		return new SocketAPICommand(commandScope, commandName, commandArguments);
	}
	
	
	
}
