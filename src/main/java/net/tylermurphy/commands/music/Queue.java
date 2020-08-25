package net.tylermurphy.commands.music;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;
import net.tylermurphy.music.GuildMusicManager;
import net.tylermurphy.music.PlayerManager;

public class Queue implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		PlayerManager playerManager = PlayerManager.getInstance();
		GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		
		if(queue.isEmpty()) {
			channel.sendMessage("Queue is empty.").queue();
			return;
		}
		
		int trackCount = Math.min(queue.size(), 20);
		List<AudioTrack> tracks = new ArrayList<>(queue);
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed()
				.setTitle("Current Queue (Total: " + queue.size() + ")")
				.setColor(Color.YELLOW)
				.setFooter("MurphyBot", null);
		
		for(int i = 0; i < trackCount; i++) {
			AudioTrack track = tracks.get(i);
			AudioTrackInfo info = track.getInfo();
			
			builder.appendDescription(String.format(
					"**%s.** %s - %s\n",
					i,
					info.title,
					info.author
			));
		}
		
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "queue";
	}

}
