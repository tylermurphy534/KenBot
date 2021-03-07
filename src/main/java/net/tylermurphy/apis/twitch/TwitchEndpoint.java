package net.tylermurphy.apis.twitch;

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
				List<Map<String,String>> store = Database.Twitch.getAllWithUserId(subscription.getJSONObject("condition").getString("broadcaster_user_id"));
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
			List<Map<String,String>> store = Database.Twitch.getAllWithUserId(id);
			for(Map<String,String> map : store) {
				TextChannel channel = (TextChannel) Bot.JDA.getTextChannelById(map.get("ChannelId"));
				
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
				
				JSONObject event = json.getJSONObject("event");
				String message = String.format(
						"%s is live at https://www.twitch.tv/%s\n%s", 
						event.getString("broadcaster_user_name"),
						event.getString("broadcaster_user_login"),
						role
					);
				channel.sendMessage(message).queue();
			}
			return "Sucess";
		} catch(JSONException e) {
			throw new BadRequest("Error parsing JSON");
		} catch (Exception e) {
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
