package net.tylermurphy.commands.essential;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.CommandManager;
import net.tylermurphy.commands.ICommand;

public class Usage implements ICommand {

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
		if(command.getUsage().equals("")) {
			channel.sendMessage("That command has no required arguments, just run the command!").queue();
			return;
		}
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.appendDescription("**Command Usage**\n")
				.appendDescription(command.getUsage());
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "usage";
	}

	public String getUsage() {
		return "Usage <command>";
	}

	public String getDescription() {
		return "Returns a usage for a command";
	}

}
