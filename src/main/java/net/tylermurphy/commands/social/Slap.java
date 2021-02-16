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

public class Slap implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/1d8edce282f3e36abc6b730357d3cea2/tenor.gif",
			"https://media.tenor.com/images/53b846f3cc11c7c5fe358fc6d458901d/tenor.gif",
			"https://media.tenor.com/images/47a6be1fbc1c40c3a55c0e2c8b725603/tenor.gif",
			"https://media.tenor.com/images/47698b115e4185036e95111f81baab45/tenor.gif",
			"https://media.tenor.com/images/3f9e6d5315b421c11cff659cd4a7a25e/tenor.gif",
			"https://media.tenor.com/images/2b983ab0ddc99168b33e18fd1c9b200f/tenor.gif",
			"https://media.tenor.com/images/b09b36ae92b2b5c6da7212472514063d/tenor.gif",
			"https://media.tenor.com/images/773a233da0deb1d764cd206b5a415c0d/tenor.gif",
			"https://media.tenor.com/images/49b0ce2032f6134c31e1313cb078fe5a/tenor.gif",
			"https://media.tenor.com/images/c366bb3a5d7820139646d8cdce96f7a8/tenor.gif",
			"https://media.tenor.com/images/79c666d38d5494bad25c5c023c0bbc44/tenor.gif",
			"https://media.tenor.com/images/a107547117e0b8f22e00a3f39c40eb2f/tenor.gif",
			"https://media.tenor.com/images/a0c111e14b73a5ff9a876eb6beab6729/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "slap");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "slap");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "slap", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s slaps %s!", name1, name2);
		String footer = String.format("Thats %s slaps now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "slap";
	}
	
	public String getUsage() {
		return "Slap <@User>";
	}
	
	public String getDescription() {
		return "Slap someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
