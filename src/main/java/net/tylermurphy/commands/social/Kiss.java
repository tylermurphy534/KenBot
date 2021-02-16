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

public class Kiss implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/6702ca08b5375a74b6b9805382021f73/tenor.gif",
			"https://media.tenor.com/images/26aaa1494b424854824019523c7ba631/tenor.gif",
			"https://media.tenor.com/images/924c9665eeb727e21a6e6a401e60183b/tenor.gif",
			"https://media.tenor.com/images/f2795e834ff4b9ed3c8ca6e1b21c3931/tenor.gif",
			"https://media.tenor.com/images/197df534507bd229ba790e8e1b5f63dc/tenor.gif",
			"https://media.tenor.com/images/4b75a7e318cb515156bb7bfe5b3bbe6f/tenor.gif",
			"https://media.tenor.com/images/7b50048d76f76a8e5b3d8fc5a3fc6a21/tenor.gif",
			"https://media.tenor.com/images/a75800a31f350c6a29ef2343931492b2/tenor.gif",
			"https://media.tenor.com/images/b020758888323338c874c549cbca5681/tenor.gif",
			"https://media.tenor.com/images/02b3ad0fb1d6aa77daeee0ace21d5774/tenor.gif",
			"https://media.tenor.com/images/6e4be7dcabb41ee76f2372f0492fc107/tenor.gif",
			"https://media.tenor.com/images/5a6a04fc81d70ef353d928a87ed25f6b/tenor.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "kiss");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "kiss");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "kiss", hugs1);
		String name1 = event.getAuthor().getName();
		String name2 = mentionedMembers.get(0).getUser().getName();
		String title = String.format("%s kisses %s!", name1, name2);
		String footer = String.format("Thats %s kisses now!", hugs);
		String url = gifs[(int)(Math.random()*gifs.length)];
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title)
				.setFooter(footer);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "kiss";
	}
	
	public String getUsage() {
		return "Kiss <@User>";
	}
	
	public String getDescription() {
		return "Kiss someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
