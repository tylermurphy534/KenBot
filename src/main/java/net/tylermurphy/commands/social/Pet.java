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

public class Pet implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/da8431374a530ae516c0cc8f966d1c2b/tenor.gif",
			"https://media.tenor.com/images/8d7cdba4b7c6a04fd0c12d5f7418f920/tenor.gif",
			"https://media.tenor.com/images/40f454db8d7ee7ccad8998479fbabe69/tenor.gif",
			"https://media.tenor.com/images/acd836cdaf53a3ffd76c7c216935325b/tenor.gif",
			"https://media.tenor.com/images/ab5183f650fc93a5ec84f8510bd9d23c/tenor.gif",
			"https://media.tenor.com/images/f2addaf38c9ca8f552a20bfa13f2adc5/tenor.gif",
			"https://media.tenor.com/images/bb4471bdc56bb2cf355338059d9fe4a0/tenor.gif",
			"https://media.tenor.com/images/69fb17b3eafe27df334f9f873473d531/tenor.gif",
			"https://media.tenor.com/images/dc008a41a50a3e91a523ccb9a1533c40/tenor.gif",
			"https://media.tenor.com/images/2a2521cb348d42f18dd1fbbe9af83e29/tenor.gif",
			"https://media.tenor.com/images/d98f3f1482faa9c978c5e720a3ad15f3/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "pet");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "pet");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "pet", hugs1);
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
