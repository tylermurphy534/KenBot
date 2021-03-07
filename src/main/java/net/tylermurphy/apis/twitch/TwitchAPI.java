package net.tylermurphy.apis.twitch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import net.dv8tion.jda.api.entities.TextChannel;
import net.tylermurphy.Config;
import net.tylermurphy.apis.API;
import net.tylermurphy.database.Database;

public class TwitchAPI extends API {

	private static String AppAccessToken = "";
	
	public static int setupBroadcastSubscription(TextChannel channel, String name) {
		JSONObject user = getUser(name);
		if(user == null) return NOT_FOUND;
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
					+ "\"callback\": \""+Config.TWITCH_CALLBACK_URL+"\","
					+ "\"secret\": \"fs938hdfws3\""
					+ "}"
					+ "}";

			JSONObject json = getJson("POST",url,body,headers);
			
			try {
				if(invalidResponse(json)) {
					return setupBroadcastSubscription(channel, name);
				}
			} catch (DuplicateWebhookException e) {
				return DUPLICATE;
			} catch (Exception e) {
				return FALIURE;
			}
			
			JSONObject data = (JSONObject) json.getJSONArray("data").get(0);
			if(data.getString("status").equals("webhook_callback_verification_pending")) {
				Map<String,String> map = new HashMap<String,String>();
				map.put("GuildId", guildId+"");
				map.put("Status", new Date().getTime()+"");
				map.put("WebhookId", "");
				map.put("Login", name);
				map.put("UserId", id+"");
				map.put("ChannelId", channel.getId());
				map.put("RoleId", "0");
				Database.Twitch.set(map);
				return SUCCESS;
			}
			return FALIURE;
			
		}catch(Exception e) {
			e.printStackTrace();
			return FALIURE;
		}
	}
	
	public static int deleteBroadcastSubscription(long guildId) {
		String id = Database.Twitch.get(guildId, "WebhookId");
		if(id == null) return NOT_FOUND;
		String url = "https://api.twitch.tv/helix/eventsub/subscriptions?id="+id;
		String[] headers = {
				"Client-ID", Config.TWITCH_CLIENT_ID,
				"Authorization", "Bearer "+AppAccessToken,
		};
		try {
			String response = getResponse("DELETE ",url,headers);
			if(invalidResponse(response)) {
				generateNewAppAccessToken();
				return deleteBroadcastSubscription(guildId);
			}
			return SUCCESS;
		} catch (Exception e) {
			return FALIURE;
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
		} catch (Exception ignored) {
			throw new NullPointerException("Twitch did not return an access token upon requeset, please check your client id and secret.");
		}
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
				return getUser(name);
			}
			// if fails the user didnt send
			json.getJSONArray("data").get(0);
			return json;
		} catch (Exception ignored) {
			ignored.printStackTrace();
			return null;
		}
	}
 
	private static boolean invalidResponse(JSONObject json) throws DuplicateWebhookException {
		String status = null;
		try {
			status = json.getString("status");
		} catch (Exception e) {
			return false;
		}
		try {
			if(status.contains("401")) {
				generateNewAppAccessToken();
				return true;
			} else if(status.contains("409")) {
				throw new DuplicateWebhookException("Subscription already exists");
			} else if(status.contains("400")) {
				return true;
			} else {
				return false;
			}
		} catch (DuplicateWebhookException e) {
			throw new DuplicateWebhookException(e.getMessage());
		} catch (Exception e) {
			return false;
		}
	}
	
	private static boolean invalidResponse(String response) {
		try {
			if(response.equals("1"))
				return false;
			else
				return true;
		} catch (Exception e) {
			return false;
		}
	}

}


@SuppressWarnings("serial")
class DuplicateWebhookException extends RuntimeException {
	public DuplicateWebhookException(String message) {
		super(message);
	}
}
