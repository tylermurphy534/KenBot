package net.tylermurphy.commands.relationship;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Divorce implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		long userId = event.getAuthor().getIdLong();
		String testId = DatabaseManager.UserSettings.get(userId, 0, "LoveId");
		if(testId == null || testId.equals("")) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You are currently not in a relationship.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String otherId = DatabaseManager.UserSettings.getUserFromValue(0, "LoveId", String.valueOf(userId));
		if(!testId.equals(otherId)) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You canceled your proposal.");
			DatabaseManager.UserSettings.delete(userId, 0, "LoveId");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle(":broken_heart: Divorced")
				.setDescription("You dicorced your relationship.");
		channel.sendMessage(embed.build()).queue();
		DatabaseManager.UserSettings.delete(userId, 0, "LoveId");
		DatabaseManager.UserSettings.delete(Long.parseLong(otherId), 0, "LoveId");
		DatabaseManager.UserSettings.delete(userId, 0, "LoveTime");
		DatabaseManager.UserSettings.delete(Long.parseLong(otherId), 0, "LoveTime");
	}

	public String getInvoke() {
		return "Divorce";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Divorce your relationship";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
