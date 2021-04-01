package net.tylermurphy.Kenbot.commands.twitch;

import java.util.List;
import java.util.Map;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class SetTwitchChannel implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		String status = Database.Twitch.get(event.getGuild().getIdLong(), "Status");
		
		if(status == null || !status.equals("complete")) {
			channel.sendMessage(":x: There is not TwitchAPI broadcast set in this server for you to delete.").queue();
			return;
		}
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		List<TextChannel> channels = event.getMessage().getMentionedChannels();
		
		TextChannel mentionedChannel = channels.get(0);
		if(!mentionedChannel.canTalk()) {
			channel.sendMessage(":x: I cant speak in that channel.").queue();
			return;
		}
		
		Map<String,String> map = Database.Twitch.getStack(event.getGuild().getIdLong());
		map.put("ChannelId", mentionedChannel.getId());
		Database.Twitch.set(map);
		
		channel.sendMessage(":white_check_mark: Set Twitch Broadcast Channel to "+mentionedChannel+".").queue();
	}

	public String getInvoke() {
		return "SetTwitchChannel";
	}

	public String getUsage() {
		return "SetTwitchChannel <#Channel>";
	}

	public String getDescription() {
		return "Set the twitch broadcast channel in the server.";
	}

	public Permission requiredPermission() {
		return Permission.MANAGE_SERVER;
	}

}
