package net.tylermurphy.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean looping,queueLooped;
    private AudioTrack lastTrack;
    public TextChannel boundTextChannel;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        looping = false;
        queueLooped = false;
    }
    
    private boolean play(AudioTrack track, boolean dontForce) {
    	boolean success = player.startTrack(track, dontForce);
    	if(success) {
    		EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
    				.setTitle("Now Playing",track.getInfo().uri)
    				.setDescription(String.format(
    						"**Title:** %s\n**Author:** %s",
    						track.getInfo().title,
    						track.getInfo().author
    					));
    		boundTextChannel.sendMessage(embed.build()).queue();
    	}
    	return success;
    }

    public void queue(AudioTrack track) {
    	lastTrack = track;
    	if (!play(track, true)) {
            queue.offer(track);
        }
    }
    
    public BlockingQueue<AudioTrack> getQueue() {
    	return queue;
    }
    
    public void loopQueue() {
    	queueLooped = true;
    }
    
    public void unLoopQueue() {
    	queueLooped = false;
    }

    public void nextTrack() {
    	lastTrack = queue.poll();
    	if(lastTrack == null) {
    		boundTextChannel = null;
    		looping = false;
    		queueLooped = false;
    		return;
    	}
    	play(lastTrack.makeClone(), false);
    	if(queueLooped)  queue.offer(lastTrack.makeClone());
    }
    
    public void repeatTrack() {
    	play(lastTrack.makeClone(), false);
    }

    public boolean removeFromQueue(int num) {
    	int i = 0;
    	Iterator<AudioTrack> itr = queue.iterator();
    	while(itr.hasNext()) {
    		AudioTrack t = itr.next();
    		if(num == i) {
				return queue.remove(t);
    		}
    		i++;
    	}
    	return false;
    }
    
    public void setLooped(boolean value) {
    	looping = value;
    }
    
    public boolean isLooped() {
    	return looping;
    }
    
    public boolean isQueueLooped() {
    	return queueLooped;
    }

    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    	if (endReason.mayStartNext) {
            if(looping) {
            	repeatTrack();
            } else {
            	nextTrack();
            }
        }
    }
}
