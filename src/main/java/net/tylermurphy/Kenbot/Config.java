package net.tylermurphy.Kenbot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Config {
	
	private static Logger LOG = Logger.getLogger(Config.class.getName());
	
	public static void loadConstants() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("bot_config.json"));
		JSONObject json = (JSONObject) obj;
		
		JSONObject GENERAL = (JSONObject) json.get("General");
		
		LOG.info("General Configurations");
		BOT_TOKEN = (String) GENERAL.get("Bot_Token");
		LOG.info("\tBOT_TOKEN: "+BOT_TOKEN.replaceAll(".", "*"));
		OWNER_USER_ID = (String) GENERAL.get("Owner_User_Id");
		LOG.info("\tOWNER_USER_ID: "+OWNER_USER_ID);
		DEFAULT_PREFIX = (String) GENERAL.get("Default_Prefix");
		LOG.info("\tDEFAULT_PREFIX: "+DEFAULT_PREFIX);
		DEBUG = (Boolean) GENERAL.get("Debug");
		LOG.info("\tDEBUG: "+DEBUG);
		BOT_NAME = (String) GENERAL.get("Bot_Name");
		LOG.info("\tBOT_NAME: "+BOT_NAME);
		SUPPORT_SERVER = (String) GENERAL.get("Support_Server");
		LOG.info("\tSUPPORT_SERVER: "+SUPPORT_SERVER);
		WELCOME_MESSAGING = (Boolean) GENERAL.get("Welcome_Messaging");
		LOG.info("\tWELCOME_MESSAGING: "+WELCOME_MESSAGING);
		LEVEL_MESSAGING = (Boolean) GENERAL.get("Level_Messaging");
		LOG.info("\tLEVEL_MESSAGING: "+LEVEL_MESSAGING);
		ACCENT_COLOR_RED = (Long) GENERAL.get("Accent_Color_Red");
		LOG.info("\tACCENT_COLOR_RED: "+ACCENT_COLOR_RED);
		ACCENT_COLOR_GREEN = (Long) GENERAL.get("Accent_Color_Green");
		LOG.info("\tACCENT_COLOR_GREEN: "+ACCENT_COLOR_GREEN);
		ACCENT_COLOR_BLUE = (Long) GENERAL.get("Accent_Color_Blue");
		LOG.info("\tACCENT_COLOR_BLUE: "+ACCENT_COLOR_BLUE);
		
		JSONObject DATABASE = (JSONObject) json.get("Database");
		
		LOG.info("Database Configurations");
		DATABASE_HOST = (String) DATABASE.get("Host");
		LOG.info("\tDATABASE_HOST: "+DATABASE_HOST);
		DATABASE_PORT = (String) DATABASE.get("Port");
		LOG.info("\tDATABASE_PORT: "+DATABASE_PORT);
		DATABASE_USER = (String) DATABASE.get("Username");
		LOG.info("\tDATABASE_USER: "+DATABASE_USER);
		DATABASE_PASSWORD = (String) DATABASE.get("Password");
		LOG.info("\tDATABASE_PASSWORD: "+DATABASE_PASSWORD.replaceAll(".", "*"));
		DATABASE_NAME = (String) DATABASE.get("Database_Name");
		LOG.info("\tDATABASE_NAME: "+DATABASE_NAME);
		
		JSONObject YOUTUBE = (JSONObject) json.get("Youtube");
		LOG.info("Youtube Configurations");
		YOUTUBE_ENABLED = (Boolean) YOUTUBE.get("ENABLED");
		LOG.info("\tYOUTUBE_ENABLED: "+YOUTUBE_ENABLED);
		YOUTUBE_API_KEY = (String) YOUTUBE.get("Api_Key");
		LOG.info("\tYOUTUBE_API_KEY: "+YOUTUBE_API_KEY);
		
		JSONObject SPOTIFY = (JSONObject) json.get("Spotify");
		
		LOG.info("Spotify Configurations");
		SPOTIFY_ENABLED = (Boolean) SPOTIFY.get("ENABLED");
		LOG.info("\tSPOTIFY_ENABLED: "+SPOTIFY_ENABLED);
		SPOTIFY_CLIENT_ID = (String) SPOTIFY.get("Client_Id");
		LOG.info("\tSPOTIFY_CLIENT_ID: "+SPOTIFY_CLIENT_ID);
		SPOTIFY_CLIENT_SECRET = (String) SPOTIFY.get("Client_Secret");
		LOG.info("\tSPOTIFY_CLIENT_SECRET: "+SPOTIFY_CLIENT_SECRET);
		
		JSONObject NSFW = (JSONObject) json.get("NSFW");
		
		LOG.info("NSFW Configurations");
		NSFW_ENABLED = (Boolean) NSFW.get("ENABLED");
		LOG.info("\tNSFW_ENABLED: "+NSFW_ENABLED);
		
		JSONObject TENNOR = (JSONObject) json.get("Tennor");
		
		LOG.info("Tennor Configurations");
		TENNOR_ENABLED = (Boolean) TENNOR.get("ENABLED");
		LOG.info("\tTENNOR_ENABLED: "+TENNOR_ENABLED);
		TENNOR_API_KEY = (String) TENNOR.get("Api_Key");
		LOG.info("\tTENNOR_API_KEY: "+TENNOR_API_KEY);
		
		JSONObject TWITCH = (JSONObject) json.get("Twitch");
		
		LOG.info("Twitch Configurations");
		TWITCH_ENABLED = (Boolean) TWITCH.get("ENABLED");
		LOG.info("\tTWITCH_ENABLED: "+TWITCH_ENABLED);
		TWITCH_CLIENT_ID = (String) TWITCH.get("Client_Id");
		LOG.info("\tTWITCH_CLIENT_ID: "+TWITCH_CLIENT_ID);
		TWITCH_CLIENT_SECRET = (String) TWITCH.get("Client_Secret");
		LOG.info("\tTWITCH_CLIENT_SECRET: "+TWITCH_CLIENT_SECRET);
		TWITCH_CALLBACK_URL = (String) TWITCH.get("Callback_URL");
		LOG.info("\tTWITCH_CALLBACK_URL: "+TWITCH_CALLBACK_URL);
	}

	public static String BOT_TOKEN;
    public static String OWNER_USER_ID;
    public static String DEFAULT_PREFIX;
    public static boolean DEBUG;
    public static String BOT_NAME;
    public static String SUPPORT_SERVER;
    public static boolean WELCOME_MESSAGING;
    public static boolean LEVEL_MESSAGING;
    public static long ACCENT_COLOR_RED;
    public static long ACCENT_COLOR_GREEN;
    public static long ACCENT_COLOR_BLUE;
    
	public static String DATABASE_HOST;
	public static String DATABASE_PORT;
	public static String DATABASE_USER;
	public static String DATABASE_PASSWORD;
	public static String DATABASE_NAME;
	
	public static boolean YOUTUBE_ENABLED;
	public static String YOUTUBE_API_KEY;
	
	public static boolean SPOTIFY_ENABLED;
	public static String SPOTIFY_CLIENT_ID;
	public static String SPOTIFY_CLIENT_SECRET;
	
	public static boolean NSFW_ENABLED;
	
	public static boolean TENNOR_ENABLED;
	public static String TENNOR_API_KEY;
	
	public static boolean TWITCH_ENABLED;
	public static String TWITCH_CLIENT_ID;
	public static String TWITCH_CLIENT_SECRET;
	public static String TWITCH_CALLBACK_URL;
	
}
 