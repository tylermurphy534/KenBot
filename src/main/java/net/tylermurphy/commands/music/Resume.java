package net.tylermurphy.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;

public class Resume implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		AudioManager audioManager = event.getGuild().getAudioManager();
		VoiceChannel voiceChannel = audioManager.getConnectedChannel();
		
		if (player.getPlayingTrack() == null) {
			channel.sendMessage(":x: Nothing is playing currently you bafoon.").queue();
			return;
		}
		
		if (!player.isPaused()) {
			channel.sendMessage(":x: Track is already playing you bafoon.").queue();
			return;
		}
		
		boolean hasDJRole = false;
		List<Role> roles = event.getMember().getRoles();
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase("dj")) {
				hasDJRole = true;
				break;
			}
		}
		List<Member> members = voiceChannel.getMembers();
		int people = 0;
		for(Member member : members) {
			if(!member.getUser().isBot())
				people++;
		}
		if(people == 1 || hasDJRole) {
			player.setPaused(false);
			channel.sendMessage(":arrow_forward: Resumed current track.").queue();
		} else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
	}

	public String getInvoke() {
		return "resume";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Resume current track";
	}
	
}
