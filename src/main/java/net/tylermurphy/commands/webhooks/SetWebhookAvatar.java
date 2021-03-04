package net.tylermurphy.commands.webhooks;

import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class SetWebhookAvatar extends ListenerAdapter implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		
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
		if(webhook==null) {
			channel.sendMessage(":x: Webhook does not exist").queue();
			return;
		}
		
		Database.GuildSettings.set(event.getGuild().getIdLong(), "WebhookCache", args.get(0)+":"+event.getChannel().getId()+":"+event.getAuthor().getId());
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Now upload the image of the webhook you want to use. Or type `cancel` to cancel.");
		channel.sendMessage(embed.build()).queue();
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		String reply = Database.GuildSettings.get(event.getGuild().getIdLong(), "WebhookCache");
		if(reply == null || reply.equals("")) return;
		String[] cache = reply.split(":");
		if(!event.getAuthor().getId().equals(cache[2])) return;
		HashMap<String,String> webhook = Database.Webhooks.get(event.getGuild().getIdLong(), cache[0].toLowerCase());
		if(webhook==null) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), "WebhookCache", "");
			return;
		}
		if(event.getMessage().getContentRaw().equalsIgnoreCase("cancel")) {
			Database.GuildSettings.set(event.getGuild().getIdLong(), "WebhookCache", "");
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("Canceled avatar change.");
			event.getChannel().sendMessage(embed.build()).queue();
			return;
		}
		List<Attachment> attachments = event.getMessage().getAttachments();
		Attachment image = null;
		for(Attachment a : attachments) {
			if(a.isImage()) {
				image = a;
				break;
			}
		}
		if(image == null) 
			return;
		String url = image.getUrl();
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Changed Webhook Avatar. If you ever delete the image, the avatar on the webhook will soon disapear as well.");
		event.getChannel().sendMessage(embed.build()).queue();
		Database.Webhooks.set(event.getGuild().getIdLong(), url, webhook.get("Name"), cache[0].toLowerCase());
		Database.GuildSettings.set(event.getGuild().getIdLong(), "WebhookCache", "");
	}

	public String getInvoke() {
		return "SetWebhookAvatar";
	}
	
	public String getUsage() {
		return "SetWebhookAvatar <prefix>";
	}
	
	public String getDescription() {
		return "Set a webhooks avatar";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}
