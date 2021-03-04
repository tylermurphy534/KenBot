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
import net.tylermurphy.database.Database;

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
	    put("pet","Pet :pinching_hand:");
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
		
		HashMap<String,Integer> stats = Database.SocialStats.getAll(userId, otherId);
		HashMap<String,Integer> stats2 = Database.SocialStats.getAll(otherId, userId);
		
		EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
		
		embed.setAuthor("Social Stats With "+ mentionedMembers.get(0).getUser().getName(), null, mentionedMembers.get(0).getUser().getAvatarUrl());
		
		int total = 0, total2 = 0;
		
		for(Entry<String, String> entry : labels.entrySet()) {
			String title = entry.getValue();
			int value = stats.get(entry.getKey()) == null ? 0 : stats.get(entry.getKey());
			total += value;
			int value2 = stats2.get(entry.getKey()) == null ? 0 : stats2.get(entry.getKey());
			total2 += value2;
			embed.addField(title, (value+value2)+"", true);
		}
		
		embed.addField(event.getAuthor().getName() + "'s social command uses", total+"", false);
		embed.addField(mentionedMembers.get(0).getUser().getName() + "'s social command uses", total2+"", false);
		
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
