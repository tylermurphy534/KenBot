package net.tylermurphy.commands.social;

import java.util.HashMap;
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

public class Stats implements ICommand{
	
	@SuppressWarnings("serial")
	private final Map<String, String> labels = new HashMap<String, String>() {{
	    put("boop", "Boop :point_right:");
	    put("cookie", "Cookie :cookie:");
	    put("gift","Gift :gift:");
	    put("highfive","High Five :raised_hands:");
	    put("hug","Hug :people_hugging:");
	    put("kiss","Kiss :kissing_heart:");
	    put("lick","Lick :flushed:");
	    put("pet","Pet :pinching_hands:");
	    put("punch","Punch :right_fist:");
	    put("slap","Slap :clap:");
	    put("tickle","Tickle :laughing:");
	}};

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		long userId = event.getAuthor().getIdLong();
		long otherId = mentionedMembers.get(0).getUser().getIdLong();
		
		HashMap<String,Integer> stats = DatabaseManager.SocialStats.getAll(userId, otherId);
		HashMap<String,Integer> stats2 = DatabaseManager.SocialStats.getAll(otherId, userId);
		
		if(stats.size() < 1 && stats2.size() < 1) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
			embed.setDescription("You have no social command history.");
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		
		embed.setAuthor("Social Stats With "+ mentionedMembers.get(0).getUser().getName(), null, mentionedMembers.get(0).getUser().getAvatarUrl());
		for(Entry<String,Integer> entry : stats.entrySet()) {
			embed.addField(labels.get(entry.getKey()), entry.getValue()+stats2.get(entry.getKey())+"", true);
		}
		
		embed.addField(event.getAuthor().getName() + "'s social command uses", stats.size()+"", false);
		embed.addField(mentionedMembers.get(0).getUser().getName() + "'s social command uses", stats2.size()+"", false);
		
		channel.sendMessage(embed.build()).queue();
	}
	
	public static int getLevel(int xp) {
		int level = 0;
		while(xp > level*250) {
			level++;
		}
		return level;
	}

	public String getInvoke() {
		return "Stats";
	}
	
	public String getUsage() {
		return "Stats <@User>";
	}
	
	public String getDescription() {
		return "Get your social command counters with someone else";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
