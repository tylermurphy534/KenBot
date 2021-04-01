package net.tylermurphy.Kenbot.commands.fun;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Ping implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		
		channel.sendMessage("stfu "+message.getAuthor().getName()).queue();
	}
	
	public String getInvoke() { return "ping"; }
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Pings the bot";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
