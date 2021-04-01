package net.tylermurphy.Kenbot.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.database.Database;
import net.tylermurphy.Kenbot.image.ImageFactory;

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
		String value = Database.GuildSettings.get(event.getGuild().getIdLong(),"Leveling");
		if(value != null && value.equals("false")) return;
		if(!blockedIds.contains(event.getAuthor().getIdLong())) {
			blockedIds.add(event.getAuthor().getIdLong());
			handleMember(event);
			return;
		}
		return;
	}
	
	private void handleMember(GuildMessageReceivedEvent event) {
		int xp = Database.Xp.get(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
		xp++;
		Database.Xp.set(event.getAuthor().getIdLong(), event.getGuild().getIdLong(), xp);
		if(getLevel(xp) > getLevel(xp-1) && getLevel(xp) > 1) {
			sendLevelUpMessage(event, getLevel(xp));
		}
	}
	
	private void sendLevelUpMessage(GuildMessageReceivedEvent event, int level) {
		
		if(!Config.LEVEL_MESSAGING) return;
		
		if(!event.getChannel().canTalk(event.getGuild().getSelfMember())) {
			return;
		}
		
		try{ 
			 byte[] img = ImageFactory.GenerateLevelUpImage(level, event.getAuthor());
			 
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
		return (int) (Math.log((xp/5000f)+1)*30);
	}
	
}
