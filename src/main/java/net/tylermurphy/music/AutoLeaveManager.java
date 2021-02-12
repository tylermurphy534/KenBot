package net.tylermurphy.music;

import java.util.Timer;
import java.util.TimerTask;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class AutoLeaveManager extends ListenerAdapter {

	
	Timer timer;
	TimeoutTask task;
	Guild guild;
	
	public void startTimeout(Guild guild) {
		task = new TimeoutTask(guild);
		timer = new Timer();
		timer.schedule(task, 0, 1 * 5000);
	}
	
	public void stopTimeout() {
		if(task == null) return;
		timer.cancel();
		timer = null;
		task = null;
	}
	
}

class TimeoutTask extends TimerTask {

	public TimeoutTask(Guild guild) {
		
		PlayerManager manager = PlayerManager.getInstance();
		GuildMusicManager musicManager = manager.getGuildMusicManager(guild);
		
		this.channel = musicManager.boundChannel;
		this.manager = guild.getAudioManager();
	}
	
	private AudioManager manager;
	private long lastTime = 0;
	private TextChannel channel;
	
	public void run() {
		if(lastTime == 0) {
			lastTime = System.nanoTime();
		}
		if( (System.nanoTime() - lastTime) / 1_000_000_000L > 5 * 60) {
			EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
					.setDescription("Left voice channel due to bot inactivity.");
			channel.sendMessage(builder.build()).queue();
			manager.closeAudioConnection();
			this.cancel();
		}
	}

	
}
