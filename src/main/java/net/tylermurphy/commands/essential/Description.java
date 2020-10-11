package net.tylermurphy.commands.essential;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.CommandManager;
import net.tylermurphy.commands.ICommand;

public class Description implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(args.size() < 1) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		ICommand command = CommandManager.commands.get(args.get(0).toLowerCase());
		if(command == null) {
			channel.sendMessage(args.get(0) + " does not invote any command");
		}
		if(command.getDescription().equals("")) {
			channel.sendMessage("That command has no description :o").queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.appendDescription("**Command Description**\n")
				.appendDescription(command.getDescription());
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "description";
	}

	public String getUsage() {
		return "Description <command>";
	}

	public String getDescription() {
		return "Returns a description for a command";
	}

}
