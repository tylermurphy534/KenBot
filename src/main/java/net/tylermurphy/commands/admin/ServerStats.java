package net.tylermurphy.commands.admin;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class ServerStats extends ListenerAdapter implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		Member member = event.getMember();

		if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            event.getChannel().sendMessage(":x: You must have the Manage Server permission to use this command.").queue();
            return;
        }
		
		try {
			boolean enabled = Database.GuildSettings.get(event.getGuild().getIdLong(), "categoryId").equals("") ? false : true;
			if(enabled) disable(args,event);
			else enable(args,event);
		} catch (Exception e){
			enable(args,event);
		}
	}
	
	private void enable(List<String> args, GuildMessageReceivedEvent event) {
		Category category = event.getGuild().createCategory("Server Stats").complete();
		VoiceChannel members = event.getGuild().createVoiceChannel("Server Members").complete();
		VoiceChannel bots = event.getGuild().createVoiceChannel("Server Bots").complete();
		VoiceChannel users = event.getGuild().createVoiceChannel("Server Users").complete();
		Database.GuildSettings.set(event.getGuild().getIdLong(), "membersChannelId", String.valueOf(members.getIdLong()));
		Database.GuildSettings.set(event.getGuild().getIdLong(), "botsChannelId", String.valueOf(bots.getIdLong()));
		Database.GuildSettings.set(event.getGuild().getIdLong(), "usersChannelId", String.valueOf(users.getIdLong()));
		Database.GuildSettings.set(event.getGuild().getIdLong(), "categoryId", String.valueOf(category.getIdLong()));
		List<Member> serverMembers = event.getGuild().getMembers();
		int numBots = 0;
		for(Member member : serverMembers) {
			if(!member.getUser().isBot())
				continue;
			numBots++;
		}
		members.getManager().setParent(category).queue();
		bots.getManager().setParent(category).queue();
		users.getManager().setParent(category).queue();
		category.getManager().setPosition(0).queue();
		members.getManager().setName("Server Members: " + serverMembers.size()).queue();
		bots.getManager().setName("Server Bots: " + numBots).queue();
		users.getManager().setName("Server Users: " + (serverMembers.size()-numBots)).queue();
		event.getChannel().sendMessage(":white_check_mark: Enabled Server Stats").queue();
	}
	
	private void disable(List<String> args, GuildMessageReceivedEvent event) {
		try {
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "membersChannelId"))).delete().queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "botsChannelId"))).delete().queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "usersChannelId"))).delete().queue();
			event.getGuild().getCategoryById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "categoryId"))).delete().queue();
			Database.GuildSettings.remove(event.getGuild().getIdLong(), "membersChannelId");
			Database.GuildSettings.remove(event.getGuild().getIdLong(), "botsChannelId");
			Database.GuildSettings.remove(event.getGuild().getIdLong(), "usersChannelId");
			Database.GuildSettings.remove(event.getGuild().getIdLong(), "categoryId");
			event.getChannel().sendMessage(":x: Disabled Server Stats").queue();
		}catch(NullPointerException e) {
			event.getChannel().sendMessage(":x: An Unexpected Error Has Occured").queue();
		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		List<Member> serverMembers = event.getGuild().getMembers();
		int numBots = 0;
		for(Member member : serverMembers) {
			if(!member.getUser().isBot())
				continue;
			numBots++;
		}
		try {
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "membersChannelId"))).getManager().setName("Server Members: " + serverMembers.size()).queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "botsChannelId"))).getManager().setName("Server Bots: " + numBots).queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "usersChannelId"))).getManager().setName("Server Users: " + (serverMembers.size()-numBots)).queue();
		}catch(Exception e) {}
	}
	
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		List<Member> serverMembers = event.getGuild().getMembers();
		int numBots = 0;
		for(Member member : serverMembers) {
			if(!member.getUser().isBot())
				continue;
			numBots++;
		}
		try {
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "membersChannelId"))).getManager().setName("Server Members: " + serverMembers.size()).queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "botsChannelId"))).getManager().setName("Server Bots: " + numBots).queue();
			event.getGuild().getVoiceChannelById(Long.parseLong(Database.GuildSettings.get(event.getGuild().getIdLong(), "usersChannelId"))).getManager().setName("Server Users: " + (serverMembers.size()-numBots)).queue();
		}catch(Exception e) {}
	}
	
	public String getInvoke() {
		return "serverstats";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Enable / Disable Server Stats";
	}
	
	public Permission requiredPermission() {
		return Permission.ADMINISTRATOR;
	}

}
