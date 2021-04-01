package net.tylermurphy.Kenbot.commands.fun;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class WhoAreYou implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		MessageChannel channel = event.getChannel();
		
		channel.sendMessage("A very good discord bot owo").queue();
		
	}
	
	public String getInvoke() { return "whoareyou"; }
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Who am i?";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
