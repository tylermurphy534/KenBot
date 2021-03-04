package net.tylermurphy.commands.relationship;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.Database;

public class Status implements ICommand{

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			mentionedMembers.add(event.getMember());
		}
		long userId = mentionedMembers.get(0).getUser().getIdLong();
		String loveId = Database.UserSettings.get(userId, 0, "LoveId");
		if(loveId == null) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(mentionedMembers.get(0).getUser().getName()+" is currently not in an relationship.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		boolean inlove = false;
		if(Database.UserSettings.get(Long.parseLong(loveId), 0, "LoveId").equals(String.valueOf(userId)))
			inlove = true;
			
		long time = new Date().getTime() - Long.parseLong(Database.UserSettings.get(userId, 0, "LoveTime"));
		HashMap<String,Integer> stats = Database.SocialStats.getAll(userId, Long.parseLong(loveId));
		HashMap<String,Integer> stats2 = Database.SocialStats.getAll(Long.parseLong(loveId), userId);
		int xp = 0,xp2 = 0;
		if(stats != null) { xp = (int) ((time/2500000)+5*(stats.getOrDefault("boop",0)+stats.getOrDefault("cookie",0)+stats.getOrDefault("gift",0)+stats.getOrDefault("hug",0)+stats.getOrDefault("kiss",0)+stats.getOrDefault("pet",0)-stats.getOrDefault("slap",0)+stats.getOrDefault("tickle",0)+stats.getOrDefault("highfive",0)+stats.getOrDefault("lick",0)-(stats.getOrDefault("punch",0)))); }
		if(stats2 != null) { xp2 = (int) ((time/2500000)+5*(stats2.getOrDefault("boop",0)+stats2.getOrDefault("cookie",0)+stats2.getOrDefault("gift",0)+stats2.getOrDefault("hug",0)+stats2.getOrDefault("kiss",0)+stats2.getOrDefault("pet",0)-stats2.getOrDefault("slap",0)+stats2.getOrDefault("tickle",0)+stats2.getOrDefault("highfive",0)+stats2.getOrDefault("lick",0)-(stats2.getOrDefault("punch",0)))); }
		int level = getLevel((xp+xp2)/2);
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		embed.appendDescription("**Love Intrests**\n");
		
		if(inlove) {
			embed.addField("Status",String.format("%s is married to %s\n\n", mentionedMembers.get(0).getUser(), "<@"+Long.parseLong(loveId)+">"), false);
			embed.addField("Level", level+"", false);
			embed.addField("Time in love", getTime(time), false);
		} else {
			embed.addField("Status", String.format("%s has proposed to %s and is still waiting\n\n", mentionedMembers.get(0).getUser(), "<@"+Long.parseLong(loveId)+">"), false);
		}
		
		channel.sendMessage(embed.build()).queue();
	}
	
	private String getTime(long time) {
		int seconds = (int) (time/1000);
		int minutes = (int) (seconds/60);
		int hours = (int) (minutes/60);
		int days = (int) (hours/24);
		int weeks = (int) (days/7);
		int months = (int) (weeks/4);
		int years = (int) (months/12);
		int pm = minutes;
		int ph = hours;
		int pd = days;
		int pw = weeks;
		int pmo = months;
		months -= (years*12);
		weeks -= (pmo*4);
		days -= (pw*7);
		hours -= (pd*24);
		minutes -= (ph*60);
		seconds -= (pm*60);
		String timeAgo = "ago";
		if(seconds > 0)
			timeAgo = String.format("%s seconds ago", seconds);
		if(minutes > 0)
			timeAgo = String.format("%s minutes, %s", minutes, timeAgo);
		if(hours > 0) 
			timeAgo = String.format("%s hours, %s", hours, timeAgo);
		if(days > 0) 
			timeAgo = String.format("%s days, %s", days, timeAgo);
		if(weeks > 0) 
			timeAgo = String.format("%s weeks, %s", weeks, timeAgo);
		if(months > 0)
			timeAgo = String.format("%s months, %s", months, timeAgo);
		if(years > 0)
			timeAgo = String.format("%s years, %s", years, timeAgo);
		return timeAgo;
	}
	
	public static int getLevel(int xp) {
		int level = 0;
		while(xp > level*250) {
			level++;
		}
		return level;
	}

	public String getInvoke() {
		return "status";
	}
	
	public String getUsage() {
		return "Status <@User>";
	}
	
	public String getDescription() {
		return "Get someones relationship status";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
