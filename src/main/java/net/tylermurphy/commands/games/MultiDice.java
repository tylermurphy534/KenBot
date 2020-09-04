package net.tylermurphy.commands.games;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class MultiDice implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		
		if(args.isEmpty() || args.size() < 2) {
			channel.sendMessage(":x: Missing Arguments").queue();
			return;
		}
		
		int sides;
		try {
			sides = Integer.parseInt(args.get(0));
		} catch(NumberFormatException e) {
			channel.sendMessage(":x: Error formatting integer: "+args.get(0)).queue();
			return;
		}
		int dice;
		try {
			dice = Integer.parseInt(args.get(1));
		} catch(NumberFormatException e) {
			channel.sendMessage(":x: Error formatting integer: "+args.get(1)).queue();
			return;
		}
		
		if(sides < 4 || sides > 100) {
			channel.sendMessage(":x: Dice sides must be between 4 and 100").queue();
			return;
		}
		if(dice < 2 || dice > 100) {
			channel.sendMessage(":x: Dice must be between 2 and 100").queue();
			return;
		}
		try {
			int total = 0;
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setColor(Color.yellow)
					.setTitle("Dice Roll");
			for(int i=0;i<dice;i++) {
				int rand = (int)(Math.random()*sides)+1;
				total+= rand;
				builder.appendDescription(String.format(
								"`Dice %s:` rolled %s\n",
								i+1,
								rand,
								sides
						));
			}
			builder.appendDescription("\n`Total:` "+total);
			channel.sendMessage(builder.build()).queue();
		}catch(Exception e) {
			int total = 0;
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setColor(Color.yellow)
					.setTitle("Dice Roll");
			String text = "```";
			for(int i=0;i<dice;i++) {
				int rand = (int)(Math.random()*sides)+1;
				total+= rand;
				text += rand;
				if(i+1<dice) text += ",";
			}
			text += ("```\n`Total:` "+total);
			builder.setDescription(text);
			channel.sendMessage(builder.build()).queue();
		}
	}

	public String getInvoke() {
		return "multidice";
	}

}
