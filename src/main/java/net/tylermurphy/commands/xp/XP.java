package net.tylermurphy.commands.xp;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class XP implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		int xp = Integer.parseInt(DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP"));
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setDescription(String.format("You have %s xp!", xp));
		event.getChannel().sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "xp";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Get current amount of xp";
	}

}
