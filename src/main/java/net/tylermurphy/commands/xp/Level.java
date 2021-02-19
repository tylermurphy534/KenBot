package net.tylermurphy.commands.xp;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;
import net.tylermurphy.managers.LevelManager;

public class Level implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String value = DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value != null && value.equals("false")) {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription("Leveling is disabled on this server");
			event.getChannel().sendMessage(builder.build()).queue();
			return;
		}
		String unparsedXp = DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP");
		if(unparsedXp == null)
			unparsedXp = "0";
		int xp = Integer.parseInt(unparsedXp);
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription(String.format("You are level %s!", LevelManager.getLevel(xp)));
		event.getChannel().sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "level";
	}
	
	public String getUsage() {
		return "";
	}

	public String getDescription() {
		return "Get current level";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
