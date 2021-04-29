package net.tylermurphy.Kenbot.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.Bot;
import net.tylermurphy.Kenbot.Config;

public class CommandRegister {
	
	public final static Map<String, ICommand> REGISTER = new HashMap<>();

	public static void registerCommands() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		Reflections reflections = new Reflections("net.tylermurphy.Kenbot");
		Set<Class<? extends ICommand>> commands = reflections.getSubTypesOf(ICommand.class);
		for(Class<? extends ICommand> c : commands) { 
			ICommand cmd = c.getDeclaredConstructor().newInstance();
			if(c.getPackage().getName().equals("net.tylermurphy.Kenbot.commands.twitch") && !Config.TWITCH_ENABLED) continue;
			if(c.getPackage().getName().equals("net.tylermurphy.Kenbot.commands.nsfw") && !Config.NSFW_ENABLED) continue;
			if(c.getPackage().getName().equals("net.tylermurphy.Kenbot.commands.music") && !Config.YOUTUBE_ENABLED) continue;
			if(cmd instanceof ListenerAdapter) {
				Bot.JDA.addEventListener(cmd);
			}
			register(cmd);
		}
	}
	
	private static void register(ICommand command) {
        if (!REGISTER.containsKey(command.getInvoke())) {
        	REGISTER.put(command.getInvoke().toLowerCase(), command);
        }
    }
	
	public Collection<ICommand> getCommands() {
		return REGISTER.values();
	}
	
	public ICommand getCommand(String name) {
        return REGISTER.get(name);
    }
	
}
