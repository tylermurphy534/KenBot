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
			"https://media.tenor.com/images/ce52606293142a2bd11cda1d3f0dc12c/tenor.gif",
			"https://media.tenor.com/images/4f22255d60f3f19edf9296992b4e3483/tenor.gif",
			"https://media.tenor.com/images/031c7c348d3b86296976e2407723d4a8/tenor.gif",
			"https://media.tenor.com/images/f5ec64b40d2adf7deb84e3c0e192ff32/tenor.gif",
			"https://media.tenor.com/images/4b5e9867209d7b1712607958e01a80f1/tenor.gif",
			"https://media.tenor.com/images/7ef999b077acd6ebef92afc34690097e/tenor.gif",
			"https://media.tenor.com/images/e69ebde3631408c200777ebe10f84367/tenor.gif",
			"https://media.tenor.com/images/45b7249b38f292c88f15c668dd1f60e4/tenor.gif",
			"https://media.tenor.com/images/09b085a6b0b33a9a9c8529a3d2ee1914/tenor.gif",
			"https://media.tenor.com/images/2fb2965acbf3ed573e8b63080b947fe5/tenor.gif",
			"https://media.tenor.com/images/e0fbb27f7f829805155140f94fe86a2e/tenor.gif",
			"https://media.tenor.com/images/87ef2f7663b9dc4bf39b7e9481cda842/tenor.gif",
			"https://media.tenor.com/images/b88fa314f0f172832a5f41fce111f359/tenor.gif"
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
