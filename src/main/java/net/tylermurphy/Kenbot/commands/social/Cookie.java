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

public class Cookie implements ICommand {
	
	private String[] gifs = {
			"https://media.tenor.com/images/56654eb38f6ed08f81ebd395e98197ac/tenor.gif",
			"https://media.tenor.com/images/68d0a78bb6a73a7aeebde4a3723583af/tenor.gif",
			"https://media.tenor.com/images/627b576970a11717cc5b6522d57789da/tenor.gif",
			"https://media.tenor.com/images/a1d39744954134c973b51205385a0932/tenor.gif",
			"https://media.tenor.com/images/4096d48462e9c8b2adc2a4fd63d292bf/tenor.gif",
			"https://media.tenor.com/images/9a684862dd6a95ca16c5ecd6b02b119f/tenor.gif",
			"https://media.tenor.com/images/70e1031a9b4380cb2107aa51ec360cdd/tenor.gif"
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
		int hugs1 = Database.SocialStats.get(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie");
		int hugs2 = Database.SocialStats.get(mentionedMembers.get(0).getUser().getIdLong(), event.getAuthor().getIdLong(), "cookie");
		hugs1++;
		int hugs = hugs1 + hugs2;
		Database.SocialStats.set(event.getAuthor().getIdLong(), mentionedMembers.get(0).getUser().getIdLong(), "cookie", hugs1);
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
