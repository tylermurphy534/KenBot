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
			"https://media.tenor.com/images/2cfb5b7ad498e16043917f02883b4793/tenor.gif",
			"https://media.tenor.com/images/fbbf9713d5202abed4ad4f4c3306cbe9/tenor.gif",
			"https://media.tenor.com/images/5628f231595350b459d6bf8278cc5e59/tenor.gif",
			"https://media.tenor.com/images/5ab9ca34fce5ecbd2a99f2a041b93f4c/tenor.gif",
			"https://media.tenor.com/images/0e95047c3c3103eb894d478646e408af/tenor.gif",
			"https://media.tenor.com/images/0e3829385e85a5c38010400642fba1dc/tenor.gif",
			"https://media.tenor.com/images/d1c848d0b68b4253e8844014905e54f7/tenor.gif",
			"https://media.tenor.com/images/d25d531813a8f492ce587f3563b20eb2/tenor.gif",
			"https://media.tenor.com/images/56c5b1b4e3e856e5e8d54460d4a44d8b/tenor.gif"
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
