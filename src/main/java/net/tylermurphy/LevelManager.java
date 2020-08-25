package net.tylermurphy;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.database.DatabaseManager;

public class LevelManager {

	private static List<String> WAITING_FLAGS = new ArrayList<String>();
	
	public void handleMessage(GuildMessageReceivedEvent event) {
		if(!WAITING_FLAGS.contains(String.valueOf(event.getAuthor().getIdLong()))) {
			handleMember(event);
			return;
		}
		return;
	}
	
	private void handleMember(GuildMessageReceivedEvent event) {
		final int delay = 60;
		WAITING_FLAGS.add(String.valueOf(event.getAuthor().getIdLong()));
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int counter = 0;
			public void run() {
				counter++;
				if(counter == 2) {
					WAITING_FLAGS.remove(String.valueOf(event.getAuthor().getIdLong()));
					this.cancel();
				}
			}
		};
		timer.schedule(task, 0, delay * 1000);
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
		return (int) (Math.max(Math.exp(Math.log(xp/20)/1.1),0))+1;
	}
	
}
