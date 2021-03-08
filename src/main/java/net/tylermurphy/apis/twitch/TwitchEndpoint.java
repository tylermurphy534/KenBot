package net.tylermurphy.apis.twitch;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.tylermurphy.Bot;
import net.tylermurphy.database.Database;

@RestController
public class TwitchEndpoint {

	@RequestMapping(value="/subscribe")
    public String callback(@RequestBody String body, @RequestHeader Map<String, String> headers) throws BadRequest, ServerError {
		if(body == null) {
			throw new BadRequest("No body was sent");
		}
		try {
			JSONObject json = new JSONObject(body);
			JSONObject subscription = json.getJSONObject("subscription");
			String status = subscription.getString("status");
			if(status.equals("webhook_callback_verification_pending")) {
				List<Map<String,String>> store = Database.Twitch.getAllWithSetting(subscription.getJSONObject("condition").getString("broadcaster_user_id"), "UserId");
				System.out.println(subscription.getString("id"));
				for(Map<String,String> map : store) {
					System.out.print(map.toString());
					if(map.get("Status").equals("complete")) continue;
					map.put("Status", "complete");
					map.put("WebhookId", subscription.getString("id"));
					Database.Twitch.set(map);
					TextChannel channel = Bot.JDA.getTextChannelById(map.get("ChannelId"));
					channel.sendMessage(String.format(
							":white_check_mark: Sucessfully set channel %s to recieve messages for user %s\n"+
							"Use `Ken setTwitchChannel <#channel>` to set the broadcast channel\n"+
							"Use `Ken setTwitchRole <@Role>` to set the role to be pinged\n"+
							"Use `Ken removeTwitchBroadcast` to disable broadcast",
							channel,
							map.get("Login")
						)).queue();
				}
				return json.getString("challenge");
			}
			String id = subscription.getJSONObject("condition").getString("broadcaster_user_id");
			List<Map<String,String>> store = Database.Twitch.getAllWithSetting(id, "UserId");
			
			System.out.println("A----------------");
			System.out.println(store.size());
			
			JSONObject user = null;
			JSONObject stream = null;
			
			for(Map<String,String> map : store) {
				TextChannel channel = (TextChannel) Bot.JDA.getTextChannelById(map.get("ChannelId"));
				
				if(user == null) {
					System.out.println("B---------------------");
					System.out.println(map.get("Login"));
					user = (JSONObject) TwitchAPI.getUser(map.get("Login")).getJSONArray("data").get(0);
					System.out.println(user.toString());
				}
				
				if(stream == null) {
					System.out.println("C---------------------");
					System.out.println(map.get("UserId"));
					stream = (JSONObject) TwitchAPI.getStream(map.get("UserId")).getJSONArray("data").get(0);
					System.out.println(stream.toString());
				}

				String roleId = map.get("RoleId");
				Guild guild = Bot.JDA.getGuildById(map.get("GuildId"));
				Role role = null;
				if(roleId.equals("0")) {
					role = guild.getPublicRole();
				} else {
					role = Bot.JDA.getRoleById(roleId);
				}
				if(role == null || !role.isMentionable()) {
					role = guild.getPublicRole();
				}
				
				String profile_image_url = user.getString("profile_image_url");
				String game_name = stream.getString("game_name");
				String thumbnail_url = stream.getString("thumbnail_url");
				String title = stream.getString("title");
				String viewer_count = stream.getInt("viewer_count")+"";
				
				thumbnail_url = thumbnail_url.replace("{width}", "400");
				thumbnail_url = thumbnail_url.replace("{height}", "200");
				
				JSONObject event = json.getJSONObject("event");
				String user_name = event.getString("broadcaster_user_name");
				String user_login = event.getString("broadcaster_user_login");

				String stream_url = "https://www.twitch.tv/"+user_login;
				
				EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
				embed.setTitle(title, stream_url);
				embed.setAuthor(user_name, null, profile_image_url);
				embed.addField("Game", game_name, true);
				embed.addField("Viewers", viewer_count, true);
				embed.setThumbnail(profile_image_url);
				embed.setImage(thumbnail_url);
				embed.setColor(new Color(147,112,219));
				
				channel.sendMessageFormat("Hey %s, %s is live at %s", role, user_name, stream_url).queue(message -> {
					message.editMessage(embed.build()).queue();
				});
//				
//				String message = String.format(
//						"%s is live at https://www.twitch.tv/%s\n%s", 
//						event.getString("broadcaster_user_name"),
//						event.getString("broadcaster_user_login"),
//						role
//					);
//				channel.sendMessage(message).queue();
			}
			return "Sucess";
		} catch(JSONException e) {
			e.printStackTrace();
			throw new BadRequest("Error parsing JSON");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerError("An unexpected error has occured: "+e.getMessage());
		}
   }

}

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequest extends Exception {
	  public BadRequest(String message) {
	      super(message);
	  }
}

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ServerError extends Exception {
	  public ServerError(String message) {
	      super(message);
	  }
}
