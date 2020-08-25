package net.tylermurphy.commands.music;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;

public class NowPlaying implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		AudioPlayer player = musicManager.player;
		
		if(player.getPlayingTrack() == null) {
			channel.sendMessage(":x: The Player is not playing any song.").queue();
			return;
		}
		
		AudioTrackInfo info = player.getPlayingTrack().getInfo();
		
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setTitle("***Now Playing***")
				.setColor(Color.YELLOW)
				.setFooter("MurphyBot", null);
		
		builder.appendDescription(info.title);
		builder.appendDescription("\n");
		builder.appendDescription(info.uri);
		builder.appendDescription("\n");
		builder.appendDescription(String.format(
			"%s %s : %s",
			player.isPaused() ? "\u23F8" : "\u23F5",
			formatTime(player.getPlayingTrack().getPosition()),
			formatTime(player.getPlayingTrack().getDuration())
		));
		
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "np";
	}
	
	private String formatTime(long timeInMillis) {
		final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
		final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
		final long seconeds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconeds);
	}

}
