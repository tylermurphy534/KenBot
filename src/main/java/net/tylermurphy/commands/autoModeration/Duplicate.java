package net.tylermurphy.commands.autoModeration;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Duplicate implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
		String dataBaseCollum = "autoModMDuplicate";
		
        if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Automod DUPLICATE TEXT is set to")
					.setDescription(DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(), dataBaseCollum));
			event.getChannel().sendMessage(embed.build()).queue();
		}
		
		if(args.get(0).equalsIgnoreCase("false")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "false");
		} else if(args.get(0).equalsIgnoreCase("mute")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "mute");
		} else if(args.get(0).equalsIgnoreCase("delete")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "delete");
		} else if(args.get(0).equalsIgnoreCase("warn")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "warn");
		} else if(args.get(0).equalsIgnoreCase("warnanddelete")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "warnanddelete");
		} else if(args.get(0).equalsIgnoreCase("kick")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "kick");;
		} else if(args.get(0).equalsIgnoreCase("ban")) {
			DatabaseManager.GuildSettings.set(event.getGuild().getIdLong(), dataBaseCollum, "ban");
		} else {
			event.getChannel().sendMessage(":x: Invalid argument.").queue();
			return;
		}
		
		event.getChannel().sendMessageFormat("Set %s automod to %s.",dataBaseCollum,args.get(0)).queue();
	}

	public String getInvoke() {
		return "duplicatetext";
	}

}