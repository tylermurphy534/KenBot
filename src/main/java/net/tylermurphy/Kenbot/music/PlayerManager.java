package net.tylermurphy.Kenbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(guild, playerManager);
            musicManagers.put(guildId, musicManager);
            musicManager.scheduler.musicManager = musicManager;
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, User u) {
    	
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
            	
            	if(musicManager.boundChannel == null) {
        			musicManager.boundChannel = channel;
    	        	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    	        			.setTitle("**Bound Text Channel**")
    	        			.setDescription(String.format(
    	        					"Bound music messages to text channel %s",
    	        					channel
    	        			))
    	        			.setColor(Color.DARK_GRAY);
    	        	channel.sendMessage(builder.build()).queue();
    	        }
            	
            	AudioTrackInfo info = track.getInfo();
            	String videoURL = info.uri;
        		String videoID = videoURL.substring(videoURL.indexOf("=")+1);
        		String imageURL = String.format("https://img.youtube.com/vi/%s/default.jpg",videoID);
        		
            	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    				.setTitle("**Queued**")
    				.setDescription(String.format(
    					"[%s](%s)", 
    					info.title, 
    					info.uri
    				))
    				.setFooter(String.format(
    					"In position #%s",
    					musicManager.scheduler.getLength()+1
    				))
    				.setThumbnail(imageURL)
    				.setColor(Color.DARK_GRAY);
                channel.sendMessage(builder.build()).queue();
                track.setUserData(u);
                play(musicManager, track);
                
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
            	
            	if(musicManager.boundChannel == null) {
        			musicManager.boundChannel = channel;
    	        	EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    	        			.setTitle("**Bound Text Channel**")
    	        			.setDescription(String.format(
    	        					"Bound music messages to text channel %s",
    	        					channel
    	        			))
    	        			.setColor(Color.DARK_GRAY);
    	        	channel.sendMessage(builder.build()).queue();
    	        }
            	
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                } else {
                	firstTrack.setUserData(u);
                }

                String videoURL = firstTrack.getInfo().uri;
        		String videoID = videoURL.substring(videoURL.indexOf("=")+1);
        		String imageURL = String.format("https://img.youtube.com/vi/%s/default.jpg",videoID);
                
                EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
        				.setTitle("**Queued**")
        				.setDescription(String.format(
        					"[%s](%s)", 
        					playlist.getName(), 
        					playlist.getTracks().get(0).getInfo().uri
        				))
        				.setFooter(String.format(
        					"First track of playlist in position #%s",
        					musicManager.scheduler.getLength()-playlist.getTracks().size()+1
        				))
        				.setThumbnail(imageURL)
        				.setColor(Color.DARK_GRAY);
                
                    channel.sendMessage(builder.build()).queue();

                play(musicManager, firstTrack);
                
                for(AudioTrack track : playlist.getTracks().subList(1, playlist.getTracks().size())) {
                	track.setUserData(u);
                	musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
    
}