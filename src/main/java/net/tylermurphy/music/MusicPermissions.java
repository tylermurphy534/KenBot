package net.tylermurphy.music;

import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicPermissions {

	public static boolean hasDJ(List<Role> roles, VoiceChannel voiceChannel) {
		boolean hasDJRole = false;
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase("dj")) {
				hasDJRole = true;
				break;
			}
		}
		List<Member> members = voiceChannel.getMembers();
		int people = 0;
		for(Member member : members) {
			if(!member.getUser().isBot())
				people++;
		}
		if(people == 1 || hasDJRole) {
			return true;
		} else {
			return false;
		}
	}
	
}
