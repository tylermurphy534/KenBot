package net.tylermurphy.apis.twitch;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.dv8tion.jda.api.entities.TextChannel;
import net.tylermurphy.Config;
import net.tylermurphy.apis.API;
import net.tylermurphy.database.Database;

public class TwitchAPI extends API {

	private static String AppAccessToken = "";
	
	public boolean setupBroadcastSubscription(TextChannel channel, String name) {
		JSONObject user = getUser(name);
		long guildId = channel.getGuild().getIdLong();
		try {
			JSONObject userData = (JSONObject) user.getJSONArray("data").get(0);
			int id = Integer.parseInt(userData.getString("id"));
			String url = "https://api.twitch.tv/helix/eventsub/subscriptions";
			String[] headers = {
					"Client-ID", Config.TWITCH_CLIENT_ID,
					"Authorization", "Bearer "+AppAccessToken,
			};
			
			String body = "{"
					+ "\"type\": \"stream.online\","
					+ "\"version\": \"1\","
					+ "\"condition\": {"
					+ "\"broadcaster_user_id\": \""+id+"\""
					+ "},"
					+ "\"transport\": {"
					+ "\"method\": \"webhook\","
					+ "\"callback\": \""+Config.REST_API_ENDPOINT_URL+"\","
					+ "\"secret\": \"fs938hdfws3\""
					+ "}"
					+ "}";

			JSONObject json = getJson("POST",url,body,headers);
			if(invalidResponse(json)) {
				generateNewAppAccessToken();
				return setupBroadcastSubscription(channel, name);
			}
			JSONObject data = (JSONObject) json.getJSONArray("data").get(0);
			if(data.getString("status").equals("webhook_callback_verification_pending")) {
				Database.GuildSettings.set(guildId, "twitchId", id+"");
				Database.GuildSettings.set(guildId, "twitchChannel", channel.getId());
				return true;
			}
			return false;
			
		}catch(Exception e) {
			return false;
		}
	}
	
	private static void generateNewAppAccessToken() {
		final String url = String.format(
				"https://id.twitch.tv/oauth2/token?client_id=%s&client_secret=%s&grant_type=client_credentials",
				Config.TWITCH_CLIENT_ID,
				Config.TWITCH_CLIENT_SECRET
			);
		try {
			JSONObject json = getJson("POST",url);
			String token = json.getString("access_token");
			if(token == null) {
				throw new NullPointerException("Twitch did not return an access token upon requeset, please check your client id and secret.");
			}
			AppAccessToken = token;
		} catch (JSONException | IOException ignored) {}
	}
 
	private static JSONObject getUser(String name) {
		final String url = String.format(
				"https://api.twitch.tv/helix/users?login=%s",
				name
				);
		String[] headers = {
				"Client-ID", Config.TWITCH_CLIENT_ID,
				"Authorization", "Bearer "+AppAccessToken
		};
		try {
			JSONObject json = getJson("GET",url,headers);
			if(invalidResponse(json)) {
				generateNewAppAccessToken();
				return getUser(name);
			}
			return json;
		} catch (JSONException | IOException ignored) {}
		return null;
	}
 
	private static boolean invalidResponse(JSONObject json) {
		try {
			if(json.getInt("status") == 400) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
