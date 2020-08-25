package net.tylermurphy.commands.music;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.tylermurphy.commands.ICommand;

public class Leave implements ICommand {

	public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        AudioManager audioManager = event.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            channel.sendMessage(":x: I'm not connected to a voice channelyou bafoon.").queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(event.getMember())) {
            channel.sendMessage(":x: You have to be in the same voice channel as me to use this you dumbo.").queue();
            return;
        }
        
        List<Role> roles = event.getMember().getRoles();
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase("dj")) {
				audioManager.closeAudioConnection();
		        channel.sendMessage("Disconnected from your channel").queue();
				return;
			}
		}

		List<Member> members = voiceChannel.getMembers();
		int people = 0;
		for(Member member : members) {
			if(!member.getUser().isBot())
				people++;
		}
		if(people == 1) {
	        audioManager.closeAudioConnection();
	        channel.sendMessage("Disconnected from your channel").queue();
		}else {
			channel.sendMessage(":x: You must be the only person in the VC or have the `DJ` role to do this.").queue();
		}
    }

    public String getInvoke() {
        return "leave";
    }
	
}
