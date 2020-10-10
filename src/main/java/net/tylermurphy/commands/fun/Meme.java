package net.tylermurphy.commands.fun;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Meme implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		WebUtils.ins.getJSONObject("https://apis.duncte123.me/meme").async(json -> {
			if(!json.get("success").asBoolean()) {
				channel.sendMessage("Something went wront, try again later").queue();
				System.out.println(json);
				return;
			}
			
			final JsonNode data = json.get("data");
			final String title = data.get("title").asText();
			final String url = data.get("url").asText();
			final String image = data.get("image").asText();
			final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);
			channel.sendMessage(embed.build()).queue();
		});
	}

	public String getInvoke() {
		return "meme";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Sends a funny meme";
	}

}
