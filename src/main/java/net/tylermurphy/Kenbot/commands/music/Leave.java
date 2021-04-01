package net.tylermurphy.Kenbot.commands.music;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.Kenbot.commands.ICommand;
import net.tylermurphy.Kenbot.music.MusicPermissions;

public class Leave implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();
        
        if (!audioManager.isConnected()) {
            channel.sendMessage(":x: I'm not connected to a voice channel.").queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            channel.sendMessage(":x: You have to be in the same voice channel as me to use this command.").queue();
            return;
        }
        
        boolean allowed = MusicPermissions.hasDJ(event.getMember().getRoles(), voiceChannel);
		
		if(allowed) {
				audioManager.closeAudioConnection();
		        channel.sendMessage("Disconnected from your channel").queue();
				return;
		}else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
    }

    public String getInvoke() {
        return "leave";
    }
    
    public String getUsage() {
		return "";
	}
    
    public String getDescription() {
		return "Remove Ken from the VC";
	}
    
    public Permission requiredPermission() {
		return null;
	}
	
}
