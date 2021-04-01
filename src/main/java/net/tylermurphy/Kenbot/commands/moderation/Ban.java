package net.tylermurphy.Kenbot.commands.moderation;

import java.awt.Color;
import java.util.List;

import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.tylermurphy.Kenbot.commands.ICommand;

public class Ban implements ICommand {

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
			channel.sendMessage(":x: I can't ban that user or I don't have permission to ban members.").queue();
			return;
		}

		EmbedBuilder builder = new EmbedBuilder()
				.setTitle("Infraction Notice")
				.setColor(Color.red)
				.setDescription(String.format("%s banned you in %s, for reason: `%s`",event.getAuthor(),event.getGuild(),reason));
		
		target.getUser().openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(builder.build()).queue((message) -> {
				event.getGuild().ban(target, 1, String.format("Ban by %s, with reason: %s", 
				event.getAuthor(), reason)).queue();
		channel.sendMessage(String.format("%s banned %s, for reason: `%s`",event.getAuthor(),target,reason)).queue();
			});
		});
		
	}

	public String getInvoke() {
		return "ban";
	}
	
	public String getUsage() {
		return "Ban <@User> <reason>";
	}
	
	public String getDescription() {
		return "Ban a user";
	}
	
	public Permission requiredPermission() {
		return Permission.BAN_MEMBERS;
	}
	
}
