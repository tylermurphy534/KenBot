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

public class Hug implements ICommand {

	private String[] gifs = {
		"https://media.tenor.com/images/1069921ddcf38ff722125c8f65401c28/tenor.gif",
		"https://media.tenor.com/images/78d3f21a608a4ff0c8a09ec12ffe763d/tenor.gif",
		"https://media.tenor.com/images/1d94b18b89f600cbb420cce85558b493/tenor.gif",
		"https://media.tenor.com/images/e9d7da26f8b2adbb8aa99cfd48c58c3e/tenor.gif",
		"https://media.tenor.com/images/94989f6312726739893d41231942bb1b/tenor.gif",
		"https://media.tenor.com/images/506aa95bbb0a71351bcaa753eaa2a45c/tenor.gif",
		"https://media.tenor.com/images/6db54c4d6dad5f1f2863d878cfb2d8df/tenor.gif",
		"https://media.tenor.com/images/5ccc34d0e6f1dccba5b1c13f8539db77/tenor.gif",
		"https://media.tenor.com/images/7db5f172665f5a64c1a5ebe0fd4cfec8/tenor.gif",
		"https://media.tenor.com/images/4d89d7f963b41a416ec8a55230dab31b/tenor.gif",
		"https://media.tenor.com/images/969f0f462e4b7350da543f0231ba94cb/tenor.gif",
		"https://media.tenor.com/images/4e9c3a6736d667bea00300721cff45ec/tenor.gif",
		"https://media.tenor.com/images/4db088cfc73a5ee19968fda53be6b446/tenor.gif",
		"https://media.tenor.com/images/daffa3b7992a08767168614178cce7d6/tenor.gif",
		"https://media.tenor.com/images/e58eb2794ff1a12315665c28d5bc3f5e/tenor.gif",
		"https://media.tenor.com/images/5845f40e535e00e753c7931dd77e4896/tenor.gif",
		"https://media.tenor.com/images/f5df55943b64922b6b16aa63d43243a6/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "hug");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "hug");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "hug", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s hugs %s!", name1, name2);
		String footer = String.format("Thats %s hugs now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "hug";
	}
	
	public String getUsage() {
		return "Hug <@User>";
	}
	
	public String getDescription() {
		return "Hug someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
