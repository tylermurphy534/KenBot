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
		
		String message = "";
		for (Entry<Integer, String> entry : warnings.entrySet()) {
			message = String.format("`Id: %s`\t%s\n", message, entry.getKey(), entry.getValue());
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.setAuthor(target.getUser().getAvatarUrl(), null, "Warns for "+target.getEffectiveName())
				.setDescription(message)
				.setFooter(target.getEffectiveName()+" has "+warnings.size()+" warnings.");
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
