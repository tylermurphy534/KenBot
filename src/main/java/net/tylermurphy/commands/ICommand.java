package net.tylermurphy.commands;

import java.util.List;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event);
	
	public String getInvoke();
	
	public String getUsage();
	
	public String getDescription();
	
}
