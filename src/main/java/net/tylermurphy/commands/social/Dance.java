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
			"https://media.tenor.com/images/86ed183f271b9b322aef616ed086c8b6/tenor.gif",
			"https://media.tenor.com/images/47d5d52b84ca2117c336ab3de3978b3a/tenor.gif",
			"https://media.tenor.com/images/56350dfdcd3a5fa4fd66e9e87f9574bb/tenor.gif",
			"https://media.tenor.com/images/e7c679012622975a67d288be3fb3b6eb/tenor.gif",
			"https://media.tenor.com/images/1a13c111736f868f9abab76e8ac9009d/tenor.gif",
			"https://media.tenor.com/images/daa38ff8d98d709e525de49f536ae278/tenor.gif",
			"https://media.tenor.com/images/a1e2967207391a46b54097b2abde78e4/tenor.gif",
			"https://media.tenor.com/images/d8e41933044336bdf8a6818299365f3f/tenor.gif",
			"https://media.tenor.com/images/97514c64332ac4660b16513fed65de84/tenor.gif",
			"https://media.tenor.com/images/4b34c2fd473942b7fbd25c443b8ed8a2/tenor.gif",
			"https://media.tenor.com/images/8df28ac0b72e04b6f464759d909a160f/tenor.gif",
			"https://media.tenor.com/images/9841990160f71767843af6cf08b5502d/tenor.gif",
			"https://media.tenor.com/images/ed527e2e52c51a4138d91c8824530d3e/tenor.gif",
			"https://media.tenor.com/images/7fbdeebbd3809df4252310264c9044d1/tenor.gif"
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
