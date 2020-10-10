package net.tylermurphy.commands.moderation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vdurmont.emoji.EmojiParser;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class SelfRole extends ListenerAdapter implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		System.out.println(args.get(3));
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		
		if(args.isEmpty() || args.size() < 4) {
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
		long channelId,messageId,roleId;
		
		
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
		
		try {
			List<Role> roles = event.getMessage().getMentionedRoles();
			List<Role> roles2 = event.getGuild().getRolesByName(args.get(2), true);
			if(roles.isEmpty() && roles2.isEmpty()) {
				roleId = Long.parseLong(args.get(2));
			}else if(!roles.isEmpty()){
				roleId = roles.get(0).getIdLong();
			}else {
				roleId = roles2.get(0).getIdLong();
			}
		}catch (Exception e) {
			channel.sendMessage(":x: The role must be either a role id, role name, or a role mention.").queue();
			return;
		}
		
		String reaction;
		try {
			Message message = event.getMessage();
			String content = message.getContentRaw();
			List<String> emojis = EmojiParser.extractEmojis(content);
			List<String> customEmoji = message.getEmotes().stream()
			        .map((emote) -> emote.getName() + ":" + emote.getId())
			        .collect(Collectors.toList());
			List<String> merged = new ArrayList<>();
			merged.addAll(emojis);
			merged.addAll(customEmoji);
			if(merged.size() > 1) {
				channel.sendMessage(":x: The message can only have one standard or custom discord emote.").queue();
				return;
			}
			if(merged.size() < 1) {
				channel.sendMessage(":x: The message must have one standard or custom discord emote.").queue();
				return;
			}
			reaction = merged.get(0);
		}catch (Exception e) {
			e.printStackTrace();
			channel.sendMessage(":x: Argument 3 is not a standard or custom discord emote.").queue();
			return;
		}
		
		String locationId = guildId+""+channelId+""+messageId;
        String result = DatabaseManager.SelfRoles.get(locationId, reaction);
		if(result != null) {
			channel.sendMessage(":x: There is already a self role for this reaction on this message.").queue();
			return;
		}
		
		TextChannel targetChannel = event.getJDA().getTextChannelById(channelId);
		if(targetChannel == null) {
			channel.sendMessage(":x: Invalid Channel Id.").queue();
			return;
		}
		targetChannel.retrieveMessageById(messageId).queue((message) -> {
			    message.addReaction(reaction).queue();
			    String locationId2 = guildId+""+channelId+""+messageId;
		        DatabaseManager.SelfRoles.set(locationId2, roleId, reaction);
				Role role = event.getJDA().getRoleById(roleId);
				EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
						.setDescription(String.format(
								"%s set as reaction for %s sucessfully! Run the delselfrole command to remove it.", 
								reaction, 
								role));
				channel.sendMessage(embed.build()).queue();
			}, (failure) -> {
				channel.sendMessage(":x: The message id was not found in the channel.").queue();
			});
	}
	
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(event.getUser().isBot()) return;
		String emote = event.getReaction().getReactionEmote().getName();
        long guildId = event.getGuild().getIdLong();
        long channelId = event.getChannel().getIdLong();
        long messageId = event.getMessageIdLong();
        String locationId = guildId+""+channelId+""+messageId;
        System.out.println(locationId);
        String result = DatabaseManager.SelfRoles.get(locationId, emote);
        if(result == null) return;
        Role role = event.getJDA().getRoleById(Long.parseLong(result));
        try {
        	event.getGuild().addRoleToMember(event.getMember(), role).complete();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if(event.getUser().isBot()) return;
		String emote = event.getReaction().getReactionEmote().getName();
        long guildId = event.getGuild().getIdLong();
        long channelId = event.getChannel().getIdLong();
        long messageId = event.getMessageIdLong();
        String locationId = guildId+""+channelId+""+messageId;
        String result = DatabaseManager.SelfRoles.get(locationId, emote);
        if(result == null) return;
        Role role = event.getJDA().getRoleById(Long.parseLong(result));
        try {
        	event.getGuild().removeRoleFromMember(event.getMember(), role).complete();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	public String getInvoke() {
		return "selfrole";
	}

}
