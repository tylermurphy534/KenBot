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

public class Cookie implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/23fa876f0384559e91cc2f56d70e8242/tenor.gif",
			"https://media.tenor.com/images/91435046882a65ff216f5c4a1fee8e56/tenor.gif",
			"https://media.tenor.com/images/21df9d9e9334c62e6662e6a02f2ae1ae/tenor.gif",
			"https://media.tenor.com/images/c041d53a96143ec2dee20b5f77de7cfd/tenor.gif",
			"https://media.tenor.com/images/8d8e509e4a48feb9eee02f7bd6036646/tenor.gif",
			"https://media.tenor.com/images/c9c5645e8616bb60f58ea6972015e276/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "cookie");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s bakes a cookie for %s!", name1, name2);
		String footer = String.format("Thats %s cookies now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "cookie";
	}
	
	public String getUsage() {
		return "Cookie <@User>";
	}
	
	public String getDescription() {
		return "Bake someone a cookie";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
