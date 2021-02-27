package net.tylermurphy.commands.relationship;

import java.util.Date;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class AcceptProposal implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		long userId = event.getAuthor().getIdLong();
		String otherId = DatabaseManager.UserSettings.getUserFromValue(0L, "LoveId", String.valueOf(userId));
		if(otherId == null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("There is no proposal for you to accept");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String testId = DatabaseManager.UserSettings.get(userId, 0, "LoveId");
		if(testId != null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You are currently in a relationship. Use `Ken divorce` if you to enter another relationship.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		DatabaseManager.UserSettings.set(userId, 0, "LoveId", otherId);
		User otherUser = event.getGuild().getMemberById(otherId).getUser();
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle(":ring: Married!")
				.setDescription(String.format("%s has accepted the proposal from %s!\nThey are now happily married!", event.getAuthor(), otherUser));
		channel.sendMessage(embed.build()).queue();
		DatabaseManager.UserSettings.set(userId, 0, "LoveTime", String.valueOf(new Date().getTime()));
		DatabaseManager.UserSettings.set(Long.parseLong(otherId), 0, "LoveTime", String.valueOf(new Date().getTime()));
	}

	public String getInvoke() {
		return "AcceptProposal";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Accepts any proposal someone sent you";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
