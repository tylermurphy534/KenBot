package net.tylermurphy.commands.relationship;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class RejectProposal implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		long userId = event.getAuthor().getIdLong();
		String otherId = Database.UserSettings.getUserFromValue(0L, "LoveId", String.valueOf(userId));
		if(otherId == null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("There is no propostal for you to reject");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String testId = Database.UserSettings.get(userId, 0, "LoveId");
		if(testId != null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You are currently in a relationship. Use `Ken divorce` to end it.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		Database.UserSettings.delete(Long.parseLong(otherId), 0, "LoveId");
		Database.UserSettings.delete(userId, 0, "LoveId");
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle("Rejected")
				.setDescription(String.format("%s has rejected the proposal", event.getAuthor()));
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "RejectProposal";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Rejects any proposal someone sent you";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
