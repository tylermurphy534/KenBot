package net.tylermurphy.Kenbot.commands.xp;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;
import net.tylermurphy.Kenbot.managers.LevelManager;

public class Level implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String value = Database.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value != null && value.equals("false")) {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription("Leveling is disabled on this server");
			event.getChannel().sendMessage(builder.build()).queue();
			return;
		}
		int xp = Database.Xp.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
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
