package net.tylermurphy.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.commands.ICommand;

public class Kick implements ICommand {

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
		
		if (!member.hasPermission(Permission.KICK_MEMBERS) || !member.canInteract(target)) {
			channel.sendMessage(":x: You dont have permission to use this command.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.KICK_MEMBERS) || !selfMember.canInteract(target)) {
			channel.sendMessage(":x: I can't kick that user or I don't have permission to kick members.").queue();
			return;
		}
		
		event.getGuild().kick(target, String.format("Kick by %s, with reason: %s", 
				event.getAuthor(), reason)).queue();;
		channel.sendMessage(String.format("%s kicked %s, for reason: `%s`",event.getAuthor(),target,reason)).queue();

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.yellow)
				.setDescription(String.format("%s kicked you in %s, for reason: `%s`",event.getAuthor(),event.getGuild(),reason));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue();
		});
	}

	public String getInvoke() {
		return "kick";
	}
	
	public String getUsage() {
		return "Kick <@User> <reason>";
	}
	
	public String getDescription() {
		return "Kick a user";
	}
	
	public Permission requiredPermission() {
		return Permission.KICK_MEMBERS;
	}

}
