package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class Gift implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/07cd6978e5ac988ff3530d40b57d22df/tenor.gif",
			"https://media.tenor.com/images/d98bef7cb37f836469d6fd92d8482a3a/tenor.gif",
			"https://media.tenor.com/images/3c3ccae11ef44be488dae1d80594af1d/tenor.gif",
			"https://media.tenor.com/images/f80c7f979ed3768f66d5428991b10f28/tenor.gif",
			"https://media.tenor.com/images/3996e8475b7acdd295ee488fafe8bce3/tenor.gif",
			"https://media.tenor.com/images/a61b678a3e4d1147704d2856a81ed156/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "gift");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "gift");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "gift", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s gifts %s!", name1, name2);
		String footer = String.format("Thats %s gifts now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "gift";
	}
	
	public String getUsage() {
		return "Gift <@User>";
	}
	
	public String getDescription() {
		return "Give someone a gift";
	}

	public Permission requiredPermission() {
		return null;
	}
	
}
