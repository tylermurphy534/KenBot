package net.tylermurphy.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean looping,queueLooped;
    private AudioTrack lastTrack;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        looping = false;
        queueLooped = false;
    }

    public void queue(AudioTrack track) {
    	lastTrack = track;
    	if (!player.startTrack(track, true)) {
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
    	player.startTrack(lastTrack.makeClone(), false);
    	if(queueLooped)  queue.offer(lastTrack.makeClone());
    }
    
    public void repeatTrack() {
    	player.startTrack(lastTrack.makeClone(), false);
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
