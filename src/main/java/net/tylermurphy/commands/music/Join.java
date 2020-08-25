package net.tylermurphy.commands.music;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;

public class Join implements ICommand {
	
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		
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
        channel.sendMessage("Joining your voice channel").queue();
	}
	
	public String getInvoke() { 
		return "join";
	}
	
}
