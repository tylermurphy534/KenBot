package net.tylermurphy.Kenbot.commands.moderation;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class SetWarnAction implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }
        
        if(args.isEmpty() || args.size() < 2) {
        	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			event.getChannel().sendMessage(embed.build()).queue();
            return;
        }
        
		String warnsString = args.get(0);
		int warns;
		try {
			warns = Integer.parseInt(warnsString);
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage(":x: Error formatting integer: "+args.get(0)).queue();
			return;
		}
		
		if(warns < 1 || warns > 99) {
			event.getChannel().sendMessage(":x: Warns must be between 1 and 99").queue();
			return;
		}
		
		if(args.get(1).equalsIgnoreCase("mute")) {
			Database.WarnActions.set(event.getGuild().getIdLong(), warns, "mute");
		} else if(args.get(1).equalsIgnoreCase("kick")) {
			Database.WarnActions.set(event.getGuild().getIdLong(), warns, "kick");
		} else if(args.get(1).equalsIgnoreCase("ban")) {
			Database.WarnActions.set(event.getGuild().getIdLong(), warns, "ban");
		} else {
			Database.WarnActions.remove(event.getGuild().getIdLong(), warns);
			event.getChannel().sendMessage("Removed warn action for "+warns+"warns").queue();
			return;
		}
		
		event.getChannel().sendMessageFormat("Set warn action for %s warns to %s.",warns,args.get(1).toLowerCase()).queue();
	}

	public String getInvoke() {
		return "SetWarnAction";
	}
	
	public String getUsage() {
		return "SetWarnAction <warns> <kick/ban/mute>";
	}
	
	public String getDescription() {
		return "Set what happens when a user get so many warns";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}