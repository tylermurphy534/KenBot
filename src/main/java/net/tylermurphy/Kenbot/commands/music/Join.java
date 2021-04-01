package net.tylermurphy.Kenbot.commands.music;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.GuildMusicManager;
import net.tylermurphy.Kenbot.music.PlayerManager;

public class Join implements ICommand {
	
	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        List<Role> roles = event.getMember().getRoles();
		boolean hasDJRole = false;
        for(Role role : roles) {
			if(role.getName().equalsIgnoreCase("dj")) {
				hasDJRole = true;
				break;
			}
		}
        
        if (audioManager.isConnected() && hasDJRole == false) {
            channel.sendMessage(":x: I'm already connected to a channel").queue();
            return;
        }

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
        PlayerManager manager = PlayerManager.getInstance();
        GuildMusicManager musicManager = manager.getGuildMusicManager(event.getGuild());
        musicManager.autoLeaveManager.startTimeout(event.getGuild());
        
        channel.sendMessage("Joining your voice channel").queue();
	}
	
	public String getInvoke() { 
		return "join";
	}
	
	public String getUsage() {
		return "";
	}
	
	public String getDescription() {
		return "Join Ken to the VC";
	}
	
	public Permission requiredPermission() {
		return null;
	}
	
}
