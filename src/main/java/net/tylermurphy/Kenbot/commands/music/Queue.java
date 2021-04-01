package net.tylermurphy.Kenbot.commands.music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.GuildMusicManager;
import net.tylermurphy.Kenbot.music.PlayerManager;

public class Queue implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
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
		EmbedBuilder builder = EmbedUtils.getDefaultEmbed().setTitle("Current Queue (Total: " + queue.size() + ")");
		
		for(int i = 0; i < trackCount; i++) {
			AudioTrack track = tracks.get(i);
			AudioTrackInfo info = track.getInfo();
			
			builder.appendDescription(String.format(
					"**%s.** %s - %s\n",
					i+1,
					info.title,
					info.author
			));
		}
		
		channel.sendMessage(builder.build()).queue();
	}

	public String getInvoke() {
		return "queue";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "See current song queue";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
