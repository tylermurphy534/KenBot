package net.tylermurphy.commands.social;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.database.DatabaseManager;

public class Ship implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<User> mentionedMembers = event.getMessage().getMentionedUsers();
		long userId = event.getAuthor().getIdLong();
		String loveId = DatabaseManager.UserSettings.get(userId, 0, "LoveId");
		if(!mentionedMembers.isEmpty()) {
			long otherId = mentionedMembers.get(0).getIdLong();
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription(String.format("You set %s as your love intrest", mentionedMembers.get(0)));
			channel.sendMessage(embed.build()).queue();
			DatabaseManager.UserSettings.set(userId, 0, "LoveId", String.valueOf(otherId));
			if(mentionedMembers.get(0).getIdLong() == event.getAuthor().getIdLong()) {
				DatabaseManager.UserSettings.set(userId, 0, "LoveTime", String.valueOf(new Date().getTime()));
			}
			else if(DatabaseManager.UserSettings.get(otherId, 0, "LoveId").equals(String.valueOf(userId))) {
				DatabaseManager.UserSettings.set(userId, 0, "LoveTime", String.valueOf(new Date().getTime()));
				DatabaseManager.UserSettings.set(otherId, 0, "LoveTime", String.valueOf(new Date().getTime()));
			}
			return;
		}
		if(loveId.equals("")) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You currently have no love intrest.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(!args.isEmpty() && args.get(0).equals("remove")) {
			DatabaseManager.UserSettings.set(userId, 0, "LoveId", "");
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setDescription("You removed your love intrest.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		if(DatabaseManager.UserSettings.get(Long.parseLong(loveId), 0, "LoveId").equals(String.valueOf(userId))) {
			long time = new Date().getTime() - Long.parseLong(DatabaseManager.UserSettings.get(userId, 0, "LoveTime"));
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
			weeks -= (pw*4);
			days -= (pmo*7);
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
			HashMap<String,Integer> stats = DatabaseManager.SocialStats.getAll(userId, Long.parseLong(loveId));
			HashMap<String,Integer> stats2 = DatabaseManager.SocialStats.getAll(Long.parseLong(loveId), userId);
			int xp = 0,xp2 = 0;
			if(stats != null) { xp = (int) ((time/100000)+5*(stats.getOrDefault("boop",0)+stats.getOrDefault("cookie",0)+stats.getOrDefault("gift",0)+stats.getOrDefault("hug",0)+stats.getOrDefault("kiss",0)+stats.getOrDefault("pet",0)-stats.getOrDefault("slap",0)+stats.getOrDefault("tickle",0)+stats.getOrDefault("highfive",0)+stats.getOrDefault("lick",0)-(stats.getOrDefault("punch",0)*3))); }
			if(stats2 != null) { xp2 = (int) ((time/100000)+5*(stats2.getOrDefault("boop",0)+stats2.getOrDefault("cookie",0)+stats2.getOrDefault("gift",0)+stats2.getOrDefault("hug",0)+stats2.getOrDefault("kiss",0)+stats2.getOrDefault("pet",0)-stats2.getOrDefault("slap",0)+stats2.getOrDefault("tickle",0)+stats2.getOrDefault("highfive",0)+stats2.getOrDefault("lick",0)-(stats2.getOrDefault("punch",0)*3))); }
			int level = (int) (Math.max(Math.exp(Math.log((xp+xp2)/10)/1.5),0))+1;
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**Love Intrests**\n")
					.appendDescription(String.format("%s is in love with %s\n\n", event.getAuthor(), "<@"+Long.parseLong(loveId)+">"))
					.appendDescription("**Counters**\n")
					.appendDescription(String.format("Boops: %s\n", stats.getOrDefault("boop",0)+stats2.getOrDefault("boop",0)))
					.appendDescription(String.format("Cookies: %s\n", stats.getOrDefault("cookie",0)+stats2.getOrDefault("cookie",0)))
					.appendDescription(String.format("Gifts: %s\n", stats.getOrDefault("gift",0)+stats2.getOrDefault("gift",0)))
					.appendDescription(String.format("Hugs: %s\n", stats.getOrDefault("hug",0)+stats2.getOrDefault("hug",0)))
					.appendDescription(String.format("Kiss: %s\n", stats.getOrDefault("kiss",0)+stats2.getOrDefault("kiss",0)))
					.appendDescription(String.format("Pets: %s\n", stats.getOrDefault("pet",0)+stats2.getOrDefault("pet",0)))
					.appendDescription(String.format("Slaps: %s\n", stats.getOrDefault("slap",0)+stats2.getOrDefault("slap",0)))
					.appendDescription(String.format("Tickles: %s\n", stats.getOrDefault("tickle",0)+stats2.getOrDefault("tickle",0)))
					.appendDescription(String.format("Licks: %s\n", stats.getOrDefault("lick",0)+stats2.getOrDefault("lick",0)))
					.appendDescription(String.format("High Fives: %s\n", stats.getOrDefault("highfive",0)+stats2.getOrDefault("highfive",0)))
					.appendDescription(String.format("Punches: %s\n\n", stats.getOrDefault("punch",0)+stats2.getOrDefault("punch",0)))
					.appendDescription("**Level**\n")
					.appendDescription(level + "\n\n")
					.appendDescription("**In Love Since**\n")
					.appendDescription(timeAgo);
			channel.sendMessage(embed.build()).queue();
			return;
		}
		HashMap<String,Integer> stats = DatabaseManager.SocialStats.getAll(userId, Long.parseLong(loveId));
		HashMap<String,Integer> stats2 = DatabaseManager.SocialStats.getAll(Long.parseLong(loveId), userId);
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
				.appendDescription("**Love Intrests**\n")
				.appendDescription(String.format("%s wants %s to love them\n\n", event.getAuthor(), "<@"+Long.parseLong(loveId)+">"))
				.appendDescription("**Counters**\n")
				.appendDescription(String.format("Boops: %s\n", stats.getOrDefault("boop",0)+stats2.getOrDefault("boop",0)))
				.appendDescription(String.format("Cookies: %s\n", stats.getOrDefault("cookie",0)+stats2.getOrDefault("cookie",0)))
				.appendDescription(String.format("Gifts: %s\n", stats.getOrDefault("gift",0)+stats2.getOrDefault("gift",0)))
				.appendDescription(String.format("Hugs: %s\n", stats.getOrDefault("hug",0)+stats2.getOrDefault("hug",0)))
				.appendDescription(String.format("Kiss: %s\n", stats.getOrDefault("kiss",0)+stats2.getOrDefault("kiss",0)))
				.appendDescription(String.format("Pets: %s\n", stats.getOrDefault("pet",0)+stats2.getOrDefault("pet",0)))
				.appendDescription(String.format("Slaps: %s\n", stats.getOrDefault("slap",0)+stats2.getOrDefault("slap",0)))
				.appendDescription(String.format("Tickles: %s\n", stats.getOrDefault("tickle",0)+stats2.getOrDefault("tickle",0)))
				.appendDescription(String.format("Licks: %s\n", stats.getOrDefault("lick",0)+stats2.getOrDefault("lick",0)))
				.appendDescription(String.format("High Fives: %s\n", stats.getOrDefault("highfive",0)+stats2.getOrDefault("highfive",0)))
				.appendDescription(String.format("Punches: %s\n\n", stats.getOrDefault("punch",0)+stats2.getOrDefault("punch",0)));
		channel.sendMessage(embed.build()).queue();
		return;
	}

	public String getInvoke() {
		return "ship";
	}

}
