package net.tylermurphy.commands.nsfw;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Config;
import net.tylermurphy.apis.NekosLifeAPI;
import net.tylermurphy.commands.ICommand;

public class Boobs implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(!channel.isNSFW() && event.getGuild().getIdLong() != 740721864846082108L) {
			channel.sendMessage(":x: You can only use this in a NSFW channel").queue();
			return;
		}
		if(Config.NSFW == false) {
			channel.sendMessage(":x: NSFW is disabled by the bot host").queue();
			return;
		}
		String url = NekosLifeAPI.getUrlFromSearch("boobs");
		EmbedBuilder embed = EmbedUtils.embedImage(url);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "boobs";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "A NSFW Command";
	}
	
}
