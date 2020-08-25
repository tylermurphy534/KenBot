package net.tylermurphy;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.TextChannel;

public class Cache {

	public static final Map<Long, String> PREFIXES = new HashMap<>();
	public static final Map<Long, TextChannel> LOG_CHANNELS = new HashMap<>();
	public static final Map<Long, HashMap<String,String>> AUTO_MOD = new HashMap<>();
	
}
