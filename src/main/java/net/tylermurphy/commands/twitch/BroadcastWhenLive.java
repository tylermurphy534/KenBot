package net.tylermurphy.commands.twitch;

import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.twitch.TwitchAPI;
import net.tylermurphy.commands.ICommand;

public class BroadcastWhenLive implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		int b = TwitchAPI.setupBroadcastSubscription(channel, args.get(0));
		channel.sendMessage(b+"").queue();
	}

	public String getInvoke() {
		return "broadcastWhenLive";
	}

	public String getUsage() {
		return "BroadcaseWhenLive <TwitchChannelName>";
	}

	public String getDescription() {
		return "Setup a channel in your server to broadcase a message when a twitch streamer goes live. (You can only have 1 per server)";
	}

	public Permission requiredPermission() {
		return Permission.MANAGE_SERVER;
	}

}
