package net.tylermurphy.commands.moderation;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class DelSelfRole implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		
		if(args.isEmpty() || args.size() < 2) {
			channel.sendMessage(":x: Missing Arguments.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage(":x: You dont have the administrator permission to use this command.").queue();
			return;
		}
		
		if (!selfMember.hasPermission(Permission.MANAGE_ROLES)) {
			channel.sendMessage(":x: I don't have permission to manage roles.").queue();
			return;
		}
		long guildId = event.getGuild().getIdLong();
		long channelId,messageId;
		
		try { channelId = Long.parseLong(args.get(0)); } 
		catch (Exception e) {
			channel.sendMessage(":x: The channel id must be a number.").queue();
			return;
		}
		
		try { messageId = Long.parseLong(args.get(1)); } 
		catch (Exception e) {
			channel.sendMessage(":x: The message id must be a number.").queue();
			return;
		}
		String locationId = guildId+""+channelId+""+messageId;
		List<String> reactions = DatabaseManager.SelfRoles.getAll(locationId);
		if( reactions != null) {
			DatabaseManager.SelfRoles.del(locationId);
			TextChannel targetChannel = event.getJDA().getTextChannelById(channelId);
			targetChannel.retrieveMessageById(messageId).queue((message) -> {
				List<MessageReaction> rcs = message.getReactions();
				for(MessageReaction r : rcs) {
					message.removeReaction(r.getReactionEmote().getName()).queue();
				}
			});
			channel.sendMessage("Self Roles removes sucessfully.").queue();
		} else {
			channel.sendMessage(":x: There are no self roles on this message.").queue();
		}
		
	}

	public String getInvoke() {
		return "delselfrole";
	}

}
