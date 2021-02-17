package net.tylermurphy.commands.social;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Dance implements ICommand {

	private String[] gifs = {
			"https://media.tenor.com/images/647b3050c0eb860b83cfff354d3c0751/tenor.gif",
			"https://media.tenor.com/images/fe3826b59f80f5e6c7cc04eb474fb44d/tenor.gif",
			"https://media.tenor.com/images/9770d9c99bf00ae01a35cc7ff12fe060/tenor.gif",
			"https://media.tenor.com/images/3f324bbd9577d28eb1fb364ad9f2acfe/tenor.gif",
			"https://media.tenor.com/images/afd634e4216d2a75a2a33388657c06b9/tenor.gif",
			"https://media.tenor.com/images/7fa3b39ddac5925af0d81aefeeeb3ad4/tenor.gif",
			"https://media.tenor.com/images/c0833b0c801c59f301879a9edbe55cc6/tenor.gif",
			"https://media.tenor.com/images/0be4033d4b361127f4990add85864c5e/tenor.gif",
			"https://media.tenor.com/images/df9198b1698b75f97e5f5fb5d808f722/tenor.gif",
			"https://media.tenor.com/images/dacd31a167bdaefe37ca269091da615b/tenor.gif",
	};
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		String title = String.format("%s is dancing!", event.getAuthor().getName());
		String url = gifs[(int)(Math.random()*gifs.length)];;
		EmbedBuilder embed = EmbedUtils.embedImage(url)
				.setTitle(title);
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "dance";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Dance";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
