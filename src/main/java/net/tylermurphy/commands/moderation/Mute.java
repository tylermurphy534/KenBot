package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.tylermurphy.commands.ICommand;

public class Mute implements ICommand {

	public void invoke(List<String> args, GuildMessageReceivedEvent event) {
		TextChannel channel = event.getChannel();
		Member member = event.getMember();
		Member selfMember = event.getGuild().getSelfMember();
		List<Member> metionedMembers = event.getMessage().getMentionedMembers();
		
		if(metionedMembers.isEmpty() || args.size() < 2) {
			EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.appendDescription("**:x: Incorrect Command Usage**\n")
					.appendDescription(getUsage() +"\n"+ getDescription());
			channel.sendMessage(embed.build()).queue();
			return;
		}
		
		if(metionedMembers.size() > 1) {
			channel.sendMessage(":x: Please only mention one user.").queue();
			return;
		}
		
		Member target = metionedMembers.get(0);
		String reason = String.join(" ", args.subList(1, args.size()));
		
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
		try{
			mutedRole = roles.get(0);
		}catch(Exception e) {
			channel.sendMessage(":x: Unable to find role names `Muted`").queue();
			return;
		}
		
		try {
		event.getGuild().addRoleToMember(target, mutedRole).queue();
		}catch(HierarchyException e) {
			channel.sendMessage(":x: I can't manage the Muted role, it is higher than my max role in the role list!").queue();
		}
		
		channel.sendMessage(String.format("%s muted %s, for reason: `%s`",event.getAuthor(),target,reason)).queue();

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s muted you in %s, for reason: `%s`",event.getAuthor(),event.getGuild(),reason));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}

	public String getInvoke() {
		return "mute";
	}
	
	public String getUsage() {
		return "Mute <@User> <reason>";
	}
	
	public String getDescription() {
		return "Mute a user";
	}
	
	public Permission requiredPermission() {
		return Permission.KICK_MEMBERS;
	}

}
