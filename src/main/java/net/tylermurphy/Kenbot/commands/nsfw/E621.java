package net.tylermurphy.Kenbot.commands.nsfw;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.apis.E621API;
import net.tylermurphy.Kenbot.commands.ICommand;

public class E621 implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(!channel.isNSFW()) {
			channel.sendMessage(":x: You can only use this in a NSFW channel").queue();
			return;
		}
		Thread newThread = new Thread(() -> {
			String search = String.join("+", args);
			String url = E621API.getUrlFromSearch(search);
			if(url == null) {
				channel.sendMessage(":x: Unable to find post with seach: "+search).queue();
				return;
			}else {
				EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
				embed.setImage(url);
				channel.sendMessage(embed.build()).queue();
			}
		});;
		newThread.start();
	}

	public String getInvoke() {
		return "e621";
	}
	
	public String getUsage() {
		return "e621 <search>";
	}
	
	public String getDescription() {
		return "A NSFW Command";
	}

	public Permission requiredPermission() {
		return null;
	}
	
}
