package net.tylermurphy.Kenbot.commands.music;

import java.net.URL;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.Kenbot.Config;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.PlayerManager;

public class Play implements ICommand {
	
	private final YouTube youtube;
	
	public Play() {
		
		YouTube temp = null;
		
		try {
			
			temp = new YouTube.Builder(
					GoogleNetHttpTransport.newTrustedTransport(),
					JacksonFactory.getDefaultInstance(),
					null
			)
				.setApplicationName("Ken Bot")
				.build();
			
		} catch  (Exception e) {
			e.printStackTrace();
		}
		
		youtube = temp;
	}

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		User u = event.getAuthor();
		if(u.isBot()) return;
		TextChannel channel = event.getChannel();
		 AudioManager audioManager = event.getGuild().getAudioManager();
		 
		if(args.isEmpty()) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		String input = String.join(" ", args);
		
		if(!isUrl(input)) {
			String ytSearched = searchYoutube(input);
			
			if(ytSearched == null) {
				channel.sendMessage(":x: Unable to find youtube video with a simmilar name.").queue();
				return;
			}
			
			input = ytSearched;
		}
		
		PlayerManager manager = PlayerManager.getInstance();
		if(!audioManager.isConnected()) {
			GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
	        if (!memberVoiceState.inVoiceChannel()) {
	            channel.sendMessage(":x: Please join a voice channel first").queue();
	            return;
	        }
	        VoiceChannel voiceChannel = memberVoiceState.getChannel();
	        Member selfMember = event.getGuild().getSelfMember();
	        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
	            channel.sendMessageFormat(":x: I am missing permission to join %s", voiceChannel).queue();
	            return;
	        }
	        audioManager.openAudioConnection(voiceChannel);
	        manager.loadAndPlay(event.getChannel(), input, u);
		} else if(manager.getGuildMusicManager(event.getGuild()).scheduler.isQueueLooped()) {
			channel.sendMessage(":x: Queue is currently looped").queue();
			return;
		} else {
			GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
			VoiceChannel voiceChannel = memberVoiceState.getChannel();
			VoiceChannel selfVoiceChannel = audioManager.getConnectedChannel();
			if(voiceChannel.getIdLong() == selfVoiceChannel.getIdLong()) {
				manager.loadAndPlay(event.getChannel(), input, u);
			} else {
				channel.sendMessage(":x: Please join the same voice channel first").queue();
			}
		}
	}
	
	private boolean isUrl(String input) {
		try {
			new URL(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	 private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.YOUTUBE_API_KEY)
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

	public String getInvoke() {
		return "play";
	}
	
	public String getUsage() {
		return "Play <video name or URL>";
	}
	
	public String getDescription() {
		return "Play a song on Ken";
	}
	
	public Permission requiredPermission() {
		return null;
	}

}
