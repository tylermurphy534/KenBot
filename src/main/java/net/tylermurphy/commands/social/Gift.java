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

public class Gift implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/426936fef75e617265c52dda0ed7a5fb/tenor.gif",
			"https://media.tenor.com/images/6728206fb90c1f0c481d335f09aab82f/tenor.gif",
			"https://media.tenor.com/images/92cd1e0053bc97a637dcb1c84a89a0ce/tenor.gif",
			"https://i0.wp.com/drunkenanimeblog.com/wp-content/uploads/2018/12/anime-christmas-gift.gif",
			"https://pa1.narvii.com/5755/c86a21e370abd85dfd4e0f975bfeeb4f53db30eb_hq.gif",
			"https://animeshelter.com/wp-content/uploads/2017/11/giphy6.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "gift");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "gift");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "gift", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s gifts %s!", name1, name2);
		String footer = String.format("Thats %s gifts now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "gift";
	}
	
	public String getUsage() {
		return "Gift <@User>";
	}
	
	public String getDescription() {
		return "Give someone a gift";
	}

	public Permission requiredPermission() {
		return null;
	}
	
}
