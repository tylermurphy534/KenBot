package net.tylermurphy.Kenbot.commands;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event);
	
	public String getInvoke();
	
	public String getUsage();
	
	public String getDescription();
	
	public Permission requiredPermission();
	
	
}
