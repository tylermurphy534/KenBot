package net.tylermurphy.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;
import net.tylermurphy.music.TrackScheduler;

public class ForceSkip implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		TrackScheduler scheduler = musicManager.scheduler;
		AudioPlayer player = musicManager.player;
		AudioManager audioManager = event.getGuild().getAudioManager();
		VoiceChannel voiceChannel = audioManager.getConnectedChannel();
		AudioTrack track = player.getPlayingTrack();
		
		if (track == null) {
			channel.sendMessage(":x: Nothing is currently playing.").queue();
			return;
		}
		
		if(voiceChannel == null) {
			channel.sendMessage(":x: Im not connected to a voice channel.").queue();
			return;
		}
		
		List<Role> roles = event.getMember().getRoles();
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase("dj")) {
				channel.sendMessage(":arrow_right: Skipping the current track").queue();
				scheduler.nextTrack();
				return;
			}
		}
		
		channel.sendMessage(":x: You must have the `DJ` role to do this.").queue();
		
	}

	public String getInvoke() {
		return "fskip";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Force skip current playing song";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
