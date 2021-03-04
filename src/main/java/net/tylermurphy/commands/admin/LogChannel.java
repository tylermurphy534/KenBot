package net.tylermurphy.commands.admin;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class LogChannel implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
        final Member member = event.getMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }
        
        TextChannel channel;
        
        if (args.isEmpty()) {
        	channel = event.getChannel();
        }else if(args.get(0).equals("remove")) {
        	Database.GuildSettings.set(event.getGuild().getIdLong(), "logChannel", "");
        	event.getChannel().sendMessage("Removed log channel!").queue();
        	return;
        }else {
        	List<TextChannel> channels = event.getMessage().getMentionedChannels();
        	if(!channels.isEmpty()) {
        		channel = channels.get(0);
        	}else {
        		event.getChannel().sendMessage(":x: Unknown channel").queue();
        		return;
        	}
        }
        
        Database.GuildSettings.set(event.getGuild().getIdLong(), "logChannel", String.valueOf(channel.getIdLong()));
        event.getChannel().sendMessageFormat("Set log channel to %s", channel).queue();
    }

	public String getInvoke() {
		return "logchannel";
	}
	
	public String getUsage() {
		return "logchannel <empty to set current channel, #channel, \"remove\" to remove log channel>";
	}
	
	public String getDescription() {
		return "Set the channel for Ken bot and server logs";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}
