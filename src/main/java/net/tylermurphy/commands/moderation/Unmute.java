package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Unmute implements ICommand {
	
	public void handle(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty()) {
			channel.sendMessage(":x: Missing Arguments").queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(target)) {
			channel.sendMessage(":x: You dont have permission to use this command.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.MANAGE_ROLES) || !selfMember.canInteract(target)) {
			channel.sendMessage(":x: I can't manage roles that user or I don't have permission to manage roles.").queue();
			return;
		}
		
		List<Role> roles = event.getGuild().getRolesByName("muted", true);
		Role mutedRole;
		
		if(roles.isEmpty()) {
			channel.sendMessage(":x: There is no role name muted on this server.").queue();
			return;
		}
		
		mutedRole = roles.get(0);
		
		List<Role> targetRoles = target.getRoles();
		boolean isMuted = false;
		for(Role role : targetRoles) {
			if(role.getId().equals(mutedRole.getId())) {
				isMuted = true; 
				break;
			}
		}
		
		if(!isMuted) {
			channel.sendMessage(":x: User is not muted.").queue();
			return;
		}
		
		event.getGuild().removeRoleFromMember(target, mutedRole).queue();
		channel.sendMessage(String.format("%s unmuted %s.",event.getAuthor(),target)).queue();

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s unmuted you in %s",event.getAuthor(),event.getGuild()));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}

	public String getInvoke() {
		return "unmute";
	}
	
}
