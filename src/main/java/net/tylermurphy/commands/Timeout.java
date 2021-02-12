package net.tylermurphy.commands;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.TextChannel;

public class Timeout {
	
	Timer timer;
	TimeoutTask task;
	
	public void startTimeout(int interval, TextChannel channel, String timeoutMessage) {
		task = new TimeoutTask(interval,channel,timeoutMessage);
		timer = new Timer();
		timer.schedule(task, 1 * 1000, 1 * 1000);
	}
	
	public void stopTimeout() {
		task.cancel();
	}
	
	public void refreshTimeout() {
		task.lastTime = System.nanoTime();
	}
	
	public boolean isTimedOut() {
		return task.timedOut;
	}
	
}

class TimeoutTask extends TimerTask {

	public TimeoutTask(int interval,TextChannel channel, String timeoutMessage) {
		this.interval = interval;
		this.channel = channel;
		this.timeoutMessage = timeoutMessage;
	}
	
	private int interval;
	public long lastTime= 0;
	private TextChannel channel;
	private String timeoutMessage;
	public boolean timedOut = false;
	
	public void run() {
		if(lastTime == 0) {
			lastTime = System.nanoTime();
		}
		if(System.nanoTime() - lastTime > interval * 1_000_000_000L) {
			channel.sendMessage(timeoutMessage).queue();
			timedOut = true;
			this.cancel();
		}
	}

}
