package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class HighFive implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/b714d7680f8b49d69b07bc2f1e052e72/tenor.gif",
			"https://media.tenor.com/images/3a34c5ca91ad9988f4a5a905bec25448/tenor.gif",
			"https://media.tenor.com/images/ad22432b945ea3676ffb16ea2989b41b/tenor.gif",
			"https://media.tenor.com/images/7b1f06eac73c36721912edcaacddf666/tenor.gif",
			"https://media.tenor.com/images/c3263b8196afc25ddc1d53a4224347cd/tenor.gif",
			"https://media.tenor.com/images/9730876547cb3939388cf79b8a641da9/tenor.gif",
			"https://media.tenor.com/images/72f1cb228b748d68ad43ffda5d29de30/tenor.gif",
			"https://media.tenor.com/images/16267f3a34efb42598bd822effaccd11/tenor.gif",
			"https://media.tenor.com/images/0c23b320822afd5b1ce3faf01c2b9b69/tenor.gif"
	};
	
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
		String url = gifs[(int)(Math.random()*gifs.length)];
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
