package net.tylermurphy.commands.twitch;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.twitch.TwitchAPI;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class RemoveTwitchBroadcast implements ICommand {
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
	
		TextChannel channel = event.getChannel();
		String status = Database.Twitch.get(event.getGuild().getIdLong(), "Status");
		
		if(status == null || !status.equals("complete")) {
			channel.sendMessage(":x: There is not TwitchAPI broadcast set in this server for you to delete.").queue();
			return;
		}
		
		String webhookId = Database.Twitch.get(event.getGuild().getIdLong(), "WebhookId");
		
		Database.Twitch.remove(event.getGuild().getIdLong());
		channel.sendMessage(":white_check_mark: Removed TwitchAPI broadcast from this server.").queue();
		
		int count = Database.Twitch.getCountWithWebhookId(webhookId);
		if(count < 1) {
			TwitchAPI.deleteBroadcastSubscription(event.getGuild().getIdLong());
		}
	}
	

	public String getInvoke() {
		return "broadcastWhenLive";
	}

	public String getUsage() {
		return "BroadcastWhenLive <TwitchChannelName>";
	}

	public String getDescription() {
		return "Setup a channel in your server to broadcase a message when a twitch streamer goes live. (You can only have 1 per server)";
	}

	public Permission requiredPermission() {
		return Permission.MANAGE_SERVER;
	}
		
	
}
