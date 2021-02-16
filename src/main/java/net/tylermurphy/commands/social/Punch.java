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

public class Punch implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/00a3cca756b4bbae191ac33ccc6d7bcf/tenor.gif",
			"https://media.tenor.com/images/b11c79cf158d8c9bd6e721676b06ad73/tenor.gif",
			"https://media.tenor.com/images/9c14d2d5dd918471954e5946166f3632/tenor.gif",
			"https://media.tenor.com/images/eb379f98c7ced6d43a16e78dc25ae864/tenor.gif",
			"https://media.tenor.com/images/bef50761d75e855c95cb94139c8c292f/tenor.gif",
			"https://media.tenor.com/images/359a3a05dbde06a89cdcf494ad62bb5d/tenor.gif",
			"https://media.tenor.com/images/3d95f0ee1f044518cbfd3f4ee12d26bd/tenor.gif",
			"https://media.tenor.com/images/e069f131288ec2e4aaa9c6e00d27cff1/tenor.gif",
			"https://media.tenor.com/images/524ff44277b710a47267d26cef5c11c5/tenor.gif",
			"https://media.tenor.com/images/7cfb1a03d0f71b7555e29963cd8da5d0/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "punch");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "punch");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "punch", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s punchs %s!", name1, name2);
		String footer = String.format("Thats %s punchs now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];;
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "punch";
	}
	
	public String getUsage() {
		return "Punch <@User>";
	}
	
	public String getDescription() {
		return "Punch someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
