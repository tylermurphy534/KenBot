package net.tylermurphy.commands.xp;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class XP implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		String value = DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value != null && value.equals("false")) {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription("Leveling is disabled on this server");
			event.getChannel().sendMessage(builder.build()).queue();
			return;
		}
		String textXp = DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP");
		if(textXp == null)
			textXp = "0";
		int xp = Integer.parseInt(textXp);
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
	
	public Permission requiredPermission() {
		return null;
	}

}
