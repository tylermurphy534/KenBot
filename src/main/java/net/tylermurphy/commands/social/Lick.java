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

public class Lick implements ICommand {
	
	private String[] gifs = {
		"https://media.tenor.com/images/1ce96858ae1368c5c6eef23f33594f0a/tenor.gif",
		"https://media.tenor.com/images/1e8001af03ad6bb7af661cf0265f86cb/tenor.gif",
		"https://media.tenor.com/images/a616a7ab01f5d090bd962ea7ec966e38/tenor.gif",
		"https://media.tenor.com/images/8cf186216a3fc420eb707624a6843f4e/tenor.gif",
		"https://media.tenor.com/images/3c32c3db39c6d5d80a291a753baa9d95/tenor.gif",
		"https://media.tenor.com/images/afd5031043d3b86f41b02b4c1390b90c/tenor.gif",
		"https://media.tenor.com/images/c2c5edf1db0766b187fffdc5e48520ee/tenor.gif",
		"https://media.tenor.com/images/afa54ff6ffe7d1702c7f1109606eaed1/tenor.gif",
		"https://media.tenor.com/images/543e40c763549a31c221b43bc0c323f1/tenor.gif",
		"https://media.tenor.com/images/79268ddef28762ae9aa37c89b4ee543b/tenor.gif",
		"https://media.tenor.com/images/af01ffd9c980f1f6256110ff51c1210d/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "lick");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "lick");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "lick", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s licks %s!", name1, name2);
		String footer = String.format("Thats %s licks now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "lick";
	}
	
	public String getUsage() {
		return "Lick <@User>";
	}
	
	public String getDescription() {
		return "Lick someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
