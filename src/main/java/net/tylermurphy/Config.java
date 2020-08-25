package net.tylermurphy;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Config {
	
	public static void loadConstants() {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader("bot_config.json"));
			JSONObject jsonObject = (JSONObject) obj;
			TOKEN = (String) jsonObject.get("TOKEN");
			YOUTUBE_API_KEY = (String) jsonObject.get("YOUTUBE_API_KEY");
			OWNER = (Long) jsonObject.get("OWNER_USER_ID");
			TENNOR_API_KEY = (String) jsonObject.get("TENNOR_API_KEY");
			DATABASE_HOST = (String) jsonObject.get("DATABASE_HOST");
			DATABASE_PORT = (String) jsonObject.get("DATABASE_PORT");
			DATABASE_USER = (String) jsonObject.get("DATABASE_USER");
			DATABASE_PASSWORD = (String) jsonObject.get("DATABASE_PASSWORD");
			DATABASE_NAME = (String) jsonObject.get("DATABASE_NAME");
			PREFIX = (String) jsonObject.get("DEFAULT_PREFIX");
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String PREFIX;
    public static long OWNER;
    public static String TOKEN;
	public static String YOUTUBE_API_KEY;
	public static String TENNOR_API_KEY;
	public static String DATABASE_HOST;
	public static String DATABASE_PORT;
	public static String DATABASE_USER;
	public static String DATABASE_PASSWORD;
	public static String DATABASE_NAME;
	
}
