package net.tylermurphy.Kenbot.commands.games;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Dice implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		int sides;
		try {
			sides = Integer.parseInt(args.get(0));
		} catch(NumberFormatException e) {
			channel.sendMessage(":x: Error formatting integer: "+args.get(0)).queue();
			return;
		}
		
		if(sides < 4 || sides > 100) {
			channel.sendMessage(":x: Dice sides must be between 4 and 100").queue();
			return;
		}
		
		int rand = (int)(Math.random()*sides)+1;
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setColor(Color.yellow)
				.setTitle("Dice Roll")
				.setDescription(String.format(
						"You rolled a %s on a d%s",
						rand,
						sides
				));
		
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "dice";
	}
	
	public String getUsage() {
		return "Dice <sides>";
	}
	
	public String getDescription() {
		return "Rolls a dice";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
