package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Cry implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/c8f6d1972f6051cf40fec17da7b18a53/tenor.gif",
			"https://media.tenor.com/images/b1c7fc822968ee417e7b249eae255f22/tenor.gif",
			"https://media.tenor.com/images/d571f86a5adcb4545444e9d1dc4638f9/tenor.gif",
			"https://media.tenor.com/images/14faea11230861e5f61bb4d90ac9e61d/tenor.gif",
			"https://media.tenor.com/images/1a8a01e36c95efd685b45f8eab6430de/tenor.gif",
			"https://media.tenor.com/images/52c4bfbe3ae9fbfcbc0e2975e78b481c/tenor.gif",
			"https://media.tenor.com/images/3368a542ef94e3ecc0821585afa96a8a/tenor.gif",
			"https://media.tenor.com/images/d089193ac77e74bb3f8e2f23c24d712d/tenor.gif",
			"https://media.tenor.com/images/af7f60b79dcaa1c78d92c8c2f2a0e63c/tenor.gif",
			"https://media.tenor.com/images/47608365978d83956791a4b70a1cfdba/tenor.gif"
	};
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		String title = String.format("%s is crying!", event.getAuthor().getName());
		String url = gifs[(int)(Math.random()*gifs.length)];;
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "Cry";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Cry";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
