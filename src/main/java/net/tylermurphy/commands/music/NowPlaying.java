package net.tylermurphy.commands.music;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;

public class NowPlaying implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		
		if(player.getPlayingTrack() == null) {
			channel.sendMessage(":x: Im not currently playing anything!").queue();
			return;
		}
		
		AudioTrackInfo info = player.getPlayingTrack().getInfo();
		String videoURL = info.uri;
		String videoID = videoURL.substring(videoURL.indexOf("=")+1);
		String imageURL = String.format("https://img.youtube.com/vi/%s/default.jpg",videoID);
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
    			.setTitle("**Now Playing**")
    			.setDescription(String.format(
    				"[%s](%s)\n`[%s/%s]`\n\nRequested by %s", 
    				info.title, 
    				info.uri,
    				formatTime(player.getPlayingTrack().getPosition()), 
    				formatTime(player.getPlayingTrack().getDuration()),
    				(User)player.getPlayingTrack().getUserData()
    			)).setThumbnail(imageURL);
		
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "np";
	}
	
	private String formatTime(long timeInMillis) {
		final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
		final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
		final long seconeds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
		
		if(hours == 0) {
			return String.format("%02d:%02d", minutes, seconeds);
		} else {
			return String.format("%02d:%02d:%02d", hours, minutes, seconeds);
		}
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "See what is currently playing in VC";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
