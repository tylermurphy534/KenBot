package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.TennorAPI;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class HighFive implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "highfive");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "highfive");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "highfive", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s highfives %s!", name1, name2);
		String footer = String.format("Thats %s highfives now!", hugs);
		String url = TennorAPI.getUrlFromSearch("animehighfive");
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "highfive";
	}
	
	public String getUsage() {
		return "HighFive <@User>";
	}
	
	public String getDescription() {
		return "Give someone a highfive";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
