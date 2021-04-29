package net.tylermurphy.Kenbot.commands.relationship;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class Divorce implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		long userId = event.getAuthor().getIdLong();
		String testId = Database.UserSettings.get(userId, 0, "LoveId");
		String proposedId = Database.UserSettings.getFirstUserWithValue(0, "LoveId", String.valueOf(userId));
		if(proposedId != null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("Someone has proposed to you. Run `acceptProposal` or `rejectProposal` before you run anything else.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(testId == null || testId.equals("")) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You are currently not in a relationship.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String otherId = Database.UserSettings.getFirstUserWithValue(0, "LoveId", String.valueOf(testId));
		if(!testId.equals(otherId)) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You canceled your proposal.");
			Database.UserSettings.delete(userId, 0, "LoveId");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle(":broken_heart: Divorced")
				.setDescription("You dicorced your relationship.");
		channel.sendMessage(embed.build()).queue();
		Database.UserSettings.delete(userId, 0, "LoveId");
		Database.UserSettings.delete(Long.parseLong(otherId), 0, "LoveId");
		Database.UserSettings.delete(userId, 0, "LoveTime");
		Database.UserSettings.delete(Long.parseLong(otherId), 0, "LoveTime");
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
