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

public class SoftBan implements ICommand {

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
		
		if (!member.hasPermission(Permission.BAN_MEMBERS) || !member.canInteract(target)) {
			channel.sendMessage(":x: You dont have permission to use this command.").queue();
			return;
		}
		
		if (!member.hasPermission(Permission.BAN_MEMBERS) || !selfMember.canInteract(target)) {
			channel.sendMessage(":x: I can't softban that user or I don't have permission to softban members.").queue();
			return;
		}

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.red)
				.setDescription(String.format("%s softbanned you in %s, for reason: `%s`",event.getAuthor(),event.getGuild(),reason));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue((message) -> {
				event.getGuild().ban(target, 0, String.format("softban by %s, with reason: %s", 
				event.getAuthor(), reason)).queue();;
				channel.sendMessage(String.format("%s softbanned %s, for reason: `%s`",event.getAuthor(),target,reason)).queue();
			});
		});
		
	}

	public String getInvoke() {
		return "softban";
	}
	
	public String getUsage() {
		return "SoftBan <@User> <reason>";
	}
	
	public String getDescription() {
		return "Ban a user without deleting their messages";
	}
	
	public Permission requiredPermission() {
		return Permission.BAN_MEMBERS;
	}
	
}
