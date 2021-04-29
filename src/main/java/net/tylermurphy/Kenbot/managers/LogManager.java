package net.tylermurphy.Kenbot.managers;

import java.util.Date;

import javax.annotation.Nonnull;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateParentEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.Kenbot.database.Database;

public class LogManager extends ListenerAdapter {
	
    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s updated their message to\n'%s'",
    					event.getAuthor(),
    					event.getMessage().getContentRaw()
    					));
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s was deleted",
    					event.getChannel().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdateName(@Nonnull TextChannelUpdateNameEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had name changed from %s to %s",
    					event.getChannel(),
    					event.getOldName(),
    					event.getNewName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdateTopic(@Nonnull TextChannelUpdateTopicEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had topic changed from %s to %s",
    					event.getChannel(),
    					event.getOldTopic(),
    					event.getNewTopic()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdatePosition(@Nonnull TextChannelUpdatePositionEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had position changed from %s to %s",
    					event.getChannel(),
    					event.getOldPosition(),
    					event.getNewPosition()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdateNSFW(@Nonnull TextChannelUpdateNSFWEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had NSFW set to %s",
    					event.getChannel(),
    					event.getNewValue()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdateParent(@Nonnull TextChannelUpdateParentEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had parent set to %s",
    					event.getChannel(),
    					event.getNewParent()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelUpdateSlowmode(@Nonnull TextChannelUpdateSlowmodeEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s had Slowmode set to %s",
    					event.getChannel(),
    					event.getNewSlowmode()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Text channel %s was created",
    					event.getChannel().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s was created",
    					event.getChannel().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelUpdateName(@Nonnull VoiceChannelUpdateNameEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s had name set  %s to %s",
    					event.getChannel(),
    					event.getOldName(),
    					event.getNewName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelUpdatePosition(@Nonnull VoiceChannelUpdatePositionEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s had position changed from %s to %s",
    					event.getChannel(),
    					event.getOldPosition(),
    					event.getNewPosition()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelUpdateUserLimit(@Nonnull VoiceChannelUpdateUserLimitEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s had user limit changed from %s to %s",
    					event.getChannel(),
    					event.getOldUserLimit(),
    					event.getNewUserLimit()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelUpdateBitrate(@Nonnull VoiceChannelUpdateBitrateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s had bitrate changed from %s to %s",
    					event.getChannel(),
    					event.getOldBitrate(),
    					event.getNewBitrate()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelUpdateParent(@Nonnull VoiceChannelUpdateParentEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s had parent set to %s",
    					event.getChannel(),
    					event.getNewParent()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Voice channel %s was created",
    					event.getChannel().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Category %s was deleted",
    					event.getCategory().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onCategoryUpdateName(@Nonnull CategoryUpdateNameEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Category %s had name changed from %s to %s",
    					event.getCategory().getName(),
    					event.getOldName(),
    					event.getNewName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onCategoryUpdatePosition(@Nonnull CategoryUpdatePositionEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Category %s has position changed from %s to %s",
    					event.getCategory().getName(),
    					event.getOldPosition(),
    					event.getNewPosition()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onCategoryCreate(@Nonnull CategoryCreateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Categoy %s was created",
    					event.getCategory().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildBan(@Nonnull GuildBanEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s was banned",
    					event.getUser().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUnban(@Nonnull GuildUnbanEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s was unbanned",
    					event.getUser().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s was removed from the guild",
    					event.getUser().getName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	String roles = "";
    	for(Role r : event.getRoles()) {
    		roles = roles + r + " ";
    	}
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s had roles %s added",
    					event.getUser(),
    					roles
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	String roles = "";
    	for(Role r : event.getRoles()) {
    		roles = roles + r + " ";
    	}
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s had roles %s removed",
    					event.getUser(),
    					roles
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUpdateAfkChannel(@Nonnull GuildUpdateAfkChannelEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Guild had AFK Channel set to %s",
    					event.getNewAfkChannel()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUpdateSystemChannel(@Nonnull GuildUpdateSystemChannelEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Guild had system channel set to %s",
    					event.getNewSystemChannel()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUpdateAfkTimeout(@Nonnull GuildUpdateAfkTimeoutEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Guild had AFK Timeout set to %s",
    					event.getNewAfkTimeout()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUpdateName(@Nonnull GuildUpdateNameEvent event){
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Guild had name changed from %s to %s",
    					event.getOldName(),
    					event.getNewName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildUpdateOwner(@Nonnull GuildUpdateOwnerEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Guild ownership has been handed to %s",
    					event.getNewOwner()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildInviteCreate(@Nonnull GuildInviteCreateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s created an invite",
    					event.getInvite().getInviter()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onRoleCreate(@Nonnull RoleCreateEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Role %s created",
    					event.getRole()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Role %s deleted",
    					event.getRole()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onRoleUpdateColor(@Nonnull RoleUpdateColorEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Role %s had color set to %s",
    					event.getRole(),
    					event.getNewColor()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"Role name changed from %s to %s",
    					event.getOldName(),
    					event.getNewName()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s joined %s at %s",
    					event.getMember(),
    					event.getChannelJoined(),
    					new Date().toString()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s left %s at %s",
    					event.getMember(),
    					event.getChannelLeft(),
    					new Date().toString()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s was server muted",
    					event.getMember()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildVoiceGuildDeafen(@Nonnull GuildVoiceGuildDeafenEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s was server defened",
    					event.getMember()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {
    	String id = Database.GuildSettings.get(event.getGuild().getIdLong(), "logChannel");
		if(id == null || id.equalsIgnoreCase("")) return;
		TextChannel channel = event.getJDA().getTextChannelById(id);
    	if(channel == null) return;
    	EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    			.setDescription(String.format(
    					"%s had nickname changed to %s",
    					event.getUser().getName()+"#"+event.getUser().getDiscriminator(),
    					event.getNewNickname()
    					));;
    	channel.sendMessage(embed.build()).queue();
    }
}