package net.tylermurphy.Kenbot.commands.relationship;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class Propose implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<User> mentionedMembers = event.getMessage().getMentionedUsers();
		long userId = event.getAuthor().getIdLong();
		String loveId = Database.UserSettings.get(userId, 0, "LoveId");
		if(mentionedMembers.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(mentionedMembers.get(0).getIdLong() == event.getAuthor().getIdLong()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("You can't propose you yourself silly. Go find someone else to propose to.", mentionedMembers.get(0)));
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(loveId != null && !loveId.equals("")) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("You currently are already in a relationship or have already proposed to someone.\n Run `Ken divorce` before you propose to anyone else.", mentionedMembers.get(0)));
			channel.sendMessage(embed.build()).queue();
			return;
		}
		String testId = Database.UserSettings.getUserFromValue(0L, "LoveId", mentionedMembers.get(0).getId());
		if(testId != null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("%s is already in a relationship or has already been proposed to.\n Wait for them to leave their relationship to try again.", mentionedMembers.get(0)));
			channel.sendMessage(embed.build()).queue();
			return;
		}
		long otherId = mentionedMembers.get(0).getIdLong();
		String otherLoveId = Database.UserSettings.get(otherId, 0, "LoveId");
		if(otherLoveId != null && !otherLoveId.equals("") && otherLoveId.equals(String.valueOf(userId))) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("%s has proposed to you. You must accept or deny before making any further relationship commands.", mentionedMembers.get(0)));
			channel.sendMessage(embed.build()).queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.appendDescription(String.format("%s has proposed to %s!\n", event.getAuthor(), mentionedMembers.get(0)))
				.appendDescription("Run `Ken acceptProposal` to accept or `Ken rejectProposal` to deny")
				.appendDescription("\nRun `Ken divorce` to cancel proposal");
		channel.sendMessage(embed.build()).queue();
		Database.UserSettings.set(userId, 0, "LoveId", String.valueOf(otherId));
		return;
	}

	public String getInvoke() {
		return "Propose";
	}
	
	public String getUsage() {
		return "Propose <@User>";
	}
	
	public String getDescription() {
		return "Propose to someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
