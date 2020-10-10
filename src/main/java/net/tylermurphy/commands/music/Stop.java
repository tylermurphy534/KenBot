package net.tylermurphy.commands.music;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;

public class Stop implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioManager audioManager = event.getGuild().getAudioManager();
		VoiceChannel voiceChannel = audioManager.getConnectedChannel();

		if(voiceChannel == null) {
			channel.sendMessage(":x: I am not connected to a voice channel").queue();
            return;
		}
		
        if (!voiceChannel.getMembers().contains(event.getMember())) {
            channel.sendMessage(":x: You have to be in the same voice channel as me to use this").queue();
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
			musicManager.scheduler.getQueue().clear();
			musicManager.player.stopTrack();
			musicManager.player.setPaused(false);
			channel.sendMessage("Stopped the music and cleared the song queue.").queue();
			musicManager.scheduler.boundTextChannel = null;
			musicManager.scheduler.unLoopQueue();
			musicManager.scheduler.setLooped(false);
		} else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
	}

	public String getInvoke() {
		return "stop";
	}

	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Stop current playing songs and clear queue";
	}
	
}
