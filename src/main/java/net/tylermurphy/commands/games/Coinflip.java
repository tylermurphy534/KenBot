package net.tylermurphy.commands.games;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Coinflip implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		int num = (int) Math.round(Math.random());
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription(num == 0 ? "Heads!" : "Tails!");
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "coinflip";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Flips a coin";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}