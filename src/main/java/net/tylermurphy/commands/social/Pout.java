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
			"https://media.tenor.com/images/46c9b8da42a62778fb37f89513c8af0e/tenor.gif",
			"https://media.tenor.com/images/d0ec17c2a83af604847276fdead3d786/tenor.gif",
			"https://media.tenor.com/images/011ec2a67e69cd48570bf530ce84016b/tenor.gif",
			"https://media.tenor.com/images/08fe41ceacbc29e336de846ef47b8e49/tenor.gif",
			"https://media.tenor.com/images/7b0ca78ae82c152cccecc7365e1be821/tenor.gif",
			"https://media.tenor.com/images/71cf1dd7a4e567a95bff94ae2bd3c9f4/tenor.gif",
			"https://media.tenor.com/images/d6a58a4bd97a128bdb2bf9597d8701c0/tenor.gif",
			"https://media.tenor.com/images/62ca52c3153ea4cfc15a9542e86a6387/tenor.gif",
			"https://media.tenor.com/images/e72b8fcd1a77ae12e69d21b6283d4823/tenor.gif",
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
