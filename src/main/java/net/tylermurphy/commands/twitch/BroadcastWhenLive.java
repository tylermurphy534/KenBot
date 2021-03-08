package net.tylermurphy.commands.twitch;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.apis.API;
import net.tylermurphy.apis.twitch.TwitchAPI;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class BroadcastWhenLive implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		String status = Database.Twitch.get(event.getGuild().getIdLong(), "Status");
		
		if(status == null) {
			//ignore
		} else if (status.equals("complete")) {
			channel.sendMessage(":x: There is currently a TwitchAPI broadcast currently set in this server. Please delete the current setting buy running `Ken removeTwitchBroadcast` and try agian.").queue();
			return;
		} else if(Long.parseLong(status) + 10_000L < new Date().getTime()){
			channel.sendMessage(":x: There is currently a TwitchAPI broadcast change currently processing, please try again later.\nIf you think this message is a bug, try runing `Ken removeTwitchBroadcast` and try agian.").queue();
			return;
		}
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		List<Map<String,String>> store = Database.Twitch.getAllWithSetting(args.get(0), "Login");
		if(store.size() > 0) {
			Map<String,String> data = store.get(0);
			data.put("ChannelId", event.getChannel().getId());
			data.put("GuildId", event.getGuild().getId());
			data.put("RoleId", "0");
			data.put("Status", "complete");
			Database.Twitch.set(data);
			channel.sendMessage(String.format(
					":white_check_mark: Sucessfully set channel %s to recieve messages for user %s\n"+
					"Use `Ken setTwitchChannel <#channel>` to set the broadcast channel\n"+
					"Use `Ken setTwitchRole <@Role>` to set the role to be pinged\n"+
					"Use `Ken removeTwitchBroadcast` to disable broadcast",
					channel,
					data.get("Login")
				)).queue();
			return;
		}
		
		int result = TwitchAPI.setupBroadcastSubscription(channel, args.get(0));
		if(result == API.SUCCESS) {
			channel.sendMessage(":wrench: Sent request to twitch to create webhook for user: "+args.get(0)).queue();
		} else if(result == API.FALIURE) {
			channel.sendMessage(":x: A fatal error has occoured, please try again later").queue();
		} else if(result == API.NOT_FOUND) {
			channel.sendMessage(":x: There was no user on twitch found with the username: "+args.get(0)).queue();
		} else if(result == API.DUPLICATE) {
			//first how did it get here, what shit had to happen for this to get to here
			//why didnt the meathod above find a webhook? WHY?
			channel.sendMessage(":x: If you are seeing this error, i dont know what you did, but something just went ucky fuwky!").queue();
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
