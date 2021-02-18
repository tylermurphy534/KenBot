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
			"https://media.tenor.com/images/fe39cfc3be04e3cbd7ffdcabb2e1837b/tenor.gif",
			"https://media.tenor.com/images/9ea4fb41d066737c0e3f2d626c13f230/tenor.gif",
			"https://media.tenor.com/images/b7a844cc66ca1c6a4f06c266646d070f/tenor.gif",
			"https://media.tenor.com/images/53d180f129f51575a46b6d3f0f5eeeea/tenor.gif",
			"https://media.tenor.com/images/3fd96f4dcba48de453f2ab3acd657b53/tenor.gif",
			"https://media.tenor.com/images/358986720d4b533a49bdb67cbc4fe3e5/tenor.gif",
			"https://media.tenor.com/images/c25d3286056d127b1eeeb1ff657b0580/tenor.gif",
			"https://media.tenor.com/images/a9b8bd2060d76ec286ec8b4c61ec1f5a/tenor.gif",
			"https://media.tenor.com/images/6885c7676d8645bf2891138564159713/tenor.gif",
			"https://media.tenor.com/images/dcd359a74e32bca7197de46a58ec7b72/tenor.gif",
			"https://media.tenor.com/images/7437caf9fb0bea289a5bb163b90163c7/tenor.gif",
			"https://media.tenor.com/images/4a6b15b8d111255c77da57c735c79b44/tenor.gif"
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
