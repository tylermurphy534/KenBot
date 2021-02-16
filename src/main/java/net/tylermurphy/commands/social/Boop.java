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

public class Boop implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/9945480efe5179c227558769613ee275/tenor.gif",
			"https://media.tenor.com/images/c46116b9116e1baa24e96fa6c5a78818/tenor.gif",
			"https://media.tenor.com/images/d07762ab2f5fc5d1d43525d2b3db7de8/tenor.gif",
			"https://media.tenor.com/images/ff69974ac6a5ffa9a4ab8a59a522d04e/tenor.gif",
			"https://media.tenor.com/images/75c40413d2a4ec935b707ef32ab53321/tenor.gif",
			"https://i.imgur.com/fZmUTgw.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "boop");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "boop");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "boop", hugs1);
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
