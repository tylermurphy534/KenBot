package net.tylermurphy.commands.webhooks;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class SetWebhookURL implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage(":x: You must have the Administrator permission to use this command.").queue();
            return;
        }
		
		if(args.isEmpty()) {
			channel.sendMessage(":x: Missing Arguments").queue();
			return;
		}
		
		if(!isUrl(args.get(1))) {
			channel.sendMessage(":x: Invalid URL: "+args.get(1)).queue();
			return;
		}
		
		HashMap<String,String> webhook = DatabaseManager.Webhooks.get(event.getGuild().getIdLong(), args.get(0).toLowerCase());
		if(webhook==null) {
			channel.sendMessage(":x: Webhook does not exist").queue();
			return;
		}
		
		DatabaseManager.Webhooks.set(event.getGuild().getIdLong(), args.get(1), webhook.get("Name"), args.get(0));
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Set webhook avatar url with prefix "+args.get(0)+" to\n`"+args.get(1)+"`");
		channel.sendMessage(embed.build()).queue();
		
	}

	public String getInvoke() {
		return "SetWebhookURL";
	}
	
	private boolean isUrl(String input) {
		try {
			new URL(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
