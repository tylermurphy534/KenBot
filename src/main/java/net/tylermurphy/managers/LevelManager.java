package net.tylermurphy.managers;

import java.util.Date;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.database.DatabaseManager;

public class LevelManager {
	
	Date date = new Date();
	
	public void handleMessage(GuildMessageReceivedEvent event) {
		String time = DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "LVLTIME");
		if(time == null || time.equals("") || date.getTime() - Long.parseLong(time) > 60 * 1000) {
			handleMember(event);
			return;
		}
		return;
	}
	
	private void handleMember(GuildMessageReceivedEvent event) {
		DatabaseManager.UserSettings.set(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "LVLTIME", String.valueOf(date.getTime()));
		String unparsedXp = DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP");
		int xp = 0;
		if(!unparsedXp.equals("")) xp = Integer.parseInt(unparsedXp);
		xp++;
		DatabaseManager.UserSettings.set(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP", String.valueOf(xp));
		if(getLevel(xp) > getLevel(xp-1)) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle("Level Up!")
					.setDescription(String.format("%s has reached level %s!", event.getAuthor(), getLevel(xp)));
			 try{ event.getChannel().sendMessage(embed.build()).queue(); }catch(Exception e) {};
		}
	}
	
	public static int getLevel(int xp) {
		return (int) (Math.max(Math.exp(Math.log(xp/20)/1.5),0))+1;
	}
	
}
