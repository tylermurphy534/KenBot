package net.tylermurphy.commands.webhooks;

import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class CreateWebhook implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		
		String DefaultAvatarURL = "";
		
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage(":x: You must have the Administrator permission to use this command.").queue();
            return;
        }
		
		if(args.isEmpty()) {
			channel.sendMessage(":x: Missing Arguments").queue();
			return;
		}
		
		HashMap<String,String> webhook = DatabaseManager.Webhooks.get(event.getGuild().getIdLong(), args.get(0).toLowerCase());
		if(webhook != null) {
			event.getChannel().sendMessage(":x: This webhook already exists.").queue();
            return;
		}else {
			DatabaseManager.Webhooks.insert(event.getGuild().getIdLong(),DefaultAvatarURL,"Default Webhook", args.get(0));
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Created webhook with prefix "+args.get(0));
		channel.sendMessage(embed.build()).queue();
	}

	public String getInvoke() {
		return "createwebhook";
	}

}
