package net.tylermurphy.Kenbot.commands.autoModeration;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class Mentions implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		String dataBaseCollum = "autoModMentions";

        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }
        String setting = Database.GuildSettings.get(event.getGuild().getIdLong(), dataBaseCollum);
		if(setting == null) setting = "false";
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Automod MENTION SPAM is set to")
					.setDescription(setting);
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		if(args.get(0).equalsIgnoreCase("false")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "false");
		} else if(args.get(0).equalsIgnoreCase("mute")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "mute");
		} else if(args.get(0).equalsIgnoreCase("delete")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "delete");
		} else if(args.get(0).equalsIgnoreCase("warn")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "warn");
		} else if(args.get(0).equalsIgnoreCase("warnanddelete")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "warnanddelete");
		} else if(args.get(0).equalsIgnoreCase("kick")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "kick");;
		} else if(args.get(0).equalsIgnoreCase("ban")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "ban");
		} else {
			event.getChannel().sendMessage(":x: Invalid argument.").queue();
			return;
		}
		
		event.getChannel().sendMessageFormat("Set %s automod to %s.",dataBaseCollum,args.get(0)).queue();
	}

	public String getInvoke() {
		return "mentionsspam";
	}
	
	public String getUsage() {
		return "MentionSpam <empty for current setting,false,warn,delete,mute (30 min), kick, ban>";
	}
	
	public String getDescription() {
		return "Set automod action for when a user spamms mentions";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}