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

public class Tickle implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/2e3bcdc3423c4b97dbdf2225fd3d6caf/tenor.gif",
			"https://media.tenor.com/images/d6a0512280a84726ff8ac695a37eadbe/tenor.gif",
			"https://media.tenor.com/images/7f41832fc5c09b4947cfde63b6ff9f80/tenor.gif",
			"https://i.pinimg.com/originals/de/63/73/de6373193dc2b6622ec4178382a6a18b.gif",
			"https://i.imgur.com/8GfodAL.gif",
			"https://i1247.photobucket.com/albums/gg628/Otokonokotron2/tumblr/Kusuguttai/K%20posts/LB-1v2.gif",
			"https://pa1.narvii.com/6319/de96cdc5bcdcb0b06e55d01cf7fd7fac24f7dcf8_00.gif",
			"https://thumbs.gfycat.com/BlandMassiveCranefly-size_restricted.gif"
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
		int hugs1 = DatabaseManager.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "tickle");
		int hugs2 = DatabaseManager.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "tickle");
		hugs1++;
		int hugs = hugs1 + hugs2;
		DatabaseManager.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "tickle", hugs1);
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
