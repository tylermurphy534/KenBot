package net.tylermurphy.commands.moderation;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Warns implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		
		if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
			channel.sendMessage(":x: You must have the kick members permission to use this command.").queue();
			return;
		}
		
		Map<Integer,String> warnings = DatabaseManager.Warnings.getAll(target.getUser().getIdLong(), event.getGuild().getIdLong());
		
		if(warnings.size() < 1) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("%s has 0 warnings.", target));
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		String message ="**Warn ID\t Reason For Warning**";
		for (Entry<Integer, String> entry : warnings.entrySet()) {
			message = String.format("%s\n`%s`\t%s", message, entry.getKey(), entry.getValue());
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setDescription(message);
		channel.sendMessage(embed.build()).queue();
		return;
		
	}

	public String getInvoke() {
		return "warns";
	}
	
	public String getUsage() {
		return "Warns <@User>";
	}
	
	public String getDescription() {
		return "See how many warns a user has";
	}
	
	public Permission requiredPermission() {
		return Permission.KICK_MEMBERS;
	}

}
