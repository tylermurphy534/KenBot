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

public class Punch implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/31686440e805309d34e94219e4bedac1/tenor.gif",
			"https://media.tenor.com/images/1c986c555ed0b645670596d978c88f6e/tenor.gif",
			"https://media.tenor.com/images/c621075def6ca41785ef4aaea20cc3a2/tenor.gif",
			"https://media.tenor.com/images/cff010b188084e1faed2905c0f1634c2/tenor.gif",
			"https://media.tenor.com/images/79cc6480652032a20f1cb5c446b113ae/tenor.gif",
			"https://media.tenor.com/images/f03329d8877abfde62b1e056953724f3/tenor.gif",
			"https://media.tenor.com/images/2487a7679b3d7d23cadcd51381635467/tenor.gif",
			"https://media.tenor.com/images/965fabbfcdc09ee0eb4d697e25509f34/tenor.gif",
			"https://media.tenor.com/images/0d0afe2df6c9ff3499a81bf0dab1d27c/tenor.gif",
			"https://media.tenor.com/images/b746bc37f2e29185523b1b7a0df45fdd/tenor.gif",
			"https://media.tenor.com/images/6afcfbc435b698fa5ceb2ff019718e6d/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "punch");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "punch");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "punch", hugs1);
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
