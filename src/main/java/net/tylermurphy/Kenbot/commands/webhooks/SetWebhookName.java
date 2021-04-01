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

public class SetWebhookName implements ICommand {

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
		String name = String.join(" ", args.subList(1, args.size()));
		Database.Webhooks.set(event.getGuild().getIdLong(), webhook.get("AvatarURL"), name, args.get(0));
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription("Set webhook name with prefix "+args.get(0)+" to\n`"+name+"`");
		channel.sendMessage(embed.build()).queue();
		
	}

	public String getInvoke() {
		return "SetWebhookName";
	}
	
	public String getUsage() {
		return "SetWebhookName <prefix> <name>";
	}
	
	public String getDescription() {
		return "Set a webhooks name";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}
