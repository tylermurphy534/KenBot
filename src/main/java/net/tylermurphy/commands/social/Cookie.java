package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.TennorAPI;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Cookie implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			channel.sendMessage(":x: Please Mention Someone").queue();
			return;
		}
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "cookie");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s bakes a cookie for %s!", name1, name2);
		String footer = String.format("Thats %s cookies now!", hugs);
		String url = TennorAPI.getUrlFromSearch("animecookie");
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "cookie";
	}
	
}
