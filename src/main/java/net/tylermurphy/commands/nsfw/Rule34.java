package net.tylermurphy.commands.nsfw;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Config;
import net.tylermurphy.apis.Rule34API;
import net.tylermurphy.commands.ICommand;

public class Rule34 implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(!channel.isNSFW() && event.getGuild().getIdLong() != 740721864846082108L) {
			channel.sendMessage(":x: You can only use this in a NSFW channel").queue();
			return;
		}
		if(Config.NSFW == false) {
			channel.sendMessage(":x: NSFW is disabled by the bot host").queue();
			return;
		}
		Thread newThread = new Thread(() -> {
			String search = String.join(" ", args);
			String url = Rule34API.getUrlFromSearch(search);
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
		return "rule34";
	}
	
	public String getUsage() {
		return "rule34 <search>";
	}
	
	public String getDescription() {
		return "A NSFW Command";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
