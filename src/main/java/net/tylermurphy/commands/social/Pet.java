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

public class Pet implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/d9b480bcd392d05426ae6150e986bbf0/tenor.gif",
			"https://media.tenor.com/images/a4a2b1eaa47fd0d8d0951433bc59ab9a/tenor.gif",
			"https://media.tenor.com/images/f5176d4c5cbb776e85af5dcc5eea59be/tenor.gif",
			"https://media.tenor.com/images/57b6168e77f77046f4f6b1158de7ba3d/tenor.gif",
			"https://media.tenor.com/images/73a746bada06751716d3173fbb9e6864/tenor.gif",
			"https://media.tenor.com/images/0ac15c04eaf7264dbfac413c6ce11496/tenor.gif",
			"https://media.tenor.com/images/116fe7ede5b7976920fac3bf8067d42b/tenor.gif",
			"https://media.tenor.com/images/ba551e0f5a51c7d77a3ca4e62a14a919/tenor.gif",
			"https://media.tenor.com/images/01827ac93db8966ee97e63308ba79965/tenor.gif",
			"https://media.tenor.com/images/17b946b3bdcf55311039ed554d776948/tenor.gif",
			"https://media.tenor.com/images/183ff4514cbe90609e3f286adaa3d0b4/tenor.gif",
			"https://media.tenor.com/images/e25190a3302799225ea95e4380ae5a45/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "pet");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "pet");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "pet", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s pets %s!", name1, name2);
		String footer = String.format("Thats %s pets now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "pet";
	}
	
	public String getUsage() {
		return "Pet <@User>";
	}
	
	public String getDescription() {
		return "Pet someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
