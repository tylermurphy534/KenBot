package net.tylermurphy.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.database.DatabaseManager;
import net.tylermurphy.image.ImageGenerator;

public class LevelManager {
	
	private static List<Long> blockedIds;
	
	static {
		blockedIds = new ArrayList<Long>();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				blockedIds.clear();
			}
		};
		timer.schedule(task, 1000 * 60, 1000 * 60);
	}
	
	public void handleMessage(GuildMessageReceivedEvent event) {
		String value = DatabaseManager.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value != null && value.equals("false")) return;
		if(!blockedIds.contains(event.getAuthor().getIdLong())) {
			blockedIds.add(event.getAuthor().getIdLong());
			handleMember(event);
			return;
		}
		return;
	}
	
	private void handleMember(GuildMessageReceivedEvent event) {
		String unparsedXp = DatabaseManager.UserSettings.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP");
		int xp = 0;
		if(!unparsedXp.equals("")) xp = Integer.parseInt(unparsedXp);
		xp++;
		DatabaseManager.UserSettings.set(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), "XP", String.valueOf(xp));
		if(getLevel(xp) > getLevel(xp-1) && getLevel(xp) > 1) {
			sendLevelUpMessage(event, getLevel(xp));
		}
	}
	
	private void sendLevelUpMessage(GuildMessageReceivedEvent event, int level) {
		try{ 
			 byte[] img = ImageGenerator.GenerateLevelUpImage(1, event.getAuthor());
			 
			 event.getChannel()
			 	.sendMessageFormat(":tada:**| %s** leveled up!", event.getAuthor().getName())
			 	.addFile(img, "levelUp.png")
			 	.queue();
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 };
	}
	
	public static int getLevel(int xp) {
		int level = 0;
		while(xp > level*250) {
			level++;
		}
		return level;
	}
	
}
