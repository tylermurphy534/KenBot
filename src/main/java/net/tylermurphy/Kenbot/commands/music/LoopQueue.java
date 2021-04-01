package net.tylermurphy.Kenbot.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.GuildMusicManager;
import net.tylermurphy.Kenbot.music.MusicPermissions;
import net.tylermurphy.Kenbot.music.PlayerManager;

public class LoopQueue implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		AudioManager audioManager = event.getGuild().getAudioManager();
		VoiceChannel voiceChannel = audioManager.getConnectedChannel();
		
		if (player.getPlayingTrack() == null) {
			channel.sendMessage(":x: Nothing is currently playing.").queue();
			return;
		}
		
		boolean allowed = MusicPermissions.hasDJ(event.getMember().getRoles(), voiceChannel);
		
		if(allowed) {
			boolean loop = !musicManager.scheduler.isQueueLooped();
			if(loop) {
				musicManager.scheduler.loopQueue();
			} else {
				musicManager.scheduler.unLoopQueue();
			}
			channel.sendMessage(String.format("%s Queue Looping %s.",
					loop ? ":arrows_counterclockwise:" : ":arrow_forward:",
					loop ? "enabled" : "disabled"
					)).queue();
		} else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
	}

	public String getInvoke() {
		return "loopqueue";
	}

	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Loop entire music queue, also locks queue";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
