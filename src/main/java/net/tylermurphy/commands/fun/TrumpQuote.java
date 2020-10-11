package net.tylermurphy.commands.fun;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.WhatDoesTrumpThinkAPI;
import net.tylermurphy.commands.ICommand;

public class TrumpQuote implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String quote = WhatDoesTrumpThinkAPI.getQuote();
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription(quote);
		event.getChannel().sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "TrumpQuote";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Sends a Trump quote";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
