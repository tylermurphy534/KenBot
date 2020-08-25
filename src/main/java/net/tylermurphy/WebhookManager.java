package net.tylermurphy;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.database.DatabaseManager;

public class WebhookManager {
	
	private HashMap<Long,WebhookClient> clients = new HashMap<Long,WebhookClient>();

	public void handleMessage(GuildMessageReceivedEvent event) {
		long guildId = event.getGuild().getIdLong();
		TextChannel channel = event.getChannel();
		String prefix, message;
		try {
			prefix = event.getMessage().getContentRaw().split(" ")[0];
			message = event.getMessage().getContentRaw().substring(prefix.length()+1);
		} catch(Exception e) { return; }
		HashMap<String,String> webhookData = DatabaseManager.Webhooks.get(guildId, prefix);
		if(webhookData == null) return;
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getMessage().delete().queue();
            channel.sendMessage(":x: You must be an administrator to send webhook messages").queue(m -> {
				m.delete().queueAfter(5, TimeUnit.SECONDS);
			});
            return;
        }
		List<Webhook> webhooks = channel.retrieveWebhooks().complete();
		Webhook webhook = null;
		if(webhooks.isEmpty()) {
			webhook = channel.createWebhook("Ken Webhook").complete();
		}else {
			webhook = webhooks.get(0);
		}
		String url = webhook.getUrl();
		WebhookClient client = clients.computeIfAbsent(guildId, k -> createClient(url));
		WebhookMessageBuilder builder = new WebhookMessageBuilder();
		builder.setUsername(webhookData.get("Name"));
		builder.setAvatarUrl(webhookData.get("AvatarURL"));
		builder.setContent(message);
		client.send(builder.build());
		event.getMessage().delete().queue();
	}
	
	private WebhookClient createClient(String url) {
		WebhookClientBuilder builder = new WebhookClientBuilder(url);
		builder.setThreadFactory((job) -> {
		    Thread thread = new Thread(job);
		    thread.setName("Default Name");
		    thread.setDaemon(true);
		    return thread;
		});
		builder.setWait(true);
		return builder.build();
	}
	
}
