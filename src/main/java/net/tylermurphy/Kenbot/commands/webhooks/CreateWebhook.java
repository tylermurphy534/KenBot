package net.tylermurphy.Kenbot.commands.webhooks;

import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.database.Database;

public class CreateWebhook implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		
		String DefaultAvatarURL = "";
		
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage(":x: You must have the Administrator permission to use this command.").queue();
            return;
        }
		
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		HashMap<String,String> webhook = Database.Webhooks.get(event.getGuild().getIdLong(), args.get(0).toLowerCase());
		if(webhook != null) {
			event.getChannel().sendMessage(":x: This webhook already exists.").queue();
            return;
		}else {
			Database.Webhooks.set(event.getGuild().getIdLong(),DefaultAvatarURL,"Default Webhook", args.get(0));
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setTitle("Created webhook with prefix "+args.get(0))
				.appendDescription("To change the webhook's name, run `Ken setWebhookName <prefix> <name>`.")
				.appendDescription("\n\nTo change the webhook's picture, run `Ken setWebhookAvatar <prefix>`. Then after sending that message, in the same channel, upload the picutre of the avatar you want.");
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "createwebhook";
	}
	
	public String getUsage() {
		return "CreateWebhook <prefix>";
	}
	
	public String getDescription() {
		return "Create a webhook that when a message is said in chat starting with its prefix, is sends the message as if the webhook was sending it using the webhooks Name and Avatar URL";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}
