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

import net.dv8tion.jda.api.entities.TextChannel;
import net.tylermurphy.Bot;
import net.tylermurphy.database.Database;

@RestController
public class RESTController {

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
				System.out.println("New Webhook Subscribed");
				List<String> data = Database.GuildSettings.getAllWithValue("twitchId", subscription.getString("id"));
				for(String s : data) {
					String state = Database.GuildSettings.get(Long.parseLong(s), "twitchStatus");
					if(!state.equals("pending")) continue;
					Database.GuildSettings.set(Long.parseLong(s), "twitchStatus", "complete");
					Database.GuildSettings.set(Long.parseLong(s), "twitchWebhookId", subscription.getString("id"));
					String channelid = Database.GuildSettings.get(Long.parseLong(s), "twitchChannel");
					Bot.JDA.getTextChannelById(channelid);
				}
				return json.getString("challenge");
			}
			String id = subscription.getJSONObject("condition").getString("broadcaster_user_id");
			List<String> data = Database.GuildSettings.getAllWithValue("twitchId", id);
			for(String s : data) {
				String channelid = Database.GuildSettings.get(Long.parseLong(s), "twitchChannel");
				TextChannel channel = (TextChannel) Bot.JDA.getTextChannelById(channelid);
				JSONObject event = json.getJSONObject("event");
				String message = String.format(
						"%s is live at https://www.twitch.tv/%s", 
						event.getString("broadcaster_user_name"),
						event.getString("broadcaster_user_login")
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
