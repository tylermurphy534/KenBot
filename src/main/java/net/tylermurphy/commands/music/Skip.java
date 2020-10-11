package net.tylermurphy.commands.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;
import net.tylermurphy.music.TrackScheduler;

public class Skip implements ICommand {
	
	HashMap<AudioTrack, List<String>> skips = new HashMap<AudioTrack, List<String>>();

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
			channel.sendMessage(":x: Nothing is playing currently you bafoon.").queue();
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
				skips.remove(track);
				scheduler.nextTrack();
				return;
			}
		}
		
		if(skips.get(track) == null) {
			List<String> users = new ArrayList<String>();
			users.add(event.getAuthor().getDiscriminator());
			skips.put(track, users);
		} else {
			List<String> users = skips.get(track);
			boolean found = false;
			for(String s : users) {
				if(event.getAuthor().getDiscriminator().equals(s)) {
					found = true;
					break;
				}
			}
			if(!found) {
				users.add(event.getAuthor().getDiscriminator());
				skips.put(track, users);
			}
		}
		
		List<Member> members = voiceChannel.getMembers();
		int people = 0;
		for(Member member : members) {
			if(!member.getUser().isBot())
				people++;
		}
		int skipsNeeded = (int) Math.ceil(people/2.0);
		if(skips.get(track).size()>=skipsNeeded) {
			channel.sendMessage(":arrow_right: Skipping the current track").queue();
			skips.remove(track);
			scheduler.nextTrack();
		}else {
			channel.sendMessage(String.format(":arrow_right: Skips (%s/%s) for skipping current track.",skips.get(track).size(),skipsNeeded)).queue();
		}
		
	}

	public String getInvoke() {
		return "skip";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Skip current playing song";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
