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

public class Boop implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/80f689a9a510e872807ad11b0560c736/tenor.gif",
			"https://media.tenor.com/images/cbf38a2e97a348a621207c967a77628a/tenor.gif",
			"https://media.tenor.com/images/0da232de2ee45e1464bf1d5916869a39/tenor.gif",
			"https://media.tenor.com/images/fde75886df37020bc37d7ba44c1e29ee/tenor.gif",
			"https://media.tenor.com/images/b0565fb51f600ee5f3deb3d572eef680/tenor.gif",
			"https://media.tenor.com/images/dbde71d42e747010b980422b88e77f9b/tenor.gif",
			"https://media.tenor.com/images/e8e15ece5fe1b91e8d349402b8fe1fad/tenor.gif",
			"https://media.tenor.com/images/1ca61d7ecff5b3433f9edd71ebd4bc40/tenor.gif",
			"https://media.tenor.com/images/6882df36a5ee12e9464549eb62730655/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "boop");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "boop");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "boop", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s boops %s!", name1, name2);
		String footer = String.format("Thats %s boops now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "boop";
	}
	
	public String getUsage() {
		return "Boop <@User>";
	}
	
	public String getDescription() {
		return "Boop someones nose";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
