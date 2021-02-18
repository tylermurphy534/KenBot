package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Pout implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/c9c9ff2eed3dff5c3b9f7c0c033704da/tenor.gif",
			"https://media.tenor.com/images/a8eb64cd41f3de31ba075838b61f718e/tenor.gif",
			"https://media.tenor.com/images/b7e132fd3f4e110ea54ef8aa8f4eebbe/tenor.gif",
			"https://media.tenor.com/images/271668b1037633d7f7ae63dc1a1c29f2/tenor.gif",
			"https://media.tenor.com/images/a2cde795512dffb7ed89448a14d7e68e/tenor.gif",
			"https://media.tenor.com/images/dfec7d2c203c08f5c6d0ddb82c3dda6f/tenor.gif",
			"https://media.tenor.com/images/885cdbb1e6950cefdc981db000079c85/tenor.gif",
			"https://media.tenor.com/images/a657046ab4532420bba5b5ed180286ad/tenor.gif",
			"https://media.tenor.com/images/2aedb9ff34aa111c5789004d22d05a78/tenor.gif",
			"https://media.tenor.com/images/d138c6a01594da4a9fc95dcde3136a99/tenor.gif",
			"https://media.tenor.com/images/8edb176f0430ed576ad2959760a8e98a/tenor.gif"
	};
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		String title = String.format("%s is pouting!", event.getAuthor().getName());
		String url = gifs[(int)(Math.random()*gifs.length)];;
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "Pout";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Pout";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
