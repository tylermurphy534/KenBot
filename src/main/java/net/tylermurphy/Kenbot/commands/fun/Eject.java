package net.tylermurphy.Kenbot.commands.fun;

import java.io.File;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.image.GifFactory;

public class Eject implements ICommand {

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
		
		File gif = GifFactory.generateEjectGif(mentionedMembers.get(0).getUser());
		
		channel
			.sendMessageFormat(":rocket:**| %s** decided to vote off %s", event.getAuthor().getName(), mentionedMembers.get(0).getUser().getName())
			.addFile(gif, "eject.gif")
			.queue((result) -> {
				gif.delete();
			});
		
	}

	public String getInvoke() {
		return "eject";
	}
	
	public String getUsage() {
		return "eject <@User>";
	}
	
	public String getDescription() {
		return "ejects someone";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
