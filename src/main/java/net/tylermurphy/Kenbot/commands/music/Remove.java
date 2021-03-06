package net.tylermurphy.Kenbot.commands.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.GuildMusicManager;
import net.tylermurphy.Kenbot.music.MusicPermissions;
import net.tylermurphy.Kenbot.music.PlayerManager;

public class Remove implements ICommand {

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
		
		if (args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		boolean allowed = MusicPermissions.hasDJ(event.getMember().getRoles(), voiceChannel);
		
		if(allowed) {
			int num = 0;
			try {
				num = Integer.parseInt(args.get(0));
			}catch(Exception e) {
				channel.sendMessage(":x: Error parsing integer: "+args.get(0)).queue();
				return;
			}
			boolean success = musicManager.scheduler.removeFromQueue(num-1);
			if(success) channel.sendMessage(":white_check_mark: Removed track successfully").queue();
			else channel.sendMessage(":x: Track not found").queue();
		} else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
	}

	public String getInvoke() {
		return "remove";
	}
	
	public String getUsage() {
		return "Remove <position in queue>";
	}
	
	public String getDescription() {
		return "Remove song in queue";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
