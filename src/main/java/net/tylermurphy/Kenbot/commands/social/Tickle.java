package net.tylermurphy.Kenbot.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class Tickle implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/a1cdb42b309b9fd6d1ed1c70a1c4b64f/tenor.gif",
			"https://media.tenor.com/images/fea79fed0168efcaf1ddfb14d8af1a6d/tenor.gif",
			"https://media.tenor.com/images/05a64a05e5501be2b1a5a734998ad2b2/tenor.gif",
			"https://media.tenor.com/images/ae7fc8d4dffe5ca4dea5eaeee5fb8abd/tenor.gif",
			"https://media.tenor.com/images/16662667791fc3275c25db595fdf89f8/tenor.gif",
			"https://media.tenor.com/images/99c4c3f50c825dabff89d9968c2dd23c/tenor.gif",
			
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "tickle");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "tickle");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "tickle", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s tickles %s!", name1, name2);
		String footer = String.format("Thats %s tickles now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "tickle";
	}
	
	public String getUsage() {
		return "Tickle <@User>";
	}
	
	public String getDescription() {
		return "Tickle someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
